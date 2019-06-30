package name.lemerdy.sebastian.scalatest.jgiven.json

import java.lang.reflect.Type
import java.util

import com.google.gson.reflect.TypeToken
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.google.gson.{Gson, GsonBuilder, JsonObject, JsonSerializationContext}
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonEventAlertProvided._
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonEventDiscovery._
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonEventInfoProvided._
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonEventMarkupProvided._
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonEventNoteProvided._
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonEventRun._
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonEventScope._
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonEventSuite._
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonEventTest._
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonFormatter.IndentedTextSerializer
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonLocation._
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonNameInfo.NameInfoDeserializer
import name.lemerdy.sebastian.scalatest.jgiven.json.GsonScala._
import org.scalatest.events._

import scala.collection.JavaConverters._
import scala.collection.immutable.IndexedSeq

object GsonEventSupport {

  def toJson(events: List[Event], writer: Appendable): Unit =
    gson.toJson(events.asJavaCollection, collectionOfEventsType, writer)

  lazy val eventAdapterFactory: RuntimeTypeAdapterFactory[Event] = RuntimeTypeAdapterFactory
    .of(classOf[Event])
    .registerSubtype(classOf[AlertProvided])
    .registerSubtype(classOf[DiscoveryCompleted])
    .registerSubtype(classOf[DiscoveryStarting])
    .registerSubtype(classOf[InfoProvided])
    .registerSubtype(classOf[MarkupProvided])
    .registerSubtype(classOf[NoteProvided])
    .registerSubtype(classOf[RunAborted])
    .registerSubtype(classOf[RunCompleted])
    .registerSubtype(classOf[RunStarting])
    .registerSubtype(classOf[RunStopped])
    .registerSubtype(classOf[ScopeClosed])
    .registerSubtype(classOf[ScopeOpened])
    .registerSubtype(classOf[ScopePending])
    .registerSubtype(classOf[SuiteAborted])
    .registerSubtype(classOf[SuiteCompleted])
    .registerSubtype(classOf[SuiteStarting])
    .registerSubtype(classOf[TestCanceled])
    .registerSubtype(classOf[TestFailed])
    .registerSubtype(classOf[TestIgnored])
    .registerSubtype(classOf[TestPending])
    .registerSubtype(classOf[TestStarting])
    .registerSubtype(classOf[TestSucceeded])

  lazy val formatterAdapterFactory: RuntimeTypeAdapterFactory[Formatter] = RuntimeTypeAdapterFactory
    .of(classOf[Formatter])
    .registerSubtype(classOf[IndentedText])
    .registerSubtype(MotionToSuppress.getClass)

  lazy val locationAdapterFactory: RuntimeTypeAdapterFactory[Location] = RuntimeTypeAdapterFactory
    .of(classOf[Location])
    .registerSubtype(classOf[TopOfClass])
    .registerSubtype(classOf[TopOfMethod])
    .registerSubtype(classOf[LineInFile])
    .registerSubtype(SeeStackDepthException.getClass)

  lazy val collectionOfEventsType
    : Type = new TypeToken[util.Collection[Event]]() {}.getType // Needed to keep runtime information of what this collection is and to choose RuntimeTypeAdapterFactory[Event]

  lazy val gson: Gson = new GsonBuilder()
    .registerTypeAdapter(classOf[Option[_]], new OptionSerializer())
    .registerTypeAdapter(classOf[Option[_]], new OptionDeserializer())
    .registerTypeAdapter(classOf[Map[String, Any]], new MapInstanceCreator())
    .registerTypeAdapter(classOf[Map[String, Any]], new MapSerializer())
    .registerTypeAdapter(classOf[Map[String, Any]], new MapDeserializer())
    .registerTypeAdapter(classOf[IndexedSeq[RecordableEvent]], new IndexedSeqInstanceCreator[RecordableEvent]())
    .registerTypeAdapter(classOf[IndexedSeq[RecordableEvent]], new IndexedSeqSerializer[RecordableEvent]())
    .registerTypeAdapter(classOf[IndexedSeq[RecordableEvent]], new IndexedSeqDeserializer())
    .registerTypeAdapter(classOf[Throwable], new ThrowableSerializer())
    .registerTypeAdapter(classOf[IndentedText], new IndentedTextSerializer())
    .registerTypeAdapter(MotionToSuppress.getClass, new ObjectSerializer[Formatter]())
    .registerTypeAdapter(classOf[AlertProvided], new AlertProvidedSerializer())
    .registerTypeAdapter(classOf[AlertProvided], new AlertProvidedDeserializer())
    .registerTypeAdapter(classOf[DiscoveryCompleted], new DiscoveryCompletedSerializer())
    .registerTypeAdapter(classOf[DiscoveryCompleted], new DiscoveryCompletedDeserializer())
    .registerTypeAdapter(classOf[InfoProvided], new InfoProvidedSerializer())
    .registerTypeAdapter(classOf[InfoProvided], new InfoProvidedDeserializer())
    .registerTypeAdapter(classOf[MarkupProvided], new MarkupProvidedSerializer())
    .registerTypeAdapter(classOf[MarkupProvided], new MarkupProvidedDeserializer())
    .registerTypeAdapter(classOf[NoteProvided], new NoteProvidedSerializer())
    .registerTypeAdapter(classOf[NoteProvided], new NoteProvidedDeserializer())
    .registerTypeAdapter(classOf[RunAborted], new RunAbortedSerializer())
    .registerTypeAdapter(classOf[RunAborted], new RunAbortedDeserializer())
    .registerTypeAdapter(classOf[RunCompleted], new RunCompletedSerializer())
    .registerTypeAdapter(classOf[RunCompleted], new RunCompletedDeserializer())
    .registerTypeAdapter(classOf[RunStarting], new RunStartingSerializer())
    .registerTypeAdapter(classOf[RunStarting], new RunStartingDeserializer())
    .registerTypeAdapter(classOf[RunStopped], new RunStoppedSerializer())
    .registerTypeAdapter(classOf[RunStopped], new RunStoppedDeserializer())
    .registerTypeAdapter(classOf[ScopeOpened], new ScopeOpenedSerializer())
    .registerTypeAdapter(classOf[ScopeOpened], new ScopeOpenedDeserializer())
    .registerTypeAdapter(classOf[ScopePending], new ScopePendingSerializer())
    .registerTypeAdapter(classOf[ScopePending], new ScopePendingDeserializer())
    .registerTypeAdapter(classOf[ScopeClosed], new ScopeClosedSerializer())
    .registerTypeAdapter(classOf[ScopeClosed], new ScopeClosedDeserializer())
    .registerTypeAdapter(classOf[SuiteAborted], new SuiteAbortedSerializer())
    .registerTypeAdapter(classOf[SuiteAborted], new SuiteAbortedDeserializer())
    .registerTypeAdapter(classOf[SuiteCompleted], new SuiteCompletedSerializer())
    .registerTypeAdapter(classOf[SuiteCompleted], new SuiteCompletedDeserializer())
    .registerTypeAdapter(classOf[SuiteStarting], new SuiteStartingSerializer())
    .registerTypeAdapter(classOf[SuiteStarting], new SuiteStartingDeserializer())
    .registerTypeAdapter(classOf[TestCanceled], new TestCanceledSerializer())
    .registerTypeAdapter(classOf[TestCanceled], new TestCanceledDeserializer())
    .registerTypeAdapter(classOf[TestFailed], new TestFailedSerializer())
    .registerTypeAdapter(classOf[TestFailed], new TestFailedDeserializer())
    .registerTypeAdapter(classOf[TestStarting], new TestStartingSerializer())
    .registerTypeAdapter(classOf[TestStarting], new TestStartingDeserializer())
    .registerTypeAdapter(classOf[TestPending], new TestPendingSerializer())
    .registerTypeAdapter(classOf[TestPending], new TestPendingDeserializer())
    .registerTypeAdapter(classOf[TestIgnored], new TestIgnoredSerializer())
    .registerTypeAdapter(classOf[TestIgnored], new TestIgnoredDeserializer())
    .registerTypeAdapter(classOf[TestSucceeded], new TestSucceededSerializer())
    .registerTypeAdapter(classOf[TestSucceeded], new TestSucceededDeserializer())
    .registerTypeAdapter(classOf[NameInfo], new NameInfoDeserializer())
    .registerTypeAdapter(classOf[TopOfClass], new TopOfClassSerializer())
    .registerTypeAdapter(classOf[TopOfMethod], new TopOfMethodSerializer())
    .registerTypeAdapter(classOf[LineInFile], new LineInFileSerializer())
    .registerTypeAdapter(classOf[LineInFile], new LineInFileDeserializer())
    .registerTypeAdapter(SeeStackDepthException.getClass, new ObjectSerializer[Location]())
    .registerTypeAdapterFactory(formatterAdapterFactory)
    .registerTypeAdapterFactory(locationAdapterFactory)
    .registerTypeAdapterFactory(eventAdapterFactory)
    .create()

  def serializeEvent(src: Event, typeOfSrc: Type, context: JsonSerializationContext): JsonObject = {
    val event = new JsonObject()
    event.addProperty("type", typeOfSrc.asInstanceOf[Class[_]].getSimpleName)
    event.add("ordinal", context.serialize(src.ordinal))
    src.formatter.foreach(formatter => event.add("formatter", context.serialize(formatter)))
    src.location.foreach(location => event.add("location", context.serialize(location)))
    src.payload.foreach(payload => event.add("payload", context.serialize(payload)))
    event.add("threadName", context.serialize(src.threadName))
    event.add("timeStamp", context.serialize(src.timeStamp))
    event
  }

}
