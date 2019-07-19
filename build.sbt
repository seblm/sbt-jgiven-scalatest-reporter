import Dependencies._

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-jgiven-scalatest-reporter",
    organization := "fr.fpe",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,
    libraryDependencies += gson,
    libraryDependencies += `jgiven-core`,
    libraryDependencies += `jgiven-html5-report`,
    libraryDependencies += scalatest,
    libraryDependencies += `slf4j-api`,
  )

initialCommands in console := """import fr.fpe.scalatest.jgiven._"""
