# sbt-jgiven-scalatest-reporter

Generates jgiven html5 reports based on scalatest tests execution.

From:

```scala
package io.github.seblm.scalatest.jgiven

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

@TVSetFeature
class TVSetSpec extends AnyFeatureSpec with GivenWhenThen {

  info("As a TV set owner")
  info("I want to be able to turn the TV on and off")
  info("So I can watch TV when I want")
  info("And save energy when I'm not watching TV")

  Feature("TV power button") {
    Scenario("User presses power button when TV is off") {
      Given("a TV set that is switched off")
      val tv = new TVSet
      assert(!tv.isOn)

      When("the power button is pressed")
      tv.pressPowerButton()

      Then("the TV should switch on")
      assert(tv.isOn)
    }

    Scenario("User presses power button when TV is on") {

      Given("a TV set that is switched on")
      val tv = new TVSet
      tv.pressPowerButton()
      assert(tv.isOn)

      When("the power button is pressed")
      tv.pressPowerButton()

      Then("the TV should switch off")
      assert(!tv.isOn)
    }
  }
}
```

To:

![a JGiven HTML5 report example](doc/JGivenReport.png?raw=true)

## Usage

### With sbt plugin

Add this plugin to your `project/plugins.sbt` file:

```sbt
addSbtPlugin("io.github.seblm" % "sbt-jgiven-scalatest-reporter" % "0.3")
```

When you run your ScalaTest tests as usual with sbt.

Then you can visit `target/jgiven-reports/html/index.html`.

### With a custom reporter through sbt

Add theses settings to your `build.sbt` file:

```sbt
libraryDependencies += "io.github.seblm" %% "jgiven-scalatest-reporter" % "0.3" % Test,
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-C", "io.github.seblm.scalatest.jgiven.JGivenHtml5Reporter"),
```

When you run your ScalaTest tests as usual with sbt.

Then you can visit `target/jgiven-reports/html/index.html`.

### With a custom reporter through your IDE

Add `jgiven-scalatest-reporter.jar` to your test classpath. Add
`-C io.github.seblm.scalatest.jgiven.JGivenHtml5Reporter` to _Program arguments_ of your scalatest launcher.

When you run your ScalaTest tests as usual with your IDE.

Then you can visit `target/jgiven-reports/html/index.html`.

## Development

### Testing

Run `scripted` for [sbt script tests](https://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html).

### Publishing

`sbt publishSigned sonatypeBundleRelease`

According to [documentation][sbt-sonatype], needs gpg with a default signature, gpg-agent running
(`gpg-connect-agent /bye`) and `~/.sbt/1.0/sonatype.sbt` with:

```sbt
credentials += Credentials("Sonatype Nexus Repository Manager",
       "oss.sonatype.org",
       "username",
       "passphrase")
```

[sbt-sonatype]: https://github.com/xerial/sbt-sonatype
