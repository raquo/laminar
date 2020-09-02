logLevel := Level.Warn

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.0.1")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.0.0"

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.8")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.8.1")
