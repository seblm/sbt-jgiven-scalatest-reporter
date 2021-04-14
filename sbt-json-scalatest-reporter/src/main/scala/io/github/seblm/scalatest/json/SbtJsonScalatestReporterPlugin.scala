package io.github.seblm.scalatest.json

import sbt.Keys._
import sbt._

object SbtJsonScalatestReporterPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override lazy val projectSettings = Seq(
    libraryDependencies += "io.github.seblm" %% "json-scalatest-reporter" % "0.5-SNAPSHOT" % Test,
    Test / testOptions +=
      Tests.Argument(TestFrameworks.ScalaTest, "-C", "io.github.seblm.scalatest.json.JsonReporter")
  )

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()

}
