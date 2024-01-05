package io.github.seblm.scalatest.json

import com.google.gson._
import org.scalatest.events.{LineInFile, TopOfClass, TopOfMethod}
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

import java.lang.reflect.Type

object GsonLocation {

  /** Needed because default serializer doesn't encode type field
    */
  class TopOfClassSerializer extends JsonSerializer[TopOfClass] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: TopOfClass, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", src, typeOfSrc)
      val topOfClass = new JsonObject()
      topOfClass.addProperty("type", "TopOfClass")
      topOfClass.addProperty("className", src.className)
      topOfClass
    }

  }

  /** Needed because default serializer doesn't encode type field
    */
  class TopOfMethodSerializer extends JsonSerializer[TopOfMethod] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: TopOfMethod, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", src, typeOfSrc)
      val topOfMethod = new JsonObject()
      topOfMethod.addProperty("type", "TopOfMethod")
      topOfMethod.addProperty("className", src.className)
      topOfMethod.addProperty("methodId", src.methodId)
      topOfMethod
    }

  }

  /** Needed because default serializer doesn't encode type field
    */
  class LineInFileSerializer extends JsonSerializer[LineInFile] {

    private lazy val logger: Logger = getLogger(getClass)

    override def serialize(src: LineInFile, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      logger.debug("serialize {} of type {}", src, typeOfSrc)
      val lineInFile = new JsonObject()
      lineInFile.addProperty("type", "LineInFile")
      lineInFile.addProperty("lineNumber", src.lineNumber)
      lineInFile.addProperty("fileName", src.fileName)
      src.filePathname.foreach(filePathname => lineInFile.addProperty("filePathname", filePathname))
      lineInFile
    }

  }

  /** Needed because filePathname is optional but yields to null if non present
    */
  class LineInFileDeserializer extends JsonDeserializer[LineInFile] {

    private lazy val logger: Logger = getLogger(getClass)

    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LineInFile = {
      logger.debug("deserialize {} of type {}", json, typeOfT)
      val jsonObject = json.getAsJsonObject
      LineInFile(
        lineNumber = jsonObject.get("lineNumber").getAsInt,
        fileName = jsonObject.get("fileName").getAsString,
        filePathname = Option(jsonObject.get("filePathname")).map(_.getAsString)
      )
    }

  }

}
