scalacOptions ++= Seq("-deprecation", "-unchecked")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.8")
enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "8.0.27"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.5.0")
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.3")
