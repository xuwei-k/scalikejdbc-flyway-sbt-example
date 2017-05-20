scalacOptions ++= Seq("-deprecation", "-unchecked")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.15")

fullResolvers ~= {_.filterNot(_.name == "jcenter")}
