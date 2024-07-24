scalacOptions ++= Seq("-deprecation", "-unchecked")

addSbtPlugin("org.playframework" % "sbt-plugin" % "3.0.5")
enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "9.0.0"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "4.3.1")
libraryDependencies += "com.mysql" % "mysql-connector-j" % mysqlDriverVersion

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")

libraryDependencySchemes += "org.scala-lang.modules" %% "scala-parser-combinators" % "always"
libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % "always"
