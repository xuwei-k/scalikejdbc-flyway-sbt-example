scalacOptions ++= Seq("-deprecation", "-unchecked")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.19")
enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "8.1.0"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "4.0.0")
libraryDependencies += "com.mysql" % "mysql-connector-j" % mysqlDriverVersion

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")

libraryDependencySchemes += "org.scala-lang.modules" %% "scala-parser-combinators" % "always"
libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % "always"
