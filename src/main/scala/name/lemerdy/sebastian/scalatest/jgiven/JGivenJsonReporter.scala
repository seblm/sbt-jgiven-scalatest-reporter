package name.lemerdy.sebastian.scalatest.jgiven

import java.nio.charset.Charset
import java.nio.file.{Files, Paths}
import java.time.Instant

import com.tngtech.jgiven.report.model.ExecutionStatus.SUCCESS
import com.tngtech.jgiven.report.model.StepStatus.PASSED
import com.tngtech.jgiven.report.model._
import org.scalatest.ResourcefulReporter
import org.scalatest.events._

import scala.collection.Map
import scala.collection.JavaConverters._

class JGivenJsonReporter extends ResourcefulReporter {

  private case class Report(model: ReportModel, testStartedAtTimestamp: Option[Instant])

  private var reports: Map[String, Report] = Map.empty[String, Report]

  override def dispose(): Unit =
    reports.values.foreach { report =>
      val directory = Paths.get("target", "jgiven-reports")
      Files.createDirectories(directory)
      Files.write(
        directory.resolve(s"${report.model.getClassName}.json"),
        reportModelToJsonString(report.model).getBytes(Charset.forName("UTF-8"))
      )
    }

  private def reportModelToJsonString(reportModel: ReportModel): String =
    s"""{
       |  "className": "${reportModel.getClassName}",
       |  "name": "${reportModel.getName}",
       |  "description": "${reportModel.getDescription}",
       |  "scenarios": [${reportModel.getScenarios.asScala.map(scenarioModelToJsonString).mkString(",\n")}]
       |}""".stripMargin

  private def scenarioModelToJsonString(scenarioModel: ScenarioModel): String =
    s"""{
       |  "description": "${scenarioModel.getDescription}",
       |  "casesAsTable": false,
       |  "scenarioCases": [${scenarioModel.getScenarioCases.asScala.map(scenarioCaseModelToJsonString).mkString(",\n")}]
       |}""".stripMargin

  private def scenarioCaseModelToJsonString(scenarioCaseModel: ScenarioCaseModel): String =
    s"""{
       |  "caseNr": ${scenarioCaseModel.getCaseNr},
       |  "steps": [${scenarioCaseModel.getSteps.asScala.map(stepModelToJsonString).mkString(",\n")}],
       |  "status": "${scenarioCaseModel.getExecutionStatus}",
       |  "durationInNanos": ${scenarioCaseModel.getDurationInNanos}
       |}""".stripMargin

  private def stepModelToJsonString(stepModel: StepModel): String =
    s"""{
       |  "name": "${stepModel.getName}",
       |  "words": [${stepModel.getWords.asScala.map(wordToJsonString).mkString(",\n")}],
       |  "status": "${stepModel.getStatus}",
       |  "durationInNanos": "${stepModel.getDurationInNanos}"
       |}""".stripMargin

  private def wordToJsonString(word: Word): String =
    s"""{
       |  "value": "${word.getValue}"${if (word.isIntroWord) ",\n  \"isIntroWord\": true" else ""}
       |}""".stripMargin

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
            .map(previousDescription => s"$previousDescription\\u003c/br\\u003e$message")
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
