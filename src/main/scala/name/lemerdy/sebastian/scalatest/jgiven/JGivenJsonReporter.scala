package name.lemerdy.sebastian.scalatest.jgiven

import java.nio.file.{Files, Paths}
import java.time.Instant

import com.tngtech.jgiven.report.html5.{Html5ReportConfig, Html5ReportGenerator}
import com.tngtech.jgiven.report.json.ScenarioJsonWriter
import com.tngtech.jgiven.report.model.ExecutionStatus.SUCCESS
import com.tngtech.jgiven.report.model.StepStatus.PASSED
import com.tngtech.jgiven.report.model._
import org.scalatest.ResourcefulReporter
import org.scalatest.events._

import scala.collection.Map

class JGivenJsonReporter extends ResourcefulReporter {

  private case class Report(model: ReportModel, testStartedAtTimestamp: Option[Instant])

  private var reports: Map[String, Report] = Map.empty[String, Report]

  override def dispose(): Unit = {
    val reportsDirectory = Paths.get("target", "jgiven-reports")

    val jsonReportsDirectory = reportsDirectory.resolve("json")
    Files.createDirectories(jsonReportsDirectory)
    reports.values.foreach { report =>
      val reportFile = jsonReportsDirectory.resolve(s"${report.model.getClassName}.json")
      new ScenarioJsonWriter(report.model).write(reportFile.toFile)
    }

    if (reports.nonEmpty) {
      val htmlReportDirectory = reportsDirectory.resolve("html")
      Files.createDirectories(htmlReportDirectory)
      val reportGenerator = new Html5ReportGenerator()
      val config = new Html5ReportConfig()
      config.setSourceDir(reportsDirectory.toFile)
      config.setTargetDir(htmlReportDirectory.toFile)
      reportGenerator.generateWithConfig(config)
    }
  }

  override def apply(event: Event): Unit = event match {
    case SuiteStarting(_, suiteName, suiteId, suiteClassName, _, _, _, _, _, _) =>
      val report = new ReportModel()
      report.setName(suiteName)
      report.setClassName(suiteClassName.getOrElse(suiteId))
      reports = reports + (suiteId -> Report(report, None))
    case InfoProvided(_, message, nameInfo, _, _, _, _, _, _) =>
      println(s"InfoProvided($message, ${nameInfo.get.suiteId}")
      nameInfo.map(_.suiteId)
        .flatMap(suiteId => reports.get(suiteId).map((suiteId, _)))
        .foreach { case (suiteId: String, report: Report) =>
          report.model.setDescription(Option(report.model.getDescription)
            .map(previousDescription => s"$previousDescription</br>$message")
            .getOrElse(message))
          reports = reports + (suiteId -> report)
        }
    case TestStarting(_, _, suiteId, _, _, _, _, _, _, _, _, timeStamp) =>
      reports.get(suiteId).foreach { report =>
        val reportWithTimeStamp = report.copy(testStartedAtTimestamp = Some(Instant.ofEpochMilli(timeStamp)))
        reports = reports + (suiteId -> reportWithTimeStamp)
      }
    case TestSucceeded(_, _, suiteId, _, _, testText, recordedEvents, _, _, _, _, _, _, timeStamp) =>
      reports.get(suiteId).foreach { report =>
        val scenario = new ScenarioModel()
        scenario.setDescription(testText.replaceFirst("^Scenario: ", ""))
        val scenarioCase = new ScenarioCaseModel
        scenarioCase.setStatus(SUCCESS)
        report.testStartedAtTimestamp.foreach { testStartedAt =>
          scenarioCase.setDurationInNanos(timeStamp - testStartedAt.toEpochMilli * 1000000)
        }
        recordedEvents.foreach {
          case InfoProvided(_, message, _, _, _, _, _, _, eventTimeStamp) =>
            val step = new StepModel()
            report.testStartedAtTimestamp.foreach { testStartedAt =>
              step.setDurationInNanos(eventTimeStamp - testStartedAt.toEpochMilli * 1000000)
            }
            step.setName(message)
            val words: Option[(String, String)] = message.substring(0, 5) match {
              case "Given" => Some(("Given", message.replaceFirst("^Given ", "")))
              case "When " => Some(("When", message.replaceFirst("^When ", "")))
              case "Then " => Some(("Then", message.replaceFirst("^Then ", "")))
              case _ => None
            }
            words.foreach { case (word: String, value: String) =>
              step.addIntroWord(new Word(word, true))
              step.addWords(new Word(value))
            }
            step.setStatus(PASSED)
            scenarioCase.addStep(step)
        }
        scenario.addCase(scenarioCase)
        report.model.addScenarioModel(scenario)
      }
    case otherEvent => println(s"${otherEvent.getClass.getName} is not handled")
  }

}
