package name.lemerdy.sebastian.scalatest.jgiven

import java.nio.file.{Files, Paths}

import com.tngtech.jgiven.report.html5.{Html5ReportConfig, Html5ReportGenerator}
import com.tngtech.jgiven.report.json.ScenarioJsonWriter
import com.tngtech.jgiven.report.model.ExecutionStatus.{FAILED, SUCCESS}
import com.tngtech.jgiven.report.model._
import com.tngtech.jgiven.report.model.StepStatus.{PASSED, FAILED ⇒ STEP_FAILED}
import org.scalatest.ResourcefulReporter
import org.scalatest.events._

import scala.collection.Map
import scala.util.Try

class JGivenHtml5Reporter extends ResourcefulReporter {

  private[jgiven] var reports: Map[String, ReportModel] = Map.empty[String, ReportModel]

  override def dispose(): Unit = {
    val reportsWithScenarios = reports.values.filter(report ⇒ report.getScenarios.size() > 0)
    if (reportsWithScenarios.nonEmpty) {
      val reportsDirectory = Paths.get("target", "jgiven-reports")

      val jsonReportsDirectory = reportsDirectory.resolve("json")
      Files.createDirectories(jsonReportsDirectory)
      reportsWithScenarios.foreach { report ⇒
        val reportFile = jsonReportsDirectory.resolve(s"${report.getClassName}.json")
        new ScenarioJsonWriter(report).write(reportFile.toFile)
      }

      val htmlReportDirectory = reportsDirectory.resolve("html")
      Files.createDirectories(htmlReportDirectory)
      val reportGenerator = new Html5ReportGenerator()
      val config = new Html5ReportConfig()
      config.setSourceDir(reportsDirectory.toFile)
      config.setTargetDir(htmlReportDirectory.toFile)
      reportGenerator.generateWithConfig(config)
    }
  }

  override def apply(event: Event): Unit = {println(event); event match {
    case SuiteStarting(_, suiteName, suiteId, suiteClassName, _, _, _, _, _, _) ⇒
      val report = new ReportModel()
      report.setName(suiteName)
      report.setClassName(suiteClassName.getOrElse(suiteId))
      reports = reports + (suiteId -> report)
      ()
    case InfoProvided(_, message, nameInfo, _, _, _, _, _, _) ⇒
      nameInfo.map(_.suiteId)
        .flatMap(suiteId ⇒ reports.get(suiteId).map((suiteId, _)))
        .foreach { case (suiteId: String, report: ReportModel) ⇒
          report.setDescription(Option(report.getDescription)
            .map(previousDescription ⇒ s"$previousDescription</br>$message")
            .getOrElse(message))
          reports = reports + (suiteId -> report)
        }
      ()
    case TestFailed(_, _, _, suiteId, _, _, testText, recordedEvents, maybeThrowable, maybeDuration, _, _, _, _, _, _) ⇒
      reports.get(suiteId).foreach { report ⇒
        val scenario = new ScenarioModel()
        scenario.setDescription(testText.replaceFirst("^Scenario: ", ""))
        val scenarioCase = new ScenarioCaseModel()
        scenarioCase.setStatus(FAILED)
        maybeDuration.foreach(duration ⇒ scenarioCase.setDurationInNanos(duration * 1000000))
        scenario.addCase(scenarioCase)
        recordedEvents.foreach {
          case InfoProvided(_, message, _, _, _, _, _, _, _) ⇒
            val step = new StepModel()
            step.setName(message)
            val (maybeIntroWord, word): (Option[String], String) = message.substring(0, 5) match {
              case "Given" ⇒ (Some("Given"), message.replaceFirst("^Given ", ""))
              case "When " ⇒ (Some("When"), message.replaceFirst("^When ", ""))
              case "Then " ⇒ (Some("Then"), message.replaceFirst("^Then ", ""))
              case _ ⇒ (None, message)
            }
            maybeIntroWord.foreach(introWord ⇒ step.addIntroWord(new Word(introWord, true)))
            step.addWords(new Word(word))
            step.setStatus(PASSED)
            scenarioCase.addStep(step)
            ()
          case _: MarkupProvided ⇒
            ()
        }
        maybeThrowable.foreach { throwable ⇒
          val throwableStepModel = new StepModel()
          throwableStepModel.setName(throwable.toString)
          throwableStepModel.addWords(new Word(throwable.toString))
          scenarioCase.addStep(throwableStepModel)
        }
        Try(scenarioCase.getSteps.get(scenarioCase.getSteps.size - 1)).map(lastStep ⇒ lastStep.setStatus(STEP_FAILED))
        report.addScenarioModel(scenario)
      }
      ()
    case TestSucceeded(_, _, suiteId, _, _, testText, recordedEvents, maybeDuration, _, _, _, _, _, _) ⇒
      reports.get(suiteId).foreach { report ⇒
        val scenario = new ScenarioModel()
        scenario.setDescription(testText.replaceFirst("^Scenario: ", ""))
        val scenarioCase = new ScenarioCaseModel
        scenarioCase.setStatus(SUCCESS)
        maybeDuration.foreach(duration ⇒ scenarioCase.setDurationInNanos(duration * 1000000))
        recordedEvents.foreach {
          case InfoProvided(_, message, _, _, _, _, _, _, _) ⇒
            val step = new StepModel()
            step.setName(message)
            val (maybeIntroWord, word): (Option[String], String) = message.substring(0, 5) match {
              case "Given" ⇒ (Some("Given"), message.replaceFirst("^Given ", ""))
              case "When " ⇒ (Some("When"), message.replaceFirst("^When ", ""))
              case "Then " ⇒ (Some("Then"), message.replaceFirst("^Then ", ""))
              case _ ⇒ (None, message)
            }
            maybeIntroWord.foreach { introWord ⇒ step.addIntroWord(new Word(introWord, true)) }
            step.addWords(new Word(word))
            step.setStatus(PASSED)
            scenarioCase.addStep(step)
            ()
          case _: MarkupProvided ⇒
            ()
        }
        scenario.addCase(scenarioCase)
        report.addScenarioModel(scenario)
      }
      ()
    case _ ⇒
      ()
  }}

}
