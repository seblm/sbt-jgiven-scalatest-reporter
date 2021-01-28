package fr.fpe.scalatest.jgiven.json

import java.lang.reflect.Type

import com.google.gson._
import com.google.gson.reflect.TypeToken
import fr.fpe.scalatest.jgiven.json.GsonEventSupport.serializeEvent
import org.scalatest.events._
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

object GsonEventMarkupProvided {

  /** Needed because default serializer doesn't encode field type
    */
  class MarkupProvidedSerializer() extends JsonSerializer[MarkupProvided] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: MarkupProvided, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val alertProvided = serializeEvent(src, typeOfSrc, context)
      alertProvided.add("text", context.serialize(src.text))
      src.nameInfo.foreach(nameInfo => alertProvided.add("nameInfo", context.serialize(nameInfo)))
      alertProvided
    }

  }

  /** Needed because nameInfo, formatter, location and payload are optionals but yields to null if non
    * present
    */
  class MarkupProvidedDeserializer() extends JsonDeserializer[MarkupProvided] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): MarkupProvided = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      val provided = MarkupProvided(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        text = jsonObject.get("text").getAsString,
        nameInfo = Option(context.deserialize(jsonObject.get("nameInfo"), new TypeToken[NameInfo]() {}.getType)),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
      provided
    }

  }

}
