import sbt._

object Dependencies {

  private val jGivenVersion = "0.17.1"

  lazy val `jgiven-core` = "com.tngtech.jgiven" % "jgiven-core" % jGivenVersion
  lazy val `jgiven-html5-report` = "com.tngtech.jgiven" % "jgiven-html5-report" % jGivenVersion
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.8"

}
