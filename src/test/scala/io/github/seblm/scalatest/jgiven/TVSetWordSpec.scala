package io.github.seblm.scalatest.jgiven

import org.scalatest.WordSpec

/** This class allow reporter to run Class.forName("name.lemerdy.sebastian.scalatest.jgiven.TVSetWordSpec") without
  * failure thus using reflection to load annotations.
  */
@TVSetFeature
class TVSetWordSpec extends WordSpec {}
