package io.github.seblm.scalatest.json

import com.google.gson._
import com.google.gson.reflect.TypeToken
import io.github.seblm.scalatest.json.GsonEventSupport.serializeEvent
import org.scalatest.events._
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

import java.lang.reflect.Type

object GsonEventSuite {

  /** Needed because default serializer doesn't encode type field
    */
  class SuiteAbortedSerializer() extends JsonSerializer[SuiteAborted] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: SuiteAborted, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val suiteAborted = serializeEvent(src, typeOfSrc, context)
      suiteAborted.add("message", context.serialize(src.message))
      suiteAborted.addProperty("suiteName", src.suiteName)
      suiteAborted.addProperty("suiteId", src.suiteId)
      src.suiteClassName.foreach(suiteClassName => suiteAborted.addProperty("suiteClassName", suiteClassName))
      src.throwable.foreach(throwable => suiteAborted.add("throwable", context.serialize(throwable)))
      src.duration.foreach(duration => suiteAborted.addProperty("duration", duration))
      src.rerunner.foreach(rerunner => suiteAborted.addProperty("rerunner", rerunner))
      suiteAborted
    }

  }

  /** Needed because throwable, duration, summary, formatter, location and payload are optionals but yields to null if
    * non present
    */
  class SuiteAbortedDeserializer() extends JsonDeserializer[SuiteAborted] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SuiteAborted = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      SuiteAborted(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        message = jsonObject.get("message").getAsString,
        suiteName = jsonObject.get("suiteName").getAsString,
        suiteId = jsonObject.get("suiteId").getAsString,
        suiteClassName = Option(jsonObject.get("suiteClassName")).map(_.getAsString),
        throwable = Option(context.deserialize(jsonObject.get("throwable"), new TypeToken[Throwable]() {}.getType)),
        duration = Option(jsonObject.get("duration")).map(_.getAsLong),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        rerunner = Option(jsonObject.get("rerunner")).map(_.getAsString),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

  /** Needed because default serializer doesn't encode type field
    */
  class SuiteCompletedSerializer() extends JsonSerializer[SuiteCompleted] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: SuiteCompleted, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val suiteCompleted = serializeEvent(src, typeOfSrc, context)
      suiteCompleted.addProperty("suiteName", src.suiteName)
      suiteCompleted.addProperty("suiteId", src.suiteId)
      src.suiteClassName.foreach(suiteClassName => suiteCompleted.addProperty("suiteClassName", suiteClassName))
      src.duration.foreach(duration => suiteCompleted.addProperty("duration", duration))
      src.rerunner.foreach(rerunner => suiteCompleted.addProperty("rerunner", rerunner))
      suiteCompleted
    }

  }

  /** Needed because duration, summary, formatter, location and payload are optionals but yields to null if non present
    */
  class SuiteCompletedDeserializer() extends JsonDeserializer[SuiteCompleted] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SuiteCompleted = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      SuiteCompleted(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        suiteName = jsonObject.get("suiteName").getAsString,
        suiteId = jsonObject.get("suiteId").getAsString,
        suiteClassName = Option(jsonObject.get("suiteClassName")).map(_.getAsString),
        duration = Option(jsonObject.get("duration")).map(_.getAsLong),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        rerunner = Option(jsonObject.get("rerunner")).map(_.getAsString),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

  /** Needed because default serializer doesn't encode type field
    */
  class SuiteStartingSerializer() extends JsonSerializer[SuiteStarting] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: SuiteStarting, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val suiteStarting = serializeEvent(src, typeOfSrc, context)
      suiteStarting.addProperty("suiteName", src.suiteName)
      suiteStarting.addProperty("suiteId", src.suiteId)
      src.suiteClassName.foreach(suiteClassName => suiteStarting.addProperty("suiteClassName", suiteClassName))
      src.rerunner.foreach(rerunner => suiteStarting.addProperty("rerunner", rerunner))
      suiteStarting
    }

  }

  /** Needed because suiteClassName, formatter, location, rerunner and payload are optionals but yields to null if non
    * present
    */
  class SuiteStartingDeserializer() extends JsonDeserializer[SuiteStarting] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SuiteStarting = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      SuiteStarting(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        suiteName = jsonObject.get("suiteName").getAsString,
        suiteId = jsonObject.get("suiteId").getAsString,
        suiteClassName = Option(jsonObject.get("suiteClassName")).map(_.getAsString),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        rerunner = Option(jsonObject.get("rerunner")).map(_.getAsString),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

}
