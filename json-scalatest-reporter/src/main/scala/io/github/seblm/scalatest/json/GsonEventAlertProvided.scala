package io.github.seblm.scalatest.json

import com.google.gson._
import com.google.gson.reflect.TypeToken
import io.github.seblm.scalatest.json.GsonEventSupport.serializeEvent
import org.scalatest.events._
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

import java.lang.reflect.Type

object GsonEventAlertProvided {

  /** Needed because default serializer doesn't encode type field
    */
  class AlertProvidedSerializer extends JsonSerializer[AlertProvided] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: AlertProvided, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", src, typeOfSrc)
      val alertProvided = serializeEvent(src, typeOfSrc, context)
      alertProvided.add("message", context.serialize(src.message))
      src.nameInfo.foreach(nameInfo => alertProvided.add("nameInfo", context.serialize(nameInfo)))
      src.throwable.foreach(throwable => alertProvided.add("throwable", context.serialize(throwable)))
      alertProvided
    }

  }

  /** Needed because nameInfo, throwable, formatter, location and payload are optionals but yields to null if non
    * present
    */
  class AlertProvidedDeserializer extends JsonDeserializer[AlertProvided] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): AlertProvided = {
      logger.debug("deserialize {} of type {}", json, typeOfT)
      val jsonObject = json.getAsJsonObject
      AlertProvided(
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
