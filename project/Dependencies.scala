import sbt.*

object Dependencies {

  private val jGivenVersion = "1.3.1"

  lazy val gson = "com.google.code.gson" % "gson" % "2.11.0"
  lazy val `jgiven-core` = "com.tngtech.jgiven" % "jgiven-core" % jGivenVersion
  lazy val `jgiven-html5-report` = "com.tngtech.jgiven" % "jgiven-html5-report" % jGivenVersion
  lazy val `log4j-slf4j-impl` = "org.apache.logging.log4j" % "log4j-slf4j2-impl" % "2.23.1" % Test
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.19"
  lazy val `slf4j-api` = "org.slf4j" % "slf4j-api" % "2.0.12"

}
