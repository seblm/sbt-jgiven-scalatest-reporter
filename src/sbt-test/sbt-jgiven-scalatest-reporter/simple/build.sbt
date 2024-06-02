version := "0.1"
scalaVersion := "2.13.14"
scalacOptions += "-deprecation"

import Dependencies._

libraryDependencies += scalatest % Test
libraryDependencies += `log4j-slf4j-impl` % Test
