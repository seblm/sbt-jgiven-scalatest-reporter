import Dependencies._
import xerial.sbt.Sonatype._

lazy val commonSettings = Seq(
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  organization := "io.github.seblm",
  publishTo := sonatypePublishToBundle.value,
  scalacOptions += "-deprecation",
  sonatypeProjectHosting :=
    Some(GitHubHosting("seblm", "sbt-jgiven-scalatest-reporter", "sebastian.lemerdy@gmail.com")),
  version := "0.5-SNAPSHOT"
)

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    commonSettings,
    name := "sbt-jgiven-scalatest-reporter",
    scalaVersion := "2.12.14",
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++ Seq("-Dplugin.version=" + version.value) },
    scriptedBufferLog := false
  )
  .aggregate(`jgiven-scalatest-reporter`, `json-scalatest-reporter`)

lazy val `jgiven-scalatest-reporter` = (project in file("jgiven-scalatest-reporter"))
  .settings(
    commonSettings,
    scalaVersion := "2.13.6",
    libraryDependencies += `jgiven-core`,
    libraryDependencies += `jgiven-html5-report`,
    libraryDependencies += `log4j-slf4j-impl`,
    libraryDependencies += scalatest,
    libraryDependencies += `slf4j-api`,
    Test / parallelExecution := false
  )
  .dependsOn(`json-scalatest-reporter` % Test)

lazy val `json-scalatest-reporter` = (project in file("json-scalatest-reporter"))
  .settings(
    commonSettings,
    scalaVersion := "2.13.6",
    libraryDependencies += gson,
    libraryDependencies += `log4j-slf4j-impl`,
    libraryDependencies += scalatest,
    libraryDependencies += `slf4j-api`,
    Test / parallelExecution := false
  )

console / initialCommands := "import io.github.seblm.scalatest.jgiven._"
