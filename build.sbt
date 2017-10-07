name := """sbt-jgiven-scalatest-reporter"""
organization := "name.lemerdy.sebastian"
version := "0.1-SNAPSHOT"

sbtPlugin := true

bintrayPackageLabels := Seq("sbt","plugin")
bintrayVcsUrl := Some("""git@github.com:seblm/sbt-jgiven-scalatest-reporter.git""")

initialCommands in console := """import name.lemerdy.sebastian.scalatest.jgiven._"""

scriptedLaunchOpts ++=
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)

scriptedBufferLog := false
