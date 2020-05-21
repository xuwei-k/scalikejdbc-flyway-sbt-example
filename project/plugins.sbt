scalacOptions ++= Seq("-deprecation", "-unchecked")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.2")
enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "5.1.49"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.4.2")
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion

addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "1.6.0-RC4")
