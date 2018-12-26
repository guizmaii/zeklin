package com.guizmaii.zeklin.json

import cats.scalatest.ValidatedMatchers
import org.scalatest.{FlatSpec, Matchers}

import scala.io.{Codec, Source}

class ParserSpec extends FlatSpec with Matchers with ValidatedMatchers {

  def raw(fileName: String): String = Source.fromResource(fileName)(Codec.UTF8).getLines().mkString

  behavior of "Parser"

  it should "parse aaa-benchs.json" in {
    JmhJsonResultParser.parse(raw("aaa-benchs.json")) should be(valid)
  }

  it should "parse geoflram-benchs.json" in {
    JmhJsonResultParser.parse(raw("geoflram-benchs.json")) should be(valid)
  }

}
