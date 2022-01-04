import sbt._

object Dependencies {

  private val jGivenVersion = "1.1.0"

  lazy val gson = "com.google.code.gson" % "gson" % "2.8.9"
  lazy val `jgiven-core` = "com.tngtech.jgiven" % "jgiven-core" % jGivenVersion
  lazy val `jgiven-html5-report` = "com.tngtech.jgiven" % "jgiven-html5-report" % jGivenVersion
  lazy val `log4j-slf4j-impl` = "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.17.1" % Test
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.10"
  lazy val `slf4j-api` = "org.slf4j" % "slf4j-api" % "1.7.32"

}
