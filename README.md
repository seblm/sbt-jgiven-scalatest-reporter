[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.seblm/sbt-jgiven-scalatest-reporter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.seblm/sbt-jgiven-scalatest-reporter)

Generates jgiven html5 reports based on ScalaTest tests execution.

## Example

Here is what you will get starting from
[ScalaTest FeatureSpec basic example](https://www.scalatest.org/at_a_glance/FeatureSpec):

![Example of produced report](/../assets/example-screenshot.png?raw=true)

## Usage

### With sbt plugin

Add this plugin to your `project/plugins.sbt` file:

```sbt
addSbtPlugin("io.github.seblm" % "sbt-jgiven-scalatest-reporter" % "0.4")
```

When you run your ScalaTest tests as usual with sbt.

Then you can visit `target/jgiven-reports/html/index.html`.

### With a custom reporter through sbt

Add theses settings to your `build.sbt` file:

```sbt
libraryDependencies += "io.github.seblm" %% "jgiven-scalatest-reporter" % "0.4" % Test,
Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-C", "io.github.seblm.scalatest.jgiven.JGivenHtml5Reporter"),
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

According to [documentation](https://github.com/xerial/sbt-sonatype), needs gpg with a default signature, gpg-agent
running (`gpg-connect-agent /bye`) and `~/.sbt/1.0/sonatype.sbt` with:

```sbt
credentials += Credentials("Sonatype Nexus Repository Manager",
       "oss.sonatype.org",
       "username",
       "passphrase")
```
