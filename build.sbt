import Dependencies._

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-jgiven-scalatest-reporter",
    organization := "name.lemerdy.sebastian",
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

bintrayPackageLabels := Seq("sbt", "plugin")
bintrayVcsUrl := Some("""git@github.com:seblm/sbt-jgiven-scalatest-reporter.git""")

initialCommands in console := """import name.lemerdy.sebastian.scalatest.jgiven._"""
