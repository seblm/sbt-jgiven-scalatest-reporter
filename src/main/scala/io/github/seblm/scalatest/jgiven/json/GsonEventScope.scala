package io.github.seblm.scalatest.jgiven.json

import java.lang.reflect.Type

import com.google.gson._
import com.google.gson.reflect.TypeToken
import GsonEventSupport.serializeEvent
import org.scalatest.events._
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

object GsonEventScope {

  /** Needed because default serializer doesn't encode type field
    */
  class ScopeClosedSerializer() extends JsonSerializer[ScopeClosed] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: ScopeClosed, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val scopeClosed = serializeEvent(src, typeOfSrc, context)
      scopeClosed.add("message", context.serialize(src.message))
      scopeClosed.add("nameInfo", context.serialize(src.nameInfo, new TypeToken[NameInfo]() {}.getType))
      scopeClosed
    }

  }

  /** Needed because formatter, location and payload are optionals but yields to null if non present
    */
  class ScopeClosedDeserializer() extends JsonDeserializer[ScopeClosed] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ScopeClosed = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      ScopeClosed(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        message = jsonObject.get("message").getAsString,
        nameInfo = context.deserialize(jsonObject.get("nameInfo"), new TypeToken[NameInfo]() {}.getType),
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
  class ScopeOpenedSerializer() extends JsonSerializer[ScopeOpened] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: ScopeOpened, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val scopeOpened = serializeEvent(src, typeOfSrc, context)
      scopeOpened.add("message", context.serialize(src.message))
      scopeOpened.add("nameInfo", context.serialize(src.nameInfo, new TypeToken[NameInfo]() {}.getType))
      scopeOpened
    }

  }

  /** Needed because formatter, location and payload are optionals but yields to null if non present
    */
  class ScopeOpenedDeserializer() extends JsonDeserializer[ScopeOpened] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ScopeOpened = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      ScopeOpened(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        message = jsonObject.get("message").getAsString,
        nameInfo = context.deserialize(jsonObject.get("nameInfo"), new TypeToken[NameInfo]() {}.getType),
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
  class ScopePendingSerializer() extends JsonSerializer[ScopePending] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: ScopePending, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", List(src, typeOfSrc): _*)
      val scopePending = serializeEvent(src, typeOfSrc, context)
      scopePending.add("message", context.serialize(src.message))
      scopePending.add("nameInfo", context.serialize(src.nameInfo, new TypeToken[NameInfo]() {}.getType))
      scopePending
    }

  }

  /** Needed because formatter, location and payload are optionals but yields to null if non present
    */
  class ScopePendingDeserializer() extends JsonDeserializer[ScopePending] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ScopePending = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      ScopePending(
        ordinal = context.deserialize(jsonObject.get("ordinal"), new TypeToken[Ordinal]() {}.getType),
        message = jsonObject.get("message").getAsString,
        nameInfo = context.deserialize(jsonObject.get("nameInfo"), new TypeToken[NameInfo]() {}.getType),
        formatter = Option(context.deserialize(jsonObject.get("formatter"), new TypeToken[Formatter]() {}.getType)),
        location = Option(context.deserialize(jsonObject.get("location"), new TypeToken[Location]() {}.getType)),
        payload = Option(context.deserialize(jsonObject.get("payload"), new TypeToken[Any]() {}.getType)),
        threadName = jsonObject.get("threadName").getAsString,
        timeStamp = jsonObject.get("timeStamp").getAsLong
      )
    }

  }

}
