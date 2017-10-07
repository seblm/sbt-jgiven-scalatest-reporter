import sbt._

object Dependencies {

  private val jGivenVersion = "0.15.1"

  lazy val `jgiven-core` = "com.tngtech.jgiven" % "jgiven-core" % jGivenVersion
  lazy val `jgiven-html5-report` = "com.tngtech.jgiven" % "jgiven-html5-report" % jGivenVersion
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.4"
  lazy val `slf4j-simple` = "org.slf4j" % "slf4j-simple" % "1.7.25"

}
