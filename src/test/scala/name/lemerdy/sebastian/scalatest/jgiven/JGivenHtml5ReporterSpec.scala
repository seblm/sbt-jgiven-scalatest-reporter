package name.lemerdy.sebastian.scalatest.jgiven

import java.nio.file.{Files, Paths}
import java.util
import java.util.Comparator.reverseOrder

import com.tngtech.jgiven.report.model.ExecutionStatus.SUCCESS
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonEventSupport
import org.scalatest._
import org.scalatest.events._

import scala.collection.JavaConverters._
import scala.io.Source

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

      When(
        s"reporter receives event suite starting with name TVSetSpec and suite id $suiteId_ and class name $suiteId_")
      applyEvents(reporter, eventsToApply)

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
        .asScala should contain only ("name.lemerdy.sebastian.scalatest.jgiven.TVSetFeature" -> JGivenHtml5Reporter
        .newReportTag("name.lemerdy.sebastian.scalatest.jgiven.TVSetFeature", "TVSetFeature"))
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
      And(s"successful scenario should be tagged")
      successfulScenario.getTagIds should contain only "name.lemerdy.sebastian.scalatest.jgiven.TVSetFeature"
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
      And(s"successful scenario should be tagged")
      failedScenario.getTagIds should contain only "name.lemerdy.sebastian.scalatest.jgiven.TVSetFeature"
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

  private def events: List[Event] =
    GsonEventSupport.gson
      .fromJson[util.Collection[Event]](
        Source.fromResource("name/lemerdy/sebastian/scalatest/jgiven/TvSetSpec.json").bufferedReader(),
        GsonEventSupport.collectionOfEventsType)
      .asScala
      .toList

}
