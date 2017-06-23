import build._
import org.flywaydb.sbt.FlywayPlugin

lazy val migration = module("migration").settings(
  flywaySchemas := databaseSchema.value :: Nil,
  flywayUrl := s"jdbc:mysql://$host",
  flywayUser := jdbcSettings.value.username,
  flywayLocations := {
    ("filesystem:" + ((resourceDirectory in Compile).value / "db/migration").getCanonicalPath) :: Nil
  },
  libraryDependencies ++= Seq(
    mysql
  )
).enablePlugins(FlywayPlugin)

lazy val domain = module("domain").settings(
  generatorSettings(Map("users" -> "User"), "example.domain.generated")
).settings(
  libraryDependencies ++= Seq(
    "org.scalikejdbc" %% "scalikejdbc" % scalikejdbc.ScalikejdbcBuildInfo.version,
    "com.typesafe.play" %% "play-json" % "2.6.2",
    mysql
  )
)

lazy val api = module("api").enablePlugins(play.sbt.PlayScala).settings(
  libraryDependencies ++= Seq(
    guice
  )
).dependsOn(domain)
