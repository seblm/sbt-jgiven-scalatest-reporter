package io.github.seblm.scalatest.jgiven

import com.tngtech.jgiven.report.model.ExecutionStatus.SUCCESS
import io.github.seblm.scalatest.jgiven.JGivenHtml5ReporterFixture._
import org.scalatest.OptionValues.convertOptionToValuable
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfter, GivenWhenThen}

import java.nio.file.{Files, Paths}
import scala.collection.JavaConverters._
import scala.concurrent.duration._

class JGivenHtml5ReporterSpec extends AnyFeatureSpec with GivenWhenThen with Matchers with BeforeAndAfter {

  private val suiteId_ = "io.github.seblm.scalatest.jgiven.TVSetSpec"

  private val suiteClassName: Option[String] = Some(suiteId_)

  info("As a test writer")
  info("I want my scalatest FeatureSpec being viewable into a browser")
  info("So anyone interested into the living documentation of my project can view it")

  Feature("Generate html5 report") {
    Scenario("Test writer runs a simple scalatest suite") {
      val reporter: JGivenHtml5Reporter = given_sbt_jgiven_scalatest_reporter_is_installed(this)

      when_scalatest_is_executed(this, reporter, "TvSetSpec.json")

      Then("a report is generated")
      Files.exists(Paths.get("target", "jgiven-reports", "html", "index.html")) should be(true)
    }
  }

  Feature("Generate json report") {
    Scenario("Create a new report when a ScalaTest suite is starting") {
      val reporter: JGivenHtml5Reporter = given_sbt_jgiven_scalatest_reporter_is_installed(this)

      when_scalatest_is_executed(this, reporter, "TvSetSpec.json")

      Then(s"reports contains key $suiteId_")
      reporter.reports should contain key suiteId_
      And(s"report identified by key $suiteId_ have name TVSetSpec")
      reporter.reports(suiteId_).getName should be("TVSetSpec")
      And(s"report identified by key $suiteId_ have class name $suiteId_")
      reporter.reports(suiteId_).getClassName should be(suiteId_)
      And(s"report identified by key $suiteId_ declares tag TVSetFeature")
      reporter
        .reports(suiteId_)
        .getTagMap
        .asScala should contain only ("io.github.seblm.scalatest.jgiven.TVSetFeature" -> JGivenHtml5Reporter
        .newReportTag("io.github.seblm.scalatest.jgiven.TVSetFeature", "TVSetFeature"))
    }

    Scenario("Register a successful test") {
      val reporter: JGivenHtml5Reporter = given_sbt_jgiven_scalatest_reporter_is_installed(this)

      when_scalatest_is_executed(this, reporter, "TvSetSpec.json")

      val successfulScenarios = reporter.reports(suiteId_).getScenariosWithStatus(SUCCESS)
      Then(s"report identified by key $suiteId_ should have successful scenario")
      successfulScenarios should have size 1
      val successfulScenario = successfulScenarios.get(0)
      And(s"successful scenario class name should be ${suiteClassName.value}")
      successfulScenario.getClassName should be(suiteClassName.value)
      And("successful scenario should be tagged")
      successfulScenario.getTagIds should contain only "io.github.seblm.scalatest.jgiven.TVSetFeature"
      And("successful scenario should report duration")
      successfulScenario.getDurationInNanos should be(29.milliseconds.toNanos)
    }

    Scenario("Register a failed test") {
      val reporter: JGivenHtml5Reporter = given_sbt_jgiven_scalatest_reporter_is_installed(this)

      when_scalatest_is_executed(this, reporter, "TvSetSpec.json")

      val failedScenarios = reporter.reports(suiteId_).getFailedScenarios
      Then(s"report identified by key $suiteId_ should have failed scenario")
      failedScenarios should have size 1
      val failedScenario = failedScenarios.get(0)
      And(s"failed scenario class name should be ${suiteClassName.value}")
      failedScenario.getClassName should be(suiteClassName.value)
      And("failed scenario should be tagged")
      failedScenario.getTagIds should contain only "io.github.seblm.scalatest.jgiven.TVSetFeature"
      And("failed scenario should report duration")
      failedScenario.getDurationInNanos should be(20.milliseconds.toNanos)
    }
  }

  after {
    JGivenHtml5ReporterFixture.removeFiles()
  }

}
