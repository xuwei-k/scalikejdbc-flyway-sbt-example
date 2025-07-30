scalacOptions ++= Seq("-deprecation", "-unchecked")

addSbtPlugin("org.playframework" % "sbt-plugin" % "3.0.8")
enablePlugins(BuildInfoPlugin)

val mysqlDriverVersion = "9.4.0"

buildInfoKeys := Seq[BuildInfoKey](
  "mysqlDriverVersion" -> mysqlDriverVersion
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "4.3.4")
libraryDependencies += "com.mysql" % "mysql-connector-j" % mysqlDriverVersion

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.5")

libraryDependencySchemes += "org.scala-lang.modules" %% "scala-parser-combinators" % "always"
libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % "always"
