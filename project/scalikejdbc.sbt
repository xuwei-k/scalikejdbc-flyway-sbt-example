buildInfoSettings

sourceGenerators in Compile <+= buildInfo

val mysqlDriverVersion = "5.1.34"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "2.2.5")
libraryDependencies += "mysql" % "mysql-connector-java" % mysqlDriverVersion
