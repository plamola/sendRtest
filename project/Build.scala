import sbt._
import sbt.Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "sendRtest"
  val appVersion      = "1.0-beta1"

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
    "org.webjars" % "requirejs" % "2.1.10" exclude("org.webjars", "jquery"),
    "net.sf.opencsv" % "opencsv" % "2.3"


  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "webjars" at "http://webjars.github.com/m2"
  )

}
