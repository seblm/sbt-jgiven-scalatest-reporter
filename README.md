# sbt-jgiven-scalatest-reporter [![Build Status](https://travis-ci.org/seblm/sbt-jgiven-scalatest-reporter.svg?branch=master)](https://travis-ci.org/seblm/sbt-jgiven-scalatest-reporter)

Sbt plugin that generates jgiven html5 reports based on scalatest tests execution.

## Usage

This plugin requires sbt 1.0.0+

## Example

Here is what you will get starting from scalatest FeatureSpec basic example:

![Example of produced report](/../assets/example-screenshot.png?raw=true)

### Testing

Run `scripted` for [sbt script tests](https://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html).

### Publishing

`sbt publish`

### Backlog

 1. requirement: plugin must serialize scalatest events into json file: tests could then use it as input
 1. fix: plugin works pretty well with FeatureSpec but description is partial with at least WordSpec
 1. improvement: display test execution time
 1. improvement: migrate to scala 2.13
