package com.guizmaii.zeklin.frontend.js

import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom

object JSMethods {

  final val clickedMessage = "clickedMessage"

}

@JSExportTopLevel("ZeklinApp")
object ZeklinApp {

  def main(): Unit = ()

  @JSExportTopLevel(JSMethods.clickedMessage)
  def clickedMessage(): Unit = dom.window.alert("Button clicked!")

}
