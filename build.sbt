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
    libraryDependencies += `sbt-`,
    libraryDependencies += `sbt-actions`,
    libraryDependencies += `sbt-collections`,
    libraryDependencies += `sbt-core-macros`,
    libraryDependencies += `sbt-librarymanagement-core`,
    libraryDependencies += `sbt-main`,
    libraryDependencies += `sbt-main-settings`,
    libraryDependencies += `sbt-task-system`,
    libraryDependencies += `sbt-testing`,
    libraryDependencies += `sbt-util-position`,
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++ Seq("-Dplugin.version=" + version.value) },
    scriptedBufferLog := false
  )
  .aggregate(`jgiven-scalatest-reporter`, `json-scalatest-reporter`)

lazy val `jgiven-scalatest-reporter` = project
  .settings(
    commonSettings,
    scalaVersion := "2.13.18",
    libraryDependencies += `jgiven-core`,
    libraryDependencies += `jgiven-html5-report`,
    libraryDependencies += `log4j-slf4j-impl`,
    libraryDependencies += `scalatest-core`,
    libraryDependencies += `scalatest-featurespec`,
    libraryDependencies += `scalatest-shouldmatchers`,
    libraryDependencies += `scalatest-wordspec`,
    Test / parallelExecution := false
  )
  .dependsOn(`json-scalatest-reporter` % Test)

lazy val `json-scalatest-reporter` = project
  .settings(
    commonSettings,
    scalaVersion := "2.13.18",
    libraryDependencies += gson,
    libraryDependencies += `log4j-slf4j-impl`,
    libraryDependencies += `scalatest-core`,
    libraryDependencies += `scalatest-flatspec`,
    libraryDependencies += `scalatest-shouldmatchers`,
    libraryDependencies += `slf4j-api`,
    Test / parallelExecution := false
  )

console / initialCommands := "import io.github.seblm.scalatest.jgiven._"
