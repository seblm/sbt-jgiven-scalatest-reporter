package fr.fpe.scalatest.jgiven

import java.lang.annotation.Annotation
import java.nio.file.{Files, Paths}
import java.util

import com.tngtech.jgiven.report.html5.{Html5ReportConfig, Html5ReportGenerator}
import com.tngtech.jgiven.report.json.ScenarioJsonWriter
import com.tngtech.jgiven.report.model.ExecutionStatus.{FAILED, SUCCESS}
import com.tngtech.jgiven.report.model.StepStatus.{PASSED, FAILED => STEP_FAILED}
import com.tngtech.jgiven.report.model._
import org.scalatest.events._
import org.scalatest.{FeatureSpecLike, ResourcefulReporter}

import scala.collection.JavaConverters._
import scala.collection.Map
import scala.concurrent.duration._
import scala.util.Try

class JGivenHtml5Reporter extends ResourcefulReporter {

  private val jsonReporter = new JsonReporter()

  private[jgiven] var reports: Map[String, ReportModel] = Map.empty[String, ReportModel]

  private var currentSpecClass: Option[Class[_]] = None

  private var currentSpecInstance: Option[Any] = None

  override def dispose(): Unit = {
    jsonReporter.dispose()
    val reportsWithScenarios = reports.values.filter(report => report.getScenarios.size() > 0)
    if (reportsWithScenarios.nonEmpty) {
      val reportsDirectory = Paths.get("target", "jgiven-reports")

      val jsonReportsDirectory = reportsDirectory.resolve("json")
      Files.createDirectories(jsonReportsDirectory)
      reportsWithScenarios.foreach { report =>
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

  override def apply(event: Event): Unit = {
    jsonReporter.apply(event)
    event match {
      case SuiteStarting(_, suiteName, suiteId, suiteClassName, _, _, _, _, _, _) =>
        val report = new ReportModel()
        report.setName(suiteName)
        report.setClassName(suiteClassName.getOrElse(suiteId))
        currentSpecClass = suiteClassName.flatMap(loadSuiteClass)
        currentSpecInstance = currentSpecClass.flatMap(instantiateSuiteClass)
        currentSpecClass.map(findTagIds).map(_.asJava).foreach(report.addTags)
        reports = reports + (suiteId -> report)
        ()
      case InfoProvided(_, message, nameInfo, _, _, _, _, _, _) =>
        nameInfo
          .map(_.suiteId)
          .flatMap(suiteId => reports.get(suiteId).map((suiteId, _)))
          .foreach { case (suiteId: String, report: ReportModel) =>
            report.setDescription(
              Option(report.getDescription)
                .map(previousDescription => s"$previousDescription</br>$message")
                .getOrElse(message)
            )
            reports = reports + (suiteId -> report)
          }
        ()
      case TestFailed(
            _,
            _,
            _,
            suiteId,
            suiteClassName,
            testName,
            testText,
            recordedEvents,
            maybeThrowable,
            maybeDuration,
            _,
            _,
            _,
            _,
            _,
            _
          ) =>
        reports.get(suiteId).foreach { report =>
          val scenario = new ScenarioModel()
          scenario.setClassName(suiteClassName.getOrElse(suiteId))
          scenario.setDescription(currentSpecInstance match {
            case Some(currentSpec) if currentSpec.isInstanceOf[FeatureSpecLike] =>
              testText.replaceFirst("^Scenario: ", "")
            case _ => testName
          })
          scenario.addTags(new util.ArrayList(report.getTagMap.values()))
          maybeDuration.foreach(duration => scenario.setDurationInNanos(duration.milliseconds.toNanos))
          val scenarioCase = new ScenarioCaseModel()
          scenarioCase.setStatus(FAILED)
          scenario.addCase(scenarioCase)
          recordedEvents.foreach {
            case InfoProvided(_, message, _, _, _, _, _, _, _) =>
              val step = new StepModel()
              step.setName(message)
              val (maybeIntroWord, word): (Option[String], String) = message.substring(0, 5) match {
                case "Given" => (Some("Given"), message.replaceFirst("^Given ", ""))
                case "When " => (Some("When"), message.replaceFirst("^When ", ""))
                case "Then " => (Some("Then"), message.replaceFirst("^Then ", ""))
                case _       => (None, message)
              }
              maybeIntroWord.foreach(introWord => step.addIntroWord(new Word(introWord, true)))
              step.addWords(new Word(word))
              step.setStatus(PASSED)
              scenarioCase.addStep(step)
              ()
            case _: MarkupProvided =>
              ()
          }
          maybeThrowable.foreach { throwable =>
            val throwableStepModel = new StepModel()
            throwableStepModel.setName(throwable.toString)
            throwableStepModel.addWords(new Word(throwable.toString))
            scenarioCase.addStep(throwableStepModel)
          }
          Try(scenarioCase.getSteps.get(scenarioCase.getSteps.size - 1))
            .map(lastStep => lastStep.setStatus(STEP_FAILED))
          report.addScenarioModel(scenario)
        }
        ()
      case TestSucceeded(
            _,
            _,
            suiteId,
            suiteClassName,
            testName,
            testText,
            recordedEvents,
            maybeDuration,
            _,
            _,
            _,
            _,
            _,
            _
          ) =>
        reports.get(suiteId).foreach { report =>
          val scenario = new ScenarioModel()
          scenario.setClassName(suiteClassName.getOrElse(suiteId))
          scenario.setDescription(currentSpecInstance match {
            case Some(currentSpec) if currentSpec.isInstanceOf[FeatureSpecLike] =>
              testText.replaceFirst("^Scenario: ", "")
            case _ => testName
          })
          scenario.addTags(new util.ArrayList(report.getTagMap.values()))
          maybeDuration.foreach(duration => scenario.setDurationInNanos(duration.milliseconds.toNanos))
          val scenarioCase = new ScenarioCaseModel
          scenarioCase.setStatus(SUCCESS)
          recordedEvents.foreach {
            case InfoProvided(_, message, _, _, _, _, _, _, _) =>
              val step = new StepModel()
              step.setName(message)
              val (maybeIntroWord, word): (Option[String], String) = message.substring(0, 5) match {
                case "Given" => (Some("Given"), message.replaceFirst("^Given ", ""))
                case "When " => (Some("When"), message.replaceFirst("^When ", ""))
                case "Then " => (Some("Then"), message.replaceFirst("^Then ", ""))
                case _       => (None, message)
              }
              maybeIntroWord.foreach { introWord =>
                step.addIntroWord(new Word(introWord, true))
              }
              step.addWords(new Word(word))
              step.setStatus(PASSED)
              scenarioCase.addStep(step)
              ()
            case _: MarkupProvided =>
              ()
          }
          scenario.addCase(scenarioCase)
          report.addScenarioModel(scenario)
        }
        ()
      case _: SuiteCompleted =>
        currentSpecClass = None
        currentSpecInstance = None
      case _ =>
        ()
    }
  }

  def findTagIds(currentSpec: Class[_]): List[Tag] =
    currentSpec.getAnnotations
      .map(_.annotationType)
      .filter(_.getAnnotations.exists(a => a.annotationType().getName == "org.scalatest.TagAnnotation"))
      .map(JGivenHtml5Reporter.newReportTag)
      .toList

  private def loadSuiteClass(suiteClassName: String): Option[Class[_]] = Try(Class.forName(suiteClassName)).toOption

  private def instantiateSuiteClass(suiteClass: Class[_]): Option[Any] = Try(suiteClass.newInstance()).toOption

}

object JGivenHtml5Reporter {

  def newReportTag(tag: Class[_ <: Annotation]): Tag = newReportTag(tag.getName, tag.getSimpleName)

  def newReportTag(fullType: String, name: String): Tag = {
    val reportTag = new Tag(fullType)
    reportTag.setName(name)
    reportTag
  }

}
