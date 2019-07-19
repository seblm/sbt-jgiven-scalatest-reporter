package fr.fpe.scalatest.jgiven.json

import java.lang.reflect.Type

import com.google.gson._
import com.google.gson.reflect.TypeToken
import fr.fpe.scalatest.jgiven.json.GsonEventSupport.serializeEvent
import org.scalatest.ConfigMap
import org.scalatest.events._
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

object GsonEventRun {

  /**
    * Needed because default serializer doesn't encode type field
    */
  class RunAbortedSerializer() extends JsonSerializer[RunAborted] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: RunAborted, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val runAborted = serializeEvent(src, typeOfSrc, context)
      runAborted.add("message", context.serialize(src.message))
      src.throwable.foreach(throwable => runAborted.add("throwable", context.serialize(throwable)))
      src.duration.foreach(duration => runAborted.addProperty("duration", duration))
      src.summary.foreach(summary => runAborted.add("summary", context.serialize(summary)))
      runAborted
    }

  }

  /**
    * Needed because throwable, duration, summary, formatter, location and payload are optionals but yields to null if
    * non present
    */
  class RunAbortedDeserializer() extends JsonDeserializer[RunAborted] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RunAborted = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      RunAborted(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        message = jsonObject.get("message").getAsString,
        throwable = Option(context.deserialize(jsonObject.get("throwable"), new TypeToken[Throwable]() {}.getType)),
        duration = Option(jsonObject.get("duration")).map(_.getAsLong),
        summary = Option(context.deserialize(jsonObject.get("summary"), new TypeToken[Summary]() {}.getType)),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

  /**
    * Needed because default serializer doesn't encode type field
    */
  class RunCompletedSerializer() extends JsonSerializer[RunCompleted] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: RunCompleted, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val runCompleted = serializeEvent(src, typeOfSrc, context)
      src.duration.foreach(duration => runCompleted.addProperty("duration", duration))
      src.summary.foreach(summary => runCompleted.add("summary", context.serialize(summary)))
      runCompleted
    }

  }

  /**
    * Needed because duration, summary, formatter, location and payload are optionals but yields to null if non present
    */
  class RunCompletedDeserializer() extends JsonDeserializer[RunCompleted] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RunCompleted = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      RunCompleted(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        duration = Option(jsonObject.get("duration")).map(_.getAsLong),
        summary = Option(context.deserialize(jsonObject.get("summary"), new TypeToken[Summary]() {}.getType)),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

  /**
    * Needed because default serializer doesn't encode type field
    */
  class RunStartingSerializer() extends JsonSerializer[RunStarting] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: RunStarting, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val runStarting = serializeEvent(src, typeOfSrc, context)
      runStarting.addProperty("testCount", src.testCount)
      runStarting.add("configMap", context.serialize(src.configMap, new TypeToken[ConfigMap]() {}.getType))
      runStarting
    }

  }

  /**
    * Needed because formatter, location and payload are optionals but yields to null if non present
    */
  class RunStartingDeserializer() extends JsonDeserializer[RunStarting] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RunStarting = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      RunStarting(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        testCount = jsonObject.get("testCount").getAsInt,
        configMap = context.deserialize(jsonObject.get("configMap"), new TypeToken[ConfigMap]() {}.getType),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

  /**
    * Needed because default serializer doesn't encode type field
    */
  class RunStoppedSerializer() extends JsonSerializer[RunStopped] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: RunStopped, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val runStopped = serializeEvent(src, typeOfSrc, context)
      src.duration.foreach(duration => runStopped.addProperty("duration", duration))
      src.summary.foreach(summary => runStopped.add("summary", context.serialize(summary)))
      runStopped
    }

  }

  /**
    * Needed because duration, summary, formatter, location and payload are optionals but yields to null if non present
    */
  class RunStoppedDeserializer() extends JsonDeserializer[RunStopped] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RunStopped = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      RunStopped(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        duration = Option(jsonObject.get("duration")).map(_.getAsLong),
        summary = Option(context.deserialize(jsonObject.get("summary"), new TypeToken[Summary]() {}.getType)),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

}
