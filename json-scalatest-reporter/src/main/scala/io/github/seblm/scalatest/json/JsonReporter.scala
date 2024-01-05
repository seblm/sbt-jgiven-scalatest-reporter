package io.github.seblm.scalatest.json

import org.scalatest.ResourcefulReporter
import org.scalatest.events.Event

import java.io.FileWriter
import java.nio.file.Paths
import scala.collection.mutable.ListBuffer
import scala.util.Using

/** Save every ScalaTest reported event to json file
  */
class JsonReporter extends ResourcefulReporter {

  private lazy val events: ListBuffer[Event] = ListBuffer[Event]()

  override def dispose(): Unit =
    Using(new FileWriter(Paths.get("target", "scalatest-events.json").toFile)) { fileWriter =>
      GsonEventSupport.toJson(events.toList, fileWriter)
    }

  override def apply(event: Event): Unit = events += event

}
