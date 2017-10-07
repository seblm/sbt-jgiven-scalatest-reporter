package name.lemerdy.sebastian.scalatest.jgiven

class TVSet {
  private var on: Boolean = false
  def isOn: Boolean = on
  def pressPowerButton() {
    on = !on
  }
}
