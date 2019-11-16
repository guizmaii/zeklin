package com.guizmaii.zeklin.frontend

import org.scalajs.dom._
import outwatch.dom._
import outwatch.dom.dsl._

class OutwatchSpec extends JSDomSpec {

  "You" should "probably add some tests" in {

    val message = "Hello World!"
    OutWatch.renderInto("#app", h1(message)).unsafeRunSync()

    document.body.innerHTML.contains(message) shouldBe true
  }
}
