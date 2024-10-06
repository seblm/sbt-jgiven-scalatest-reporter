import Dependencies.*
import xerial.sbt.Sonatype.*

lazy val commonSettings = Seq(
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  organization := "io.github.seblm",
  publishTo := sonatypePublishToBundle.value,
  scalacOptions += "-deprecation",
  sonatypeProjectHosting :=
    Some(GitHubHosting("seblm", "sbt-jgiven-scalatest-reporter", "sebastian.lemerdy@gmail.com")),
  version := "1.0.4-SNAPSHOT"
)

lazy val `sbt-jgiven-scalatest-reporter` = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    commonSettings,
    scalaVersion := "2.12.20",
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++ Seq("-Dplugin.version=" + version.value) },
    scriptedBufferLog := false
  )
  .aggregate(`jgiven-scalatest-reporter`, `json-scalatest-reporter`)

lazy val `jgiven-scalatest-reporter` = project
  .settings(
    commonSettings,
    scalaVersion := "2.13.15",
    libraryDependencies += `jgiven-core`,
    libraryDependencies += `jgiven-html5-report`,
    libraryDependencies += `log4j-slf4j-impl`,
    libraryDependencies += scalatest,
    libraryDependencies += `slf4j-api`,
    Test / parallelExecution := false
  )
  .dependsOn(`json-scalatest-reporter` % Test)

lazy val `json-scalatest-reporter` = project
  .settings(
    commonSettings,
    scalaVersion := "2.13.15",
    libraryDependencies += gson,
    libraryDependencies += `log4j-slf4j-impl`,
    libraryDependencies += scalatest,
    libraryDependencies += `slf4j-api`,
    Test / parallelExecution := false
  )

console / initialCommands := "import io.github.seblm.scalatest.jgiven._"
