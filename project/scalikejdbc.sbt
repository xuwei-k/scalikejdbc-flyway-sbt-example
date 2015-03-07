buildInfoSettings

sourceGenerators in Compile <+= buildInfo

val scaliekjdbcVersion = "2.2.4"
val mysqlDriverVersion = "5.1.34"

buildInfoKeys := Seq[BuildInfoKey](
  "scalikejdbcVersion" -> scaliekjdbcVersion,
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % scaliekjdbcVersion)
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion
