enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "5.1.41"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.0.0-RC4")
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion
