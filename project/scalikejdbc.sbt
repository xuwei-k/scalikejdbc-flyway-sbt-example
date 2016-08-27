buildInfoSettings

sourceGenerators in Compile <+= buildInfo

val mysqlDriverVersion = "5.1.38"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "2.4.2")
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion
