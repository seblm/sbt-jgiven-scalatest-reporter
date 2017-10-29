package name.lemerdy.sebastian.scalatest.jgiven

import java.nio.file.{Files, Paths}
import java.util.Comparator.reverseOrder

import com.tngtech.jgiven.report.model.ExecutionStatus.SUCCESS
import org.scalatest._
import org.scalatest.events._
import org.scalatest.exceptions.TestFailedException

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

  feature("Generate json report") {
    scenario("Create a new report when a ScalaTest suite is starting") {
      Given("JGiven html5 reporter")
      val reporter = new JGivenHtml5Reporter()
      val eventsToApply = events.take(2)

      When(s"reporter receives event suite starting with name TVSetSpec and suite id $suiteId_ and class name $suiteId_")
      applyEvents(reporter, eventsToApply)

      Then(s"reports contains key $suiteId_")
      reporter.reports should contain key suiteId_
      And(s"report identified by key $suiteId_ have name TVSetSpec")
      reporter.reports(suiteId_).getName should be("TVSetSpec")
      And(s"report identified by key $suiteId_ have class name $suiteId_")
      reporter.reports(suiteId_).getClassName should be(suiteId_)
    }

    scenario("Register a successful test") {
      Given("JGiven html5 reporter")
      val reporter = new JGivenHtml5Reporter()

      When(s"reporter receives event test succeeded")
      applyEvents(reporter, events)

      val successfulScenarios = reporter.reports(suiteId_).getScenariosWithStatus(SUCCESS)
      Then(s"report identified by key $suiteId_ should have successful scenario")
      successfulScenarios should have size 1
      val successfulScenario = successfulScenarios.get(0)
      And(s"successful scenario class name should be ${suiteClassName.get}")
      successfulScenario.getClassName should be(suiteClassName.get)
    }

    scenario("Register a failed test") {
      Given("JGiven html5 reporter")
      val reporter = new JGivenHtml5Reporter()

      When(s"reporter receives event test failed")
      applyEvents(reporter, events)

      val failedScenarios = reporter.reports(suiteId_).getFailedScenarios
      Then(s"report identified by key $suiteId_ should have failed scenario")
      failedScenarios should have size 1
      val failedScenario = failedScenarios.get(0)
      And(s"failed scenario class name should be ${suiteClassName.get}")
      failedScenario.getClassName should be(suiteClassName.get)
    }
  }

  after {
    val jGivenReports = Paths.get("target", "jgiven-reports")
    if (jGivenReports.toFile.exists()) {
      Files.walk(jGivenReports).sorted(reverseOrder()).iterator.asScala.map(_.toFile).foreach(_.delete)
    }
  }

  private def applyEvents(reporter: JGivenHtml5Reporter, events: Seq[Event]): JGivenHtml5Reporter = events match {
    case Nil ⇒
      reporter
    case event :: tail ⇒
      reporter.apply(event)
      applyEvents(reporter, tail)
  }

  private val suiteId_ = "name.lemerdy.sebastian.scalatest.jgiven.TVSetSpec"

  private val suiteClassName = Some(suiteId_)

  private def events: Seq[Event] = {
    val suiteName = "TVSetSpec"
    val className = suiteId_
    val fileName = "TVSetSpec.scala"
    val filePathname = Some("Please set the environment variable SCALACTIC_FILL_FILE_PATHNAMES to yes at compile time to enable this feature.")
    val nameInfo = NameInfo(suiteName, suiteId_, suiteClassName, None)
    val ordinalRunStarting = new Ordinal(0)
    val ordinalSuiteStarting = ordinalRunStarting.next
    val ordinalInfoProvided1 = ordinalSuiteStarting.next
    val ordinalInfoProvided2 = ordinalInfoProvided1.next
    val ordinalInfoProvided3 = ordinalInfoProvided2.next
    val ordinalInfoProvided4 = ordinalInfoProvided3.next
    val scopeOpened = ordinalInfoProvided4.next
    val testStarting1 = scopeOpened.next
    val infoProvided1 = testStarting1.next
    val infoProvided2 = infoProvided1.next
    val infoProvided3 = infoProvided2.next
    val testSucceeded1 = infoProvided3.next
    val testStarting2 = testSucceeded1.next
    val infoProvided4 = testStarting2.next
    val infoProvided5 = infoProvided4.next
    val infoProvided6 = infoProvided5.next
    val testFailed = infoProvided6.next
    val scopeClosed = testFailed.next
    val suiteCompleted = scopeClosed.next
    val runCompleted = suiteCompleted.next

    Seq(
      RunStarting(ordinalRunStarting, 0, ConfigMap.empty, None, None, None, "pool-5-thread-2", 1507619835762L),
      SuiteStarting(ordinalSuiteStarting, suiteName, suiteId_, suiteClassName, Some(IndentedText(s"$suiteName:", suiteName, 0)), Some(TopOfClass(className)), None, None, "pool-5-thread-2", 1507619835933L),
      InfoProvided(ordinalInfoProvided1, "As a TV set owner                          ".trim(), Some(nameInfo), None, Some(IndentedText("As a TV set owner                          ".trim(), "As a TV set owner                          ".trim(), 0)), Some(LineInFile(7, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835954L),
      InfoProvided(ordinalInfoProvided2, "I want to be able to turn the TV on and off".trim(), Some(nameInfo), None, Some(IndentedText("I want to be able to turn the TV on and off".trim(), "I want to be able to turn the TV on and off".trim(), 0)), Some(LineInFile(8, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835954L),
      InfoProvided(ordinalInfoProvided3, "So I can watch TV when I want              ".trim(), Some(nameInfo), None, Some(IndentedText("So I can watch TV when I want              ".trim(), "So I can watch TV when I want              ".trim(), 0)), Some(LineInFile(9, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835954L),
      InfoProvided(ordinalInfoProvided4, "And save energy when I'm not watching TV   ".trim(), Some(nameInfo), None, Some(IndentedText("And save energy when I'm not watching TV   ".trim(), "And save energy when I'm not watching TV   ".trim(), 0)), Some(LineInFile(10, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835954L),
      ScopeOpened(scopeOpened, "Feature: TV power button", nameInfo, Some(IndentedText("Feature: TV power button", "Feature: TV power button", 0)), Some(LineInFile(12, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835956L),
      TestStarting(testStarting1, suiteName, suiteId_, suiteClassName, "Feature: TV power button Scenario: User presses power button when TV is off", "Scenario: User presses power button when TV is off", Some(MotionToSuppress), Some(LineInFile(13, fileName, filePathname)), suiteClassName, None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835961L),
      TestSucceeded(testSucceeded1, suiteName, suiteId_, suiteClassName, "Feature: TV power button Scenario: User presses power button when TV is off", "Scenario: User presses power button when TV is off", Vector(
        InfoProvided(infoProvided1, "Given a TV set that is switched off".trim(), Some(nameInfo.copy(testName = Some("Feature: TV power button Scenario: User presses power button when TV is off"))), None, Some(IndentedText("----Given a TV set that is switched off".trim().replaceAll("-", ""), "Given a TV set that is switched off".trim(), 2)), Some(LineInFile(14, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835971L),
        InfoProvided(infoProvided2, "When the power button is pressed   ".trim(), Some(nameInfo.copy(testName = Some("Feature: TV power button Scenario: User presses power button when TV is off"))), None, Some(IndentedText("----When the power button is pressed   ".trim().replaceAll("-", ""), "When the power button is pressed   ".trim(), 2)), Some(LineInFile(18, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835971L),
        InfoProvided(infoProvided3, "Then the TV should switch on       ".trim(), Some(nameInfo.copy(testName = Some("Feature: TV power button Scenario: User presses power button when TV is off"))), None, Some(IndentedText("----Then the TV should switch on       ".trim().replaceAll("-", ""), "Then the TV should switch on       ".trim(), 2)), Some(LineInFile(21, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835971L),
      ), Some(10), Some(IndentedText("  Scenario: User presses power button when TV is off", "Scenario: User presses power button when TV is off", 1)), Some(LineInFile(13, fileName, filePathname)), suiteClassName, None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835972L),
      TestStarting(testStarting2, suiteName, suiteId_, suiteClassName, "Feature: TV power button Scenario: User presses power button when TV is on", "Scenario: User presses power button when TV is on", Some(MotionToSuppress), Some(LineInFile(25, fileName, filePathname)), suiteClassName, None, "pool-7-thread-10-ScalaTest-running-TVSetSpec", 1508325703748L),
      TestFailed(testFailed, "name.lemerdy.sebastian.scalatest.jgiven.TVSet@243ece77 was not isOn", suiteName, suiteId_, suiteClassName, "Feature: TV power button Scenario: User presses power button when TV is on", "Scenario: User presses power button when TV is on", Vector(
        InfoProvided(infoProvided4, "Given a TV set that is switched on".trim(), Some(nameInfo.copy(testName = Some("Feature: TV power button Scenario: User presses power button when TV is on"))), None, Some(IndentedText("----Given a TV set that is switched on".trim().replaceAll("-", ""), "Given a TV set that is switched on".trim(), 2)), Some(LineInFile(27, fileName, filePathname)), None, "pool-7-thread-10-ScalaTest-running-TVSetSpec", 1508325703753L),
        InfoProvided(infoProvided5, "When the power button is pressed  ".trim(), Some(nameInfo.copy(testName = Some("Feature: TV power button Scenario: User presses power button when TV is on"))), None, Some(IndentedText("----When the power button is pressed  ".trim().replaceAll("-", ""), "When the power button is pressed  ".trim(), 2)), Some(LineInFile(32, fileName, filePathname)), None, "pool-7-thread-10-ScalaTest-running-TVSetSpec", 1508325703753L),
        InfoProvided(infoProvided6, "Then the TV should switch off     ".trim(), Some(nameInfo.copy(testName = Some("Feature: TV power button Scenario: User presses power button when TV is on"))), None, Some(IndentedText("----Then the TV should switch off     ".trim().replaceAll("-", ""), "Then the TV should switch off     ".trim(), 2)), Some(LineInFile(35, fileName, filePathname)), None, "pool-7-thread-10-ScalaTest-running-TVSetSpec", 1508325703753L)
      ), Some(new TestFailedException("name.lemerdy.sebastian.scalatest.jgiven.TVSet@243ece77 was not isOn", 0)), Some(5L), Some(IndentedText("  Scenario: User presses power button when TV is on", "Scenario: User presses power button when TV is on", 1)), Some(SeeStackDepthException), suiteClassName, None, "pool-7-thread-10-ScalaTest-running-TVSetSpec", 1508325703755L),
      ScopeClosed(scopeClosed, "Feature: TV power button", nameInfo, Some(MotionToSuppress), Some(LineInFile(12, fileName, filePathname)), None, "pool-5-thread-2-ScalaTest-running-TVSetSpec", 1507619835977L),
      SuiteCompleted(suiteCompleted, suiteName, suiteId_, suiteClassName, Some(55), Some(MotionToSuppress), Some(TopOfClass(className)), None, None, "pool-5-thread-2", 1507619835980L),
      RunCompleted(runCompleted, Some(376), Some(Summary(1, 0, 0, 0, 0, 1, 0, 0)), None, None, None, "pool-5-thread-2", 1507619836126L),
    )
  }

}
