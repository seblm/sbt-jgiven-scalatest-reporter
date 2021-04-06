package io.github.seblm.scalatest.jgiven.json

import com.google.gson._
import com.google.gson.reflect.TypeToken
import io.github.seblm.scalatest.jgiven.json.GsonEventSupport.serializeEvent
import org.scalatest.events._
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

import java.lang.reflect.Type

object GsonEventInfoProvided {

  /** Needed because default serializer doesn't encode type field
    */
  class InfoProvidedSerializer() extends JsonSerializer[InfoProvided] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: InfoProvided, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val infoProvided = serializeEvent(src, typeOfSrc, context)
      infoProvided.add("message", context.serialize(src.message))
      src.nameInfo.foreach(nameInfo => infoProvided.add("nameInfo", context.serialize(nameInfo)))
      src.throwable.foreach(throwable => infoProvided.add("throwable", context.serialize(throwable)))
      infoProvided
    }

  }

  /** Needed because nameInfo, throwable, formatter, location and payload are optionals but yields to null if non
    * present
    */
  class InfoProvidedDeserializer() extends JsonDeserializer[InfoProvided] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): InfoProvided = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      InfoProvided(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        message = jsonObject.get("message").getAsString,
        nameInfo = Option(context.deserialize(jsonObject.get("nameInfo"), new TypeToken[NameInfo]() {}.getType)),
        throwable = Option(context.deserialize(jsonObject.get("throwable"), new TypeToken[Throwable]() {}.getType)),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

}
