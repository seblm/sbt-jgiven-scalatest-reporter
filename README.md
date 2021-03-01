# sbt-jgiven-scalatest-reporter

Sbt plugin that generates jgiven html5 reports based on scalatest tests execution.

## Usage

This plugin requires sbt 1.0.0+

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

### Backlog

 1. improvement: migrate to scala 2.13

[sbt-sonatype]: https://github.com/xerial/sbt-sonatype
