enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "5.1.44"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.1.0")
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion
