package io.github.seblm.scalatest.jgiven

import sbt.Keys._
import sbt._

object SbtJgivenScalatestReporterPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override lazy val projectSettings = Seq(
    libraryDependencies += "io.github.seblm" %% "jgiven-scalatest-reporter" % "0.5-scala212" % Test,
    Test / testOptions +=
      Tests.Argument(TestFrameworks.ScalaTest, "-C", "io.github.seblm.scalatest.jgiven.JGivenHtml5Reporter")
  )

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()

}
