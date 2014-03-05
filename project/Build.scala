import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "sendR"
  val appVersion      = "1.1-beta6"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.webjars" %% "webjars-play" % "2.2.1" exclude("org.webjars", "jquery"),
    "org.webjars" % "jquery" % "1.10.1",                                   // AngularJS can't handle jQuery 2.x
    "org.webjars" % "bootstrap" % "3.0.3" exclude("org.webjars", "jquery"),
    "org.webjars" % "angularjs" % "1.2.9" exclude("org.webjars", "jquery"),
    "org.webjars" % "requirejs-domready" % "2.0.1" exclude("org.webjars", "jquery"),
    "org.webjars" % "requirejs" % "2.1.10" exclude("org.webjars", "jquery")
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "webjars" at "http://webjars.github.com/m2"
  )

}
