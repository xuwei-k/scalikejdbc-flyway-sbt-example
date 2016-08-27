import build._
import org.flywaydb.sbt.FlywayPlugin
import play.sbt.routes.RoutesKeys.routesGenerator
import play.routes.compiler.StaticRoutesGenerator

lazy val migration = module("migration").settings(
  flywaySchemas := databaseSchema.value :: Nil,
  flywayUrl := s"jdbc:mysql://$host",
  flywayUser := jdbcSettings.value.username,
  libraryDependencies ++= Seq(
    mysql
  )
).enablePlugins(FlywayPlugin)

lazy val domain = module("domain").settings(
  generatorSettings(Map("users" -> "User"), "example.domain.generated")
).settings(
  libraryDependencies ++= Seq(
    "org.scalikejdbc" %% "scalikejdbc" % scalikejdbc.ScalikejdbcBuildInfo.version,
    "com.typesafe.play" %% "play-json" % play.core.PlayVersion.current,
    mysql
  )
)

lazy val api = module("api").enablePlugins(play.sbt.PlayScala).settings(
  routesGenerator := StaticRoutesGenerator,
  libraryDependencies ++= Seq(
  )
).dependsOn(domain)
