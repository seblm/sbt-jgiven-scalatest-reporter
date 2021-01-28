package fr.fpe.scalatest.jgiven.json

import java.lang.reflect.Type

import com.google.gson.{JsonDeserializationContext, JsonDeserializer, JsonElement}
import org.scalatest.events.NameInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

object GsonNameInfo {

  /** Needed because suiteClassName and testName are optionals but yields to null if non present
    */
  class NameInfoDeserializer() extends JsonDeserializer[NameInfo] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): NameInfo = {
      logger.debug("deserialize {} of type {}", List(json, typeOfT): _*)
      val jsonObject = json.getAsJsonObject
      NameInfo(
        suiteName = jsonObject.get("suiteName").getAsString,
        suiteId = jsonObject.get("suiteId").getAsString,
        suiteClassName = Option(jsonObject.get("suiteClassName")).map(_.getAsString),
        testName = Option(jsonObject.get("testName")).map(_.getAsString)
      )
    }

  }

}
