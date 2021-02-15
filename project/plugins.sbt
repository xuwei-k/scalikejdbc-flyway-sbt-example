scalacOptions ++= Seq("-deprecation", "-unchecked")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.7")
enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "8.0.23"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.5.0")
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
