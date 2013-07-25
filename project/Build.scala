import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "csvtows"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.webjars" % "webjars-play" % "2.0",
    "org.webjars" % "bootstrap" % "2.1.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "webjars" at "http://webjars.github.com/m2"
  )

}