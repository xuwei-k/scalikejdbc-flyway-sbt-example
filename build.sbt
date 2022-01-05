import scalikejdbc.{JDBCSettings => _, _}
import scalikejdbc.mapper._
import scala.collection.JavaConverters._
import java.lang.management.ManagementFactory

val mysql = "mysql" % "mysql-connector-java" % buildinfo.BuildInfo.mysqlDriverVersion
val defaultSchema = "schema_" + System.currentTimeMillis
val databaseSchema = SettingKey[String]("databaseSchema")
val host = "localhost"

val jdbcSettings = Def.setting {
  val schema = databaseSchema.value
  JDBCSettings(
    driver = "com.mysql.jdbc.Driver",
    url = s"jdbc:mysql://$host/$schema",
    username = "root",
    password = "",
    schema = schema
  )
}

def generatorSettings(tables: Map[String, String], packageName: String) =
  ScalikejdbcPlugin.projectSettings ++ inConfig(Compile)(
    Seq(
      scalikejdbcJDBCSettings := jdbcSettings.value,
      scalikejdbcGeneratorSettings := null,
      scalikejdbcCodeGeneratorAll := { (jdbc, _) =>
        val config = GeneratorConfig(
          srcDir = scalaSource.value.getAbsolutePath,
          testTemplate = GeneratorTestTemplate(""),
          packageName = packageName,
          defaultAutoSession = false,
          dateTimeClass = DateTimeClass.OffsetDateTime
        )
        try {
          Class.forName(jdbc.driver)
          val model = Model(jdbc.url, jdbc.username, jdbc.password)
          model
            .allTables(jdbc.schema)
            .filter(table => tables.contains(table.name))
            .map { table =>
              new CodeGenerator(
                table.copy(schema = None),
                tables.get(table.name)
              )(config)
            }
        } finally {
          scalikejdbc.ConnectionPool.closeAll()
        }
      }
    )
  ) ++ Seq(
    TaskKey[Unit]("showDatabases") := {
      val query = scalikejdbc.SQL[Any]("""SHOW DATABASES;""")
      executeQuery(jdbcSettings.value, query) { (sql, session) =>
        implicit val s = session
        sql.map(_.string(1)).list.apply.foreach(println)
      }
    },
    TaskKey[Unit]("checkGeneratedCode") := {
      val diff = sys.process.Process("git diff").!!
      if (diff.nonEmpty) {
        sys.error(diff)
      }
    }
  )

def executeQuery[A, C](jdbc: JDBCSettings, sql: SQL[A, NoExtractor])(
    f: (SQL[A, NoExtractor], DBSession) => C
): C = {
  try {
    Class.forName(jdbc.driver)
    ConnectionPool.singleton(
      s"jdbc:mysql://$host/test",
      jdbc.username,
      jdbc.password
    )
    DB.autoCommit { session =>
      f(sql, session)
    }
  } finally {
    ConnectionPool.closeAll()
  }
}

val commonSettings = Def.settings(
  libraryDependencySchemes += "org.scala-lang.modules" %% "scala-parser-combinators" % "always",
  scalaVersion := "2.13.7",
  run / fork := true,
  licenses := Seq(
    "public domain" -> url(
      "https://raw.githubusercontent.com/xuwei-k/scalikejdbc-flyway-sbt-example/master/LICENSE"
    )
  ),
  databaseSchema := databaseSchema.??(defaultSchema).value,
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials"
  ),
  javacOptions ++= Seq("-encoding", "UTF-8"),
  javaOptions ++= ManagementFactory.getRuntimeMXBean.getInputArguments.asScala.toList
    .filter(a => Seq("-Xmx", "-Xms", "-XX", "-Xss").exists(a.startsWith)),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.10" % "test"
  )
)

addCommandAlias("flywayMigrate", "domain/test:runMain FlywayMigrate migrate")
addCommandAlias("flywayClean", "domain/test:runMain FlywayMigrate clean")

def module(id: String): Project =
  Project(id, file(id)).settings(commonSettings)

lazy val domain = module("domain")
  .settings(
    generatorSettings(Map("users" -> "User"), "example.domain.generated")
  )
  .settings(
    Test / javaOptions ++= Seq(
      "flyway.schema" -> databaseSchema.value,
      "flyway.url" -> s"jdbc:mysql://$host",
      "flyway.user" -> jdbcSettings.value.username,
      "flyway.password" -> jdbcSettings.value.password,
      "flyway.location" -> ("filesystem:" + file("sql").getCanonicalPath)
    ).map { case (key, value) =>
      s"-D$key=$value"
    },
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc" % scalikejdbc.ScalikejdbcBuildInfo.version,
      "com.typesafe.play" %% "play-json" % "2.9.2",
      "org.flywaydb" % "flyway-core" % "8.4.0" % "test",
      mysql
    )
  )

lazy val api = module("api")
  .enablePlugins(play.sbt.PlayScala)
  .settings(
    libraryDependencies ++= Seq(
      guice
    )
  )
  .dependsOn(domain)
