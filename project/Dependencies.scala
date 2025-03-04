import sbt.*

object Dependencies {

  private val jGivenVersion = "2.0.1"
  private val sbtVersion = "1.10.9"
  private val scalatestVersion = "3.2.19"

  lazy val gson = "com.google.code.gson" % "gson" % "2.12.1"
  lazy val `jgiven-core` = "com.tngtech.jgiven" % "jgiven-core" % jGivenVersion
  lazy val `jgiven-html5-report` = "com.tngtech.jgiven" % "jgiven-html5-report" % jGivenVersion exclude ("commons-io", "commons-io")
  lazy val `log4j-slf4j-impl` = "org.apache.logging.log4j" % "log4j-slf4j2-impl" % "2.24.3" % Test
  lazy val `scalatest-core` = "org.scalatest" %% "scalatest-core" % scalatestVersion
  lazy val `scalatest-featurespec` = "org.scalatest" %% "scalatest-featurespec" % scalatestVersion
  lazy val `scalatest-flatspec` = "org.scalatest" %% "scalatest-flatspec" % scalatestVersion % Test
  lazy val `scalatest-shouldmatchers` = "org.scalatest" %% "scalatest-shouldmatchers" % scalatestVersion % Test
  lazy val `scalatest-wordspec` = "org.scalatest" %% "scalatest-wordspec" % scalatestVersion % Test
  lazy val `sbt-` = "org.scala-sbt" % "sbt" % sbtVersion // append dash to avoid error: <expected-type>:1: error: value internal is not a member of sbt.librarymanagement.ModuleID sbt.internal.DslEntry
  lazy val `sbt-actions` = "org.scala-sbt" %% "actions" % sbtVersion
  lazy val `sbt-collections` = "org.scala-sbt" %% "collections" % sbtVersion
  lazy val `sbt-core-macros` = "org.scala-sbt" %% "core-macros" % sbtVersion
  lazy val `sbt-librarymanagement-core` = "org.scala-sbt" %% "librarymanagement-core" % "1.10.4"
  lazy val `sbt-main` = "org.scala-sbt" %% "main" % sbtVersion
  lazy val `sbt-main-settings` = "org.scala-sbt" %% "main-settings" % sbtVersion
  lazy val `sbt-task-system` = "org.scala-sbt" %% "task-system" % sbtVersion
  lazy val `sbt-testing` = "org.scala-sbt" %% "testing" % sbtVersion
  lazy val `sbt-util-position` = "org.scala-sbt" %% "util-position" % sbtVersion
  lazy val `slf4j-api` = "org.slf4j" % "slf4j-api" % "2.0.17"

}
