package io.github.seblm.scalatest.jgiven

import io.github.seblm.scalatest.jgiven.json.GsonEventSupport

import java.nio.file.{Files, Paths}
import java.util
import org.scalatest.GivenWhenThen
import org.scalatest.events.Event

import scala.collection.JavaConverters._
import scala.io.Source

object JGivenHtml5ReporterFixture {

  def given_sbt_jgiven_scalatest_reporter_is_installed(bdd: GivenWhenThen): JGivenHtml5Reporter = {
    bdd.Given("sbt-jgiven-scalatest-reporter is installed")
    new JGivenHtml5Reporter()
  }

  def when_scalatest_is_executed(bdd: GivenWhenThen, reporter: JGivenHtml5Reporter, eventsFileName: String): Unit = {
    bdd.When("ScalaTest is executed")
    applyEvents(reporter, events(eventsFileName))
    reporter.dispose()
  }

  def events(fileName: String): List[Event] =
    GsonEventSupport.gson
      .fromJson[util.Collection[Event]](
        Source.fromResource(s"io/github/seblm/scalatest/jgiven/$fileName").bufferedReader(),
        GsonEventSupport.collectionOfEventsType
      )
      .asScala
      .toList

  def applyEvents(reporter: JGivenHtml5Reporter, events: Seq[Event]): JGivenHtml5Reporter = events match {
    case Nil => reporter
    case event :: tail =>
      reporter.apply(event)
      applyEvents(reporter, tail)
  }

  def removeFiles(): Unit = {
    val jGivenReports = Paths.get("target", "jgiven-reports")
    if (jGivenReports.toFile.exists()) {
      Files.walk(jGivenReports).iterator.asScala.map(_.toFile).foreach(_.delete)
    }
  }

}
