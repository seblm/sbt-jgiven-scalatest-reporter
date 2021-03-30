package io.github.seblm.scalatest.jgiven

class TVSet {
  private var on: Boolean = false
  def isOn: Boolean = on
  def pressPowerButton(): Unit = {
    on = !on
    ()
  }
}
