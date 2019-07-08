package name.lemerdy.sebastian.scalatest.jgiven

import sbt.Keys._
import sbt._

object SbtJgivenScalatestReporterPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override lazy val projectSettings = Seq(
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-C", classOf[JGivenHtml5Reporter].getName),
    libraryDependencies += Defaults
      .sbtPluginExtra("name.lemerdy.sebastian" % "sbt-jgiven-scalatest-reporter" % "0.1-SNAPSHOT", "1.0", "2.12") % Test
  )

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()

}
