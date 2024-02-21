package io.github.seblm.scalatest.jgiven

import sbt.Keys.{libraryDependencies, testOptions}
import sbt._

object SbtJgivenScalatestReporterPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    libraryDependencies += "io.github.seblm" %% "jgiven-scalatest-reporter" % "1.0.3" % Test,
    Test / testOptions +=
      Tests.Argument(TestFrameworks.ScalaTest, "-C", "io.github.seblm.scalatest.jgiven.JGivenHtml5Reporter")
  )

}
