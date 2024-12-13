import sbt.*

object Dependencies {

  private val jGivenVersion = "2.0.0"

  lazy val `commons-io` =
    "commons-io" % "commons-io" % "2.18.0" % Runtime // Please remove this dependency once jgiven-html5-report will transitively depend on it
  lazy val gson = "com.google.code.gson" % "gson" % "2.11.0"
  lazy val `jgiven-core` = "com.tngtech.jgiven" % "jgiven-core" % jGivenVersion
  lazy val `jgiven-html5-report` =
    "com.tngtech.jgiven" % "jgiven-html5-report" % jGivenVersion exclude ("commons-io", "commons-io")
  lazy val `log4j-slf4j-impl` = "org.apache.logging.log4j" % "log4j-slf4j2-impl" % "2.24.3" % Test
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.19"
  lazy val `slf4j-api` = "org.slf4j" % "slf4j-api" % "2.0.16"

}
