package name.lemerdy.sebastian.scalatest.jgiven

import com.tngtech.jgiven.report.model.ExecutionStatus.SUCCESS
import name.lemerdy.sebastian.scalatest.jgiven.JGivenHtml5ReporterFixture._
import org.scalatest._

class JGivenHtml5ReporterWordSpec extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfter {

  info("As a test writer")
  info("I want my scalatest FeatureSpec being viewable into a browser")
  info("So anyone interested into the living documentation of my project can view it")

  feature("Generate json report") {
    scenario("Register a successful test") {
      val reporter: JGivenHtml5Reporter = given_sbt_jgiven_scalatest_reporter_is_installed(this)

      when_scalatest_is_executed(this, reporter, "TvSetWordSpec.json")

      val successfulScenario = reporter.reports(suiteId_).getScenariosWithStatus(SUCCESS).get(0)
      And("successful scenario should provide correct description")
      successfulScenario.getDescription should be("User presses power button when TV is off")
    }
  }

  after {
    JGivenHtml5ReporterFixture.removeFiles()
  }

  private val suiteId_ = "name.lemerdy.sebastian.scalatest.jgiven.TVSetWordSpec"

}
