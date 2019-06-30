import sbt._

object Dependencies {

  private val jGivenVersion = "0.17.1"

  lazy val gson = "com.google.code.gson" % "gson" % "2.8.5"
  lazy val `jgiven-core` = "com.tngtech.jgiven" % "jgiven-core" % jGivenVersion
  lazy val `jgiven-html5-report` = "com.tngtech.jgiven" % "jgiven-html5-report" % jGivenVersion
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.8"
  lazy val `slf4j-api` = "org.slf4j" % "slf4j-api" % "1.7.26"

}
