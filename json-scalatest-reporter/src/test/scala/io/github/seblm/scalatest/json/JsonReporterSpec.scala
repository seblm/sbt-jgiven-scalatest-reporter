package io.github.seblm.scalatest.json

import org.scalatest.ConfigMap
import org.scalatest.events._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

import java.nio.file.Paths
import java.time.Instant
import java.util
import scala.io.Source
import scala.jdk.CollectionConverters.CollectionHasAsScala

class JsonReporterSpec extends AnyFlatSpec {

  "JsonReporter" should "serialize and deserialize AlertProvided" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val alertProvided = AlertProvided(
      ordinal = new Ordinal(runStamp = 1),
      message = "some message",
      nameInfo = Some(
        NameInfo(
          suiteName = "TVSetSpec",
          suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
          suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
          testName = None
        )
      ),
      throwable = None,
      formatter = Some(
        IndentedText(
          formattedText = "some formatted text",
          rawText = "some raw text",
          indentationLevel = 1
        )
      ),
      location = Some(TopOfClass(className = "io.github.seblm.scalatest.jgiven.TVSetSpec")),
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(alertProvided)

    then_deserialized_events should contain only alertProvided
  }

  it should "serialize and deserialize DiscoveryCompleted" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val discoveryCompleted = DiscoveryCompleted(
      ordinal = new Ordinal(runStamp = 1),
      duration = Some(34L),
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(discoveryCompleted)

    then_deserialized_events should contain only discoveryCompleted
  }

  it should "serialize and deserialize DiscoveryStarting" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val discoveryStarting = DiscoveryStarting(
      ordinal = new Ordinal(runStamp = 1),
      configMap = new ConfigMap(underlying = Map("config" -> "value")),
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(discoveryStarting)

    then_deserialized_events should contain only discoveryStarting
  }

  it should "serialize and deserialize InfoProvided" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val infoProvided = InfoProvided(
      ordinal = new Ordinal(runStamp = 1),
      message = "a given note",
      nameInfo = Some(
        NameInfo(
          suiteName = "TVSetSpec",
          suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
          suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
          testName = None
        )
      ),
      throwable = None,
      formatter = None,
      location = Some(TopOfMethod(className = "io.github.seblm.scalatest.jgiven.TVSetSpec", methodId = "methodId")),
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(infoProvided)

    then_deserialized_events should contain only infoProvided
  }

  it should "serialize and deserialize MarkupProvided" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val markupProvided = MarkupProvided(
      ordinal = new Ordinal(runStamp = 1),
      text = "# Head\n\nHello *World*_!!!_",
      nameInfo = Some(
        NameInfo(
          suiteName = "TVSetSpec",
          suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
          suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
          testName = None
        )
      ),
      formatter = None,
      location = Some(
        LineInFile(
          23,
          "TVSetSpec.scala",
          Some(
            "Please set the environment variable SCALACTIC_FILL_FILE_PATHNAMES to yes at compile time to enable this feature."
          )
        )
      ),
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(markupProvided)

    then_deserialized_events should contain only markupProvided
  }

  it should "serialize and deserialize NoteProvided" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val noteProvided = NoteProvided(
      ordinal = new Ordinal(runStamp = 1),
      message = "a given note",
      nameInfo = Some(
        NameInfo(
          suiteName = "TVSetSpec",
          suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
          suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
          testName = None
        )
      ),
      throwable = None,
      formatter = None,
      location = None,
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(noteProvided)

    then_deserialized_events should contain only noteProvided
  }

  it should "serialize and deserialize RunAborded" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val runAborted = RunAborted(
      ordinal = new Ordinal(runStamp = 1),
      message = "current run is aborted",
      throwable = None,
      duration = Some(21L),
      summary = Some(
        Summary(
          testsSucceededCount = 3,
          testsFailedCount = 0,
          testsIgnoredCount = 1,
          testsPendingCount = 0,
          testsCanceledCount = 0,
          suitesCompletedCount = 1,
          suitesAbortedCount = 0,
          scopesPendingCount = 0
        )
      ),
      formatter = None,
      location = None,
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(runAborted)

    then_deserialized_events should contain only runAborted
  }

  it should "serialize and deserialize RunCompleted" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val runCompleted = RunCompleted(
      ordinal = new Ordinal(runStamp = 1),
      duration = Some(21L),
      summary = Some(
        Summary(
          testsSucceededCount = 3,
          testsFailedCount = 0,
          testsIgnoredCount = 1,
          testsPendingCount = 0,
          testsCanceledCount = 0,
          suitesCompletedCount = 1,
          suitesAbortedCount = 0,
          scopesPendingCount = 0
        )
      ),
      formatter = None,
      location = None,
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(runCompleted)

    then_deserialized_events should contain only runCompleted
  }

  it should "serialize and deserialize RunStarting" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val runStarting = RunStarting(
      ordinal = new Ordinal(runStamp = 1),
      testCount = 3,
      configMap = ConfigMap("key" -> "value"),
      formatter = None,
      location = None,
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(runStarting)

    then_deserialized_events should contain only runStarting
  }

  it should "serialize and deserialize RunStopped" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val runStopped = RunStopped(
      ordinal = new Ordinal(runStamp = 1),
      duration = Some(21L),
      summary = Some(
        Summary(
          testsSucceededCount = 3,
          testsFailedCount = 0,
          testsIgnoredCount = 1,
          testsPendingCount = 0,
          testsCanceledCount = 0,
          suitesCompletedCount = 1,
          suitesAbortedCount = 0,
          scopesPendingCount = 0
        )
      ),
      formatter = None,
      location = None,
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(runStopped)

    then_deserialized_events should contain only runStopped
  }

  it should "serialize and deserialize ScopeClosed" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val scopeClosed = ScopeClosed(
      ordinal = new Ordinal(runStamp = 1),
      message = "current scope is closed",
      nameInfo = NameInfo(
        suiteName = "TVSetSpec",
        suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
        suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
        testName = None
      ),
      formatter = None,
      location = None,
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(scopeClosed)

    then_deserialized_events should contain only scopeClosed
  }

  it should "serialize and deserialize ScopeOpened" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val scopeOpened = ScopeOpened(
      ordinal = new Ordinal(runStamp = 1),
      message = "current scope is opened",
      nameInfo = NameInfo(
        suiteName = "TVSetSpec",
        suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
        suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
        testName = None
      ),
      formatter = None,
      location = None,
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(scopeOpened)

    then_deserialized_events should contain only scopeOpened
  }

  it should "serialize and deserialize ScopePending" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val scopePending = ScopePending(
      ordinal = new Ordinal(runStamp = 1),
      message = "current scope is Pending",
      nameInfo = NameInfo(
        suiteName = "TVSetSpec",
        suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
        suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
        testName = None
      ),
      formatter = None,
      location = None,
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(scopePending)

    then_deserialized_events should contain only scopePending
  }

  it should "serialize and deserialize SuiteAborded" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val suiteAborted = SuiteAborted(
      ordinal = new Ordinal(runStamp = 1),
      message = "current suite is aborted",
      suiteName = "TVSetSpec",
      suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
      suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      throwable = None,
      duration = Some(21L),
      formatter = None,
      location = None,
      rerunner = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(suiteAborted)

    then_deserialized_events should contain only suiteAborted
  }

  it should "serialize and deserialize SuiteCompleted" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val suiteCompleted = SuiteCompleted(
      ordinal = new Ordinal(runStamp = 1),
      suiteName = "TVSetSpec",
      suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
      suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      duration = Some(21L),
      formatter = None,
      location = None,
      rerunner = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(suiteCompleted)

    then_deserialized_events should contain only suiteCompleted
  }

  it should "serialize and deserialize SuiteStarting" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val suiteStarting = SuiteStarting(
      ordinal = new Ordinal(runStamp = 1),
      suiteName = "TVSetSpec",
      suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
      suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      formatter = None,
      location = None,
      rerunner = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(suiteStarting)

    then_deserialized_events should contain only suiteStarting
  }

  it should "serialize and deserialize TestCanceled" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val testCanceled = TestCanceled(
      ordinal = new Ordinal(runStamp = 1),
      message = "current test is canceled",
      suiteName = "TVSetSpec",
      suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
      suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      testName = "should works",
      testText = "should works as expected",
      recordedEvents = IndexedSeq(
        InfoProvided(
          ordinal = new Ordinal(runStamp = 1),
          message = "a given note",
          nameInfo = Some(
            NameInfo(
              suiteName = "TVSetSpec",
              suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
              suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
              testName = None
            )
          ),
          throwable = None,
          formatter = None,
          location = None,
          payload = None,
          threadName = "pool-5-thread-2",
          timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
        ),
        MarkupProvided(
          ordinal = new Ordinal(runStamp = 1),
          text = "# Head\n\nHello *World*_!!!_",
          nameInfo = Some(
            NameInfo(
              suiteName = "TVSetSpec",
              suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
              suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
              testName = None
            )
          ),
          formatter = None,
          location = None,
          payload = None,
          threadName = "pool-5-thread-2",
          timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
        )
      ),
      throwable = None,
      duration = Some(21L),
      formatter = None,
      location = None,
      rerunner = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(testCanceled)

    then_deserialized_events should contain only testCanceled
  }

  it should "serialize and deserialize TestFailed" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val testFailed = TestFailed(
      ordinal = new Ordinal(runStamp = 1),
      message = "current test is canceled",
      suiteName = "TVSetSpec",
      suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
      suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      testName = "should works",
      testText = "should works as expected",
      recordedEvents = IndexedSeq(
        InfoProvided(
          ordinal = new Ordinal(runStamp = 1),
          message = "a given note",
          nameInfo = Some(
            NameInfo(
              suiteName = "TVSetSpec",
              suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
              suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
              testName = None
            )
          ),
          throwable = None,
          formatter = None,
          location = None,
          payload = None,
          threadName = "pool-5-thread-2",
          timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
        ),
        MarkupProvided(
          ordinal = new Ordinal(runStamp = 1),
          text = "# Head\n\nHello *World*_!!!_",
          nameInfo = Some(
            NameInfo(
              suiteName = "TVSetSpec",
              suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
              suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
              testName = None
            )
          ),
          formatter = None,
          location = None,
          payload = None,
          threadName = "pool-5-thread-2",
          timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
        )
      ),
      analysis = IndexedSeq.empty,
      throwable = None,
      duration = Some(21L),
      formatter = None,
      location = None,
      rerunner = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(testFailed)

    then_deserialized_events should contain only testFailed
  }

  it should "serialize and deserialize TestIgnored" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val testIgnored = TestIgnored(
      ordinal = new Ordinal(runStamp = 1),
      suiteName = "TVSetSpec",
      suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
      suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      testName = "should works",
      testText = "should works as expected",
      formatter = None,
      location = None,
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(testIgnored)

    then_deserialized_events should contain only testIgnored
  }

  it should "serialize and deserialize TestPending" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val testPending = TestPending(
      ordinal = new Ordinal(runStamp = 1),
      suiteName = "TVSetSpec",
      suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
      suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      testName = "should works",
      testText = "should works as expected",
      recordedEvents = IndexedSeq(
        InfoProvided(
          ordinal = new Ordinal(runStamp = 1),
          message = "a given note",
          nameInfo = Some(
            NameInfo(
              suiteName = "TVSetSpec",
              suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
              suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
              testName = None
            )
          ),
          throwable = None,
          formatter = None,
          location = None,
          payload = None,
          threadName = "pool-5-thread-2",
          timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
        ),
        MarkupProvided(
          ordinal = new Ordinal(runStamp = 1),
          text = "# Head\n\nHello *World*_!!!_",
          nameInfo = Some(
            NameInfo(
              suiteName = "TVSetSpec",
              suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
              suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
              testName = None
            )
          ),
          formatter = None,
          location = None,
          payload = None,
          threadName = "pool-5-thread-2",
          timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
        )
      ),
      duration = Some(21L),
      formatter = None,
      location = None,
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(testPending)

    then_deserialized_events should contain only testPending
  }

  it should "serialize and deserialize TestStarting" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val testStarting = TestStarting(
      ordinal = new Ordinal(runStamp = 1),
      suiteName = "TVSetSpec",
      suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
      suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      testName = "should works",
      testText = "should works as expected",
      formatter = None,
      location = None,
      rerunner = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(testStarting)

    then_deserialized_events should contain only testStarting
  }

  it should "serialize and deserialize TestSucceeded" in {
    implicit val reporter: JsonReporter = new JsonReporter()
    val testSucceeded = TestSucceeded(
      ordinal = new Ordinal(runStamp = 1),
      suiteName = "TVSetSpec",
      suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
      suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      testName = "should works",
      testText = "should works as expected",
      recordedEvents = IndexedSeq(
        InfoProvided(
          ordinal = new Ordinal(runStamp = 1),
          message = "a given note",
          nameInfo = Some(
            NameInfo(
              suiteName = "TVSetSpec",
              suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
              suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
              testName = None
            )
          ),
          throwable = None,
          formatter = None,
          location = None,
          payload = None,
          threadName = "pool-5-thread-2",
          timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
        ),
        MarkupProvided(
          ordinal = new Ordinal(runStamp = 1),
          text = "# Head\n\nHello *World*_!!!_",
          nameInfo = Some(
            NameInfo(
              suiteName = "TVSetSpec",
              suiteId = "io.github.seblm.scalatest.jgiven.TVSetSpec",
              suiteClassName = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
              testName = None
            )
          ),
          formatter = None,
          location = None,
          payload = None,
          threadName = "pool-5-thread-2",
          timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
        )
      ),
      duration = Some(21L),
      formatter = None,
      location = None,
      rerunner = Some("io.github.seblm.scalatest.jgiven.TVSetSpec"),
      payload = None,
      threadName = "pool-5-thread-2",
      timeStamp = Instant.parse("2019-06-25T21:55:00Z").toEpochMilli
    )

    reporter(testSucceeded)

    then_deserialized_events should contain only testSucceeded
  }

  private def then_deserialized_events(implicit reporter: JsonReporter): List[Event] = {
    reporter.dispose()
    val source = Source.fromFile(Paths.get("target", "scalatest-events.json").toFile)
    val events: List[Event] = GsonEventSupport.gson
      .fromJson(source.getLines().mkString, GsonEventSupport.collectionOfEventsType)
      .asInstanceOf[util.Collection[Event]]
      .asScala
      .toList
    source.close()
    events
  }

}
