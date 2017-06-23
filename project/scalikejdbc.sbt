enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "5.1.42"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.0.1")
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion
