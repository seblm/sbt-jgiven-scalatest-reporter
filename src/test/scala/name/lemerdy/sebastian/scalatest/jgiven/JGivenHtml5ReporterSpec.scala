package name.lemerdy.sebastian.scalatest.jgiven

import java.nio.file.{Files, Paths}
import java.util.Comparator.reverseOrder

import org.scalatest._
import org.scalatest.events._

import scala.collection.JavaConverters._

class JGivenHtml5ReporterSpec extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfter {

  info("As a test writer")
  info("I want my scalatest FeatureSpec being viewable into a browser")
  info("So anyone interested into the living documentation of my project can view it")

  feature("Generate html5 report") {
    scenario("Test writer runs a simple scalatest suite") {
      Given("sbt-jgiven-scalatest-reporter is installed")
      val reporter = new JGivenHtml5Reporter()

      When("ScalaTest is executed")
      applyEvents(reporter, events)
      reporter.dispose()

      Then("a report is generated")
      Files.exists(Paths.get("target", "jgiven-reports", "html", "index.html")) should be(true)
    }
  }

  after {
    Files.walk(Paths.get("target", "jgiven-reports"))
      .sorted(reverseOrder())
      .iterator.asScala
      .map(_.toFile)
      .foreach(_.delete)
  }

  private def applyEvents(reporter: JGivenHtml5Reporter, events: Seq[Event]): JGivenHtml5Reporter = events match {
    case Nil =>
      reporter
    case event :: tail =>
      reporter.apply(event)
      applyEvents(reporter, tail)
  }

  private def events: Seq[Event] = {
    val suiteName = "TVSetSpec"
    val suiteId = "name.lemerdy.sebastian.scalatest.jgiven.TVSetSpec"
    val className = suiteId
    val suiteClassName = Some(suiteId)
    val fileName = "TVSetSpec.scala"
    val filePathname = Some("Please set the environment variable SCALACTIC_FILL_FILE_PATHNAMES to yes at compile time to enable this feature.")
    val nameInfo = NameInfo(suiteName, suiteId, suiteClassName, None)
    val ordinalRunStarting = new Ordinal(0)
    val ordinalSuiteStarting = ordinalRunStarting.next
    val ordinalInfoProvided1 = ordinalSuiteStarting.next
    val ordinalInfoProvided2 = ordinalInfoProvided1.next
    val ordinalInfoProvided3 = ordinalInfoProvided2.next
    val ordinalInfoProvided4 = ordinalInfoProvided3.next
    val scopeOpened = ordinalInfoProvided4.next
    val testStarting1 = scopeOpened.next
    val testSucceeded1 = testStarting1.next
    val infoProvided1 = testSucceeded1.next
    val infoProvided2 = infoProvided1.next
    val infoProvided3 = infoProvided2.next
    val scopeClosed = infoProvided3.next
    val suiteCompleted = scopeClosed.next
    val runCompleted = suiteCompleted.next

    Seq(
      RunStarting(ordinalRunStarting, 0, ConfigMap.empty, None, None, None, "pool-5-thread-2", 1507619835762L),
      SuiteStarting(ordinalSuiteStarting, suiteName, suiteId, suiteClassName, Some(IndentedText(s"$suiteName:", suiteName, 0)), Some(TopOfClass(className)), None, None, "pool-5-thread-2", 1507619835933L),
      InfoProvided(ordinalInfoProvided1, "As a TV set owner                          ".trim(), Some(nameInfo), None, Some(IndentedText("As a TV set owner                          ".trim(), "As a TV set owner                          ".trim(), 0)), Some(LineInFile(7, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835954L),
      InfoProvided(ordinalInfoProvided2, "I want to be able to turn the TV on and off".trim(), Some(nameInfo), None, Some(IndentedText("I want to be able to turn the TV on and off".trim(), "I want to be able to turn the TV on and off".trim(), 0)), Some(LineInFile(8, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835954L),
      InfoProvided(ordinalInfoProvided3, "So I can watch TV when I want              ".trim(), Some(nameInfo), None, Some(IndentedText("So I can watch TV when I want              ".trim(), "So I can watch TV when I want              ".trim(), 0)), Some(LineInFile(9, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835954L),
      InfoProvided(ordinalInfoProvided4, "And save energy when I'm not watching TV   ".trim(), Some(nameInfo), None, Some(IndentedText("And save energy when I'm not watching TV   ".trim(), "And save energy when I'm not watching TV   ".trim(), 0)), Some(LineInFile(10, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835954L),
      ScopeOpened(scopeOpened, "Feature: TV power button", nameInfo, Some(IndentedText("Feature: TV power button", "Feature: TV power button", 0)), Some(LineInFile(12, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835956L),
      TestStarting(testStarting1, suiteName, suiteId, suiteClassName, "Feature: TV power button Scenario: User presses power button when TV is off", "Scenario: User presses power button when TV is off", Some(MotionToSuppress), Some(LineInFile(13, fileName, filePathname)), suiteClassName, None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835961L),
      TestSucceeded(testSucceeded1, suiteName, suiteId, suiteClassName, "Feature: TV power button Scenario: User presses power button when TV is off", "Scenario: User presses power button when TV is off", Vector(
        InfoProvided(infoProvided1, "Given a TV set that is switched off".trim(), Some(nameInfo.copy(testName = Some("Feature: TV power button Scenario: User presses power button when TV is off"))), None, Some(IndentedText("----Given a TV set that is switched off".trim().replaceAll("-", ""), "Given a TV set that is switched off".trim(), 2)), Some(LineInFile(14, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835971L),
        InfoProvided(infoProvided2, "When the power button is pressed   ".trim(), Some(nameInfo.copy(testName = Some("Feature: TV power button Scenario: User presses power button when TV is off"))), None, Some(IndentedText("----When the power button is pressed   ".trim().replaceAll("-", ""), "When the power button is pressed   ".trim(), 2)), Some(LineInFile(18, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835971L),
        InfoProvided(infoProvided3, "Then the TV should switch on       ".trim(), Some(nameInfo.copy(testName = Some("Feature: TV power button Scenario: User presses power button when TV is off"))), None, Some(IndentedText("----Then the TV should switch on       ".trim().replaceAll("-", ""), "Then the TV should switch on       ".trim(), 2)), Some(LineInFile(21, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835971L),
      ), Some(10), Some(IndentedText("  Scenario: User presses power button when TV is off", "Scenario: User presses power button when TV is off", 1)), Some(LineInFile(13, fileName, filePathname)), suiteClassName, None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835972L),
      ScopeClosed(scopeClosed, "Feature: TV power button", nameInfo, Some(MotionToSuppress), Some(LineInFile(12, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835977L),
      SuiteCompleted(suiteCompleted, suiteName, suiteId, suiteClassName, Some(55), Some(MotionToSuppress), Some(TopOfClass(className)), None, None, "pool-5-thread-2", 1507619835980L),
      RunCompleted(runCompleted, Some(376), Some(Summary(1, 0, 0, 0, 0, 1, 0, 0)), None, None, None, "pool-5-thread-2", 1507619836126L),
    )
  }

}
