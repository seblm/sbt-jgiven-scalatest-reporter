import Dependencies._

lazy val commonSettings = Seq(
  organization := "io.github.seblm",
  version := "0.4-SNAPSHOT",
  scalacOptions += "-deprecation",
  publishTo := sonatypePublishToBundle.value
)

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    commonSettings,
    name := "sbt-jgiven-scalatest-reporter",
    scalaVersion := "2.12.13",
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++ Seq("-Dplugin.version=" + version.value) },
    scriptedBufferLog := false
  )
  .aggregate(`jgiven-scalatest-reporter`)

lazy val `jgiven-scalatest-reporter` = (project in file("jgiven-scalatest-reporter"))
  .settings(
    commonSettings,
    name := "jgiven-scalatest-reporter",
    scalaVersion := "2.13.5",
    libraryDependencies += gson,
    libraryDependencies += `jgiven-core`,
    libraryDependencies += `jgiven-html5-report`,
    libraryDependencies += `log4j-slf4j-impl`,
    libraryDependencies += scalatest,
    libraryDependencies += `slf4j-api`,
    Test / parallelExecution := false
  )

console / initialCommands := "import io.github.seblm.scalatest.jgiven._"
