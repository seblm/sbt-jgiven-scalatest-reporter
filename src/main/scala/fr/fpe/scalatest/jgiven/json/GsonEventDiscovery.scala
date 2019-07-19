package fr.fpe.scalatest.jgiven.json

import java.lang.reflect.Type

import com.google.gson._
import com.google.gson.reflect.TypeToken
import fr.fpe.scalatest.jgiven.json.GsonEventSupport.serializeEvent
import org.scalatest.events._
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

object GsonEventDiscovery {

  /**
    * Needed because default serializer doesn't encode type field
    */
  class DiscoveryCompletedSerializer() extends JsonSerializer[DiscoveryCompleted] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: DiscoveryCompleted, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val discoveryCompleted = serializeEvent(src, typeOfSrc, context)
      src.duration.foreach(duration => discoveryCompleted.add("duration", context.serialize(duration)))
      discoveryCompleted
    }

  }

  /**
    * Needed because duration, formatter, location and payload are optionals but yields to null if non present
    */
  class DiscoveryCompletedDeserializer() extends JsonDeserializer[DiscoveryCompleted] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement,
                             typeOfT: Type,
                             context: JsonDeserializationContext): DiscoveryCompleted = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      DiscoveryCompleted(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        duration = Option(jsonObject.get("duration")).map(_.getAsLong),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

}
