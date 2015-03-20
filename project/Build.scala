import org.flywaydb.sbt.FlywayPlugin._
import sbt._, Keys._
import scalikejdbc._
import scalikejdbc.mapper.SbtPlugin.JDBCSettings
import scalikejdbc.mapper._

object build extends Build {

  private val mysql = "mysql" % "mysql-connector-java" % buildinfo.BuildInfo.mysqlDriverVersion

  private val defaultSchema = "schema_" + System.currentTimeMillis
  val databaseSchema = SettingKey[String]("databaseSchema")
  private val host = "localhost"

  private val jdbcSettings = Def.setting{
    val schema = databaseSchema.value
    SbtPlugin.JDBCSettings(
      driver = "com.mysql.jdbc.Driver",
      url = s"jdbc:mysql://$host/$schema",
      username = "root",
      password = "",
      schema = schema 
    )
  }

  private def generatorSettings(tables: Map[String, String], packageName: String) =
    SbtPlugin.scalikejdbcSettings ++ inConfig(Compile)(Seq(
      SbtKeys.scalikejdbcJDBCSettings := jdbcSettings.value,
      SbtKeys.scalikejdbcGeneratorSettings := null,
      SbtKeys.scalikejdbcCodeGeneratorAll := { (jdbc, _) =>
        val config = GeneratorConfig(
          srcDir = scalaSource.value.getAbsolutePath,
          testTemplate = GeneratorTestTemplate(""),
          packageName = packageName,
          caseClassOnly = true,
          defaultAutoSession = false
        )
        try {
          Class.forName(jdbc.driver)
          val model = Model(jdbc.url, jdbc.username, jdbc.password)
          model.allTables(jdbc.schema).filter(table => tables.contains(table.name)).map { table =>
            new CodeGenerator(table.copy(schema = None), tables.get(table.name))(config)
          }
        } finally {
          scalikejdbc.ConnectionPool.closeAll()
        }
      }
    )) ++ Seq(
      TaskKey[Unit]("showDatabases") := {
        val query = scalikejdbc.SQL[Any]("""SHOW DATABASES;""")
        executeQuery(jdbcSettings.value, query){ (sql, session) =>
          implicit val s = session
          sql.map(_.string(1)).list().apply().foreach(println)
        }
      },
      TaskKey[Unit]("checkGeneratedCode") := {
        val diff = "git diff".!!
        if(diff.nonEmpty){
          sys.error(diff)
        }
      }
    )

  private def executeQuery[A, C](jdbc: JDBCSettings, sql: SQL[A, NoExtractor])(f: (SQL[A, NoExtractor], DBSession) => C): C = {
    try {
      Class.forName(jdbc.driver)
      ConnectionPool.singleton(s"jdbc:mysql://$host/test", jdbc.username, jdbc.password)
      DB.autoCommit { session =>
        f(sql, session)
      }
    } finally {
      ConnectionPool.closeAll()
    }
  }

  private val commonSettings = Seq[Def.Setting[_]](
    scalaVersion := "2.11.6",
    licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
    databaseSchema <<= databaseSchema.??(defaultSchema),
    ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-feature",
      "-language:postfixOps",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:existentials"
    ),
    resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/releases/",
    updateOptions ~= {_.withCachedResolution(true)},
    javacOptions ++= Seq("-encoding", "UTF-8", "-target", "7", "-source", "7"),
    javaOptions ++= sys.process.javaVmArguments.filter(
      a => Seq("-Xmx", "-Xms", "-XX", "-Xss").exists(a.startsWith)
    ),
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.4" % "test"
    )
  )

  private def module(id: String): Project =
    Project(id, file(id)).settings(commonSettings: _*)

  lazy val migration = module("migration").settings(
    flywaySettings: _*
  ).settings(
    flywaySchemas := databaseSchema.value :: Nil,
    flywayUrl := s"jdbc:mysql://$host",
    flywayUser := jdbcSettings.value.username,
    libraryDependencies ++= Seq(
      mysql
    )
  )

  lazy val domain = module("domain").settings(
    generatorSettings(Map("users" -> "User"), "example.domain.generated"): _*
  ).settings(
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc" % scalikejdbc.ScalikejdbcBuildInfo.version,
      "com.typesafe.play" %% "play-json" % play.core.PlayVersion.current,
      mysql
    )
  )

  lazy val api = module("api").enablePlugins(play.PlayScala).settings(
    libraryDependencies ++= Seq(
    )
  ).dependsOn(domain)

}
