package fr.fpe.scalatest.jgiven

import java.io.FileWriter
import java.nio.file.Paths

import fr.fpe.scalatest.jgiven.json.GsonEventSupport
import org.scalatest.ResourcefulReporter
import org.scalatest.events._

import scala.collection.mutable.ListBuffer

/**
  * Save every ScalaTest reported event to json file
  */
class JsonReporter extends ResourcefulReporter {

  private lazy val events: ListBuffer[Event] = ListBuffer[Event]()

  override def dispose(): Unit = {
    val fileWriter = new FileWriter(Paths.get("target", "scalatest-events.json").toFile)
    GsonEventSupport.toJson(events.toList, fileWriter)
    fileWriter.close()
  }

  override def apply(event: Event): Unit = events += event

}
