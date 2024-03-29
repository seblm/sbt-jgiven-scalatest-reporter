package io.github.seblm.scalatest.json

import com.google.gson._
import com.google.gson.reflect.TypeToken
import org.scalatest.events.{Event, RecordableEvent}
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

import java.lang.reflect.{ParameterizedType, Type}
import java.util
import scala.jdk.CollectionConverters._

object GsonScala {

  class OptionSerializer extends JsonSerializer[Option[_]] {

    private lazy val logger: Logger = getLogger(this.getClass)

    override def serialize(option: Option[_], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", option, typeOfSrc)
      option.fold[JsonElement](JsonNull.INSTANCE)(context.serialize)
    }

  }

  class OptionDeserializer extends JsonDeserializer[Option[_]] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Option[_] = {
      logger.debug("deserialize {} of type {}", json, typeOfT)
      Option(context.deserialize(json, typeOfT.asInstanceOf[ParameterizedType].getActualTypeArguments.head))
    }

  }

  /** Needed by MapConfig.underlying
    */
  class MapInstanceCreator extends InstanceCreator[Map[_, _]] {

    private lazy val logger: Logger = getLogger(getClass)

    override def createInstance(`type`: Type): Map[_, _] = {
      logger.debug("create a new instance of {}", `type`)
      Map.empty
    }

  }

  /** Needed by MapConfig.underlying
    */
  class MapSerializer extends JsonSerializer[Map[String, Any]] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: Map[String, Any], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", src, typeOfSrc)
      context.serialize(src.asJava, new TypeToken[util.Map[String, Any]]() {}.getType)
    }

  }

  /** Needed by MapConfig.underlying
    */
  class MapDeserializer extends JsonDeserializer[Map[String, Any]] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Map[String, Any] = {
      logger.debug("deserialize {} of type {}", json, typeOfT)
      json.getAsJsonObject
        .entrySet()
        .asScala
        .map(entry => (entry.getKey, context.deserialize(entry.getValue, new TypeToken[Any]() {}.getType)))
        .toMap
    }

  }

  /** Needed by Test* events
    */
  class IndexedSeqInstanceCreator[T] extends InstanceCreator[IndexedSeq[T]] {

    private lazy val logger: Logger = getLogger(getClass)

    override def createInstance(`type`: Type): IndexedSeq[T] = {
      logger.debug("create a new instance of {}", `type`)
      IndexedSeq.empty[T]
    }

  }

  /** Needed by Test* events
    */
  class IndexedSeqSerializer[T] extends JsonSerializer[IndexedSeq[T]] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: IndexedSeq[T], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", src, typeOfSrc)
      context.serialize(src.asJavaCollection, new TypeToken[util.Collection[Event]]() {}.getType)
    }

  }

  /** Needed by Test* events
    */
  class IndexedSeqDeserializer extends JsonDeserializer[IndexedSeq[RecordableEvent]] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): IndexedSeq[RecordableEvent] = {
      logger.debug("deserialize {} of type {}", json, typeOfT)
      context
        .deserialize[util.Collection[RecordableEvent]](json, new TypeToken[util.Collection[Event]]() {}.getType)
        .asScala
        .toIndexedSeq
    }

  }

  /** Needed because some Throwables can't be serialized. For example, Gson fails to serialize TestFailedException with
    * message: class org.scalatest.exceptions.TestFailedException declares multiple JSON fields named posOrStackDepthFun
    */
  class ThrowableSerializer extends JsonSerializer[Throwable] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: Throwable, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", src, typeOfSrc)

      context.serialize(
        new Throwable(s"${src.getClass.getName}${Option(src.getMessage).map(message => s": $message").getOrElse("")}")
      )
    }

  }

  /** Needed because Throwables can't be deserialized because JsonIOException is raised with this message: <i>Failed
    * making field 'java.lang.Throwable#detailMessage' accessible; either increase its visibility or write a custom
    * TypeAdapter for its declaring type.</i>
    */
  class ThrowableDeserializer extends JsonDeserializer[Throwable] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Throwable = {
      logger.debug("deserialize {} of type {}", json, typeOfT)

      Option(json.getAsJsonObject.get("detailMessage"))
        .map(_.getAsString)
        .fold(new Throwable())(detailMessage => new Throwable(detailMessage))
    }

  }

  /** Serialize object with only type field
    */
  class ObjectSerializer[T] extends JsonSerializer[T] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", src, typeOfSrc)
      val motionToSuppress = new JsonObject()
      motionToSuppress.add("type", new JsonPrimitive(typeOfSrc.asInstanceOf[Class[_]].getSimpleName))
      motionToSuppress
    }

  }

}
