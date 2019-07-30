scalacOptions ++= Seq("-deprecation", "-unchecked")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.3")
enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "5.1.48"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.3.5")
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion

addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "1.6.0-RC4")
