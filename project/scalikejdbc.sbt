enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "5.1.40"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "2.5.0")
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion
