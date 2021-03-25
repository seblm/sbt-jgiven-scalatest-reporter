import Dependencies._

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-jgiven-scalatest-reporter",
    organization := "io.github.seblm",
    version := "0.2",
    publishTo := sonatypePublishToBundle.value,
    scalaVersion := "2.12.13",
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++ Seq("-Dplugin.version=" + version.value) },
    scriptedBufferLog := false,
    libraryDependencies += gson,
    libraryDependencies += `jgiven-core`,
    libraryDependencies += `jgiven-html5-report`,
    libraryDependencies += scalatest,
    libraryDependencies += `slf4j-api`
  )

initialCommands in console := "import io.github.seblm.scalatest.jgiven._"
