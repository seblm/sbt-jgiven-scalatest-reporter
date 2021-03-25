package io.github.seblm.scalatest.jgiven.json

import java.lang.reflect.Type

import com.google.gson._
import com.google.gson.reflect.TypeToken
import GsonEventSupport.serializeEvent
import org.scalatest.events._
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

import scala.collection.immutable.IndexedSeq

object GsonEventTest {

  /** Needed because default serializer doesn't encode type field
    */
  class TestCanceledSerializer() extends JsonSerializer[TestCanceled] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: TestCanceled, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val testCanceled = serializeEvent(src, typeOfSrc, context)
      testCanceled.add("message", context.serialize(src.message))
      testCanceled.addProperty("suiteName", src.suiteName)
      testCanceled.addProperty("suiteId", src.suiteId)
      src.suiteClassName.foreach(suiteClassName => testCanceled.addProperty("suiteClassName", suiteClassName))
      testCanceled.addProperty("testName", src.testName)
      testCanceled.addProperty("testText", src.testText)
      testCanceled.add(
        "recordedEvents",
        context.serialize(src.recordedEvents, new TypeToken[IndexedSeq[RecordableEvent]]() {}.getType)
      )
      src.throwable.foreach(throwable => testCanceled.add("throwable", context.serialize(throwable)))
      src.duration.foreach(duration => testCanceled.addProperty("duration", duration))
      src.rerunner.foreach(rerunner => testCanceled.addProperty("rerunner", rerunner))
      testCanceled
    }

  }

  /** Needed because suiteClassName, throwable, duration, formatter, location, rerunner and payload are optionals but
    * yields to null if non present
    */
  class TestCanceledDeserializer() extends JsonDeserializer[TestCanceled] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TestCanceled = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      TestCanceled(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        message = jsonObject.get("message").getAsString,
        suiteName = jsonObject.get("suiteName").getAsString,
        suiteId = jsonObject.get("suiteId").getAsString,
        suiteClassName = Option(jsonObject.get("suiteClassName")).map(_.getAsString),
        testName = jsonObject.get("testName").getAsString,
        testText = jsonObject.get("testText").getAsString,
        recordedEvents = context
          .deserialize(jsonObject.get("recordedEvents"), new TypeToken[IndexedSeq[RecordableEvent]]() {}.getType),
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
  class TestFailedSerializer() extends JsonSerializer[TestFailed] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: TestFailed, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val testFailed = serializeEvent(src, typeOfSrc, context)
      testFailed.add("message", context.serialize(src.message))
      testFailed.addProperty("suiteName", src.suiteName)
      testFailed.addProperty("suiteId", src.suiteId)
      src.suiteClassName.foreach(suiteClassName => testFailed.addProperty("suiteClassName", suiteClassName))
      testFailed.addProperty("testName", src.testName)
      testFailed.addProperty("testText", src.testText)
      testFailed.add(
        "recordedEvents",
        context.serialize(src.recordedEvents, new TypeToken[IndexedSeq[RecordableEvent]]() {}.getType)
      )
      testFailed.add("analysis", context.serialize(src.analysis, new TypeToken[IndexedSeq[String]]() {}.getType))
      src.throwable.foreach(throwable =>
        testFailed.add("throwable", context.serialize(throwable, new TypeToken[Throwable]() {}.getType))
      )
      src.duration.foreach(duration => testFailed.addProperty("duration", duration))
      src.rerunner.foreach(rerunner => testFailed.addProperty("rerunner", rerunner))
      testFailed
    }

  }

  /** Needed because suiteClassName, throwable, duration, formatter, location, rerunner and payload are optionals but
    * yields to null if non present
    */
  class TestFailedDeserializer() extends JsonDeserializer[TestFailed] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TestFailed = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      TestFailed(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        message = jsonObject.get("message").getAsString,
        suiteName = jsonObject.get("suiteName").getAsString,
        suiteId = jsonObject.get("suiteId").getAsString,
        suiteClassName = Option(jsonObject.get("suiteClassName")).map(_.getAsString),
        testName = jsonObject.get("testName").getAsString,
        testText = jsonObject.get("testText").getAsString,
        recordedEvents = context
          .deserialize(jsonObject.get("recordedEvents"), new TypeToken[IndexedSeq[RecordableEvent]]() {}.getType),
        analysis = context.deserialize(jsonObject.get("analysis"), new TypeToken[IndexedSeq[String]]() {}.getType),
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
  class TestIgnoredSerializer() extends JsonSerializer[TestIgnored] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: TestIgnored, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val testIgnored = serializeEvent(src, typeOfSrc, context)
      testIgnored.addProperty("suiteName", src.suiteName)
      testIgnored.addProperty("suiteId", src.suiteId)
      src.suiteClassName.foreach(suiteClassName => testIgnored.addProperty("suiteClassName", suiteClassName))
      testIgnored.addProperty("testName", src.testName)
      testIgnored.addProperty("testText", src.testText)
      testIgnored
    }

  }

  /** Needed because suiteClassName, formatter, location and payload are optionals but yields to null if non present
    */
  class TestIgnoredDeserializer() extends JsonDeserializer[TestIgnored] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TestIgnored = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      TestIgnored(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        suiteName = jsonObject.get("suiteName").getAsString,
        suiteId = jsonObject.get("suiteId").getAsString,
        suiteClassName = Option(jsonObject.get("suiteClassName")).map(_.getAsString),
        testName = jsonObject.get("testName").getAsString,
        testText = jsonObject.get("testText").getAsString,
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

  /** Needed because default serializer doesn't encode type field
    */
  class TestPendingSerializer() extends JsonSerializer[TestPending] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: TestPending, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val testPending = serializeEvent(src, typeOfSrc, context)
      testPending.addProperty("suiteName", src.suiteName)
      testPending.addProperty("suiteId", src.suiteId)
      src.suiteClassName.foreach(suiteClassName => testPending.addProperty("suiteClassName", suiteClassName))
      testPending.addProperty("testName", src.testName)
      testPending.addProperty("testText", src.testText)
      testPending.add(
        "recordedEvents",
        context.serialize(src.recordedEvents, new TypeToken[IndexedSeq[RecordableEvent]]() {}.getType)
      )
      src.duration.foreach(duration => testPending.addProperty("duration", duration))
      testPending
    }

  }

  /** Needed because suiteClassName, duration, formatter, location and payload are optionals but yields to null if non
    * present
    */
  class TestPendingDeserializer() extends JsonDeserializer[TestPending] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TestPending = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      TestPending(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        suiteName = jsonObject.get("suiteName").getAsString,
        suiteId = jsonObject.get("suiteId").getAsString,
        suiteClassName = Option(jsonObject.get("suiteClassName")).map(_.getAsString),
        testName = jsonObject.get("testName").getAsString,
        testText = jsonObject.get("testText").getAsString,
        recordedEvents = context
          .deserialize(jsonObject.get("recordedEvents"), new TypeToken[IndexedSeq[RecordableEvent]]() {}.getType),
        duration = Option(jsonObject.get("duration")).map(_.getAsLong),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

  /** Needed because default serializer doesn't encode type field
    */
  class TestStartingSerializer() extends JsonSerializer[TestStarting] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: TestStarting, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val testStarting = serializeEvent(src, typeOfSrc, context)
      testStarting.addProperty("suiteName", src.suiteName)
      testStarting.addProperty("suiteId", src.suiteId)
      src.suiteClassName.foreach(suiteClassName => testStarting.addProperty("suiteClassName", suiteClassName))
      testStarting.addProperty("testName", src.testName)
      testStarting.addProperty("testText", src.testText)
      src.rerunner.foreach(rerunner => testStarting.addProperty("rerunner", rerunner))
      testStarting
    }

  }

  /** Needed because suiteClassName, formatter, location, rerunner and payload are optionals but yields to null if non
    * present
    */
  class TestStartingDeserializer() extends JsonDeserializer[TestStarting] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TestStarting = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      TestStarting(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        suiteName = jsonObject.get("suiteName").getAsString,
        suiteId = jsonObject.get("suiteId").getAsString,
        suiteClassName = Option(jsonObject.get("suiteClassName")).map(_.getAsString),
        testName = jsonObject.get("testName").getAsString,
        testText = jsonObject.get("testText").getAsString,
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
  class TestSucceededSerializer() extends JsonSerializer[TestSucceeded] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: TestSucceeded, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val testSucceeded = serializeEvent(src, typeOfSrc, context)
      testSucceeded.addProperty("suiteName", src.suiteName)
      testSucceeded.addProperty("suiteId", src.suiteId)
      src.suiteClassName.foreach(suiteClassName => testSucceeded.addProperty("suiteClassName", suiteClassName))
      testSucceeded.addProperty("testName", src.testName)
      testSucceeded.addProperty("testText", src.testText)
      testSucceeded.add(
        "recordedEvents",
        context.serialize(src.recordedEvents, new TypeToken[IndexedSeq[RecordableEvent]]() {}.getType)
      )
      src.duration.foreach(duration => testSucceeded.addProperty("duration", duration))
      src.rerunner.foreach(rerunner => testSucceeded.addProperty("rerunner", rerunner))
      testSucceeded
    }

  }

  /** Needed because suiteClassName, duration, formatter, location, rerunner and payload are optionals but yields to
    * null if non present
    */
  class TestSucceededDeserializer() extends JsonDeserializer[TestSucceeded] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TestSucceeded = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      TestSucceeded(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        suiteName = jsonObject.get("suiteName").getAsString,
        suiteId = jsonObject.get("suiteId").getAsString,
        suiteClassName = Option(jsonObject.get("suiteClassName")).map(_.getAsString),
        testName = jsonObject.get("testName").getAsString,
        testText = jsonObject.get("testText").getAsString,
        recordedEvents = context
          .deserialize(jsonObject.get("recordedEvents"), new TypeToken[IndexedSeq[RecordableEvent]]() {}.getType),
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

}
