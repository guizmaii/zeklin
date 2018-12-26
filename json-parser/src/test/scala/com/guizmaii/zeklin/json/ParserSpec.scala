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

  it should "parse correctly the `ComputationSpeed`s from aaa-benchs.json" in {
    val res = JmhJsonResultParser.parse(raw("aaa-benchs.json")).valueOr(null)

    res should not be null

    import ComputationSpeedConversions._

    res.head.primaryMetric.score shouldBe 56.37251605275668.`ops/ms`
    res.head.primaryMetric.scoreError shouldBe 1.493234981857803.`ops/ms`

    res.head.primaryMetric.scoreConfidence(0) shouldBe 54.87928107089888.`ops/ms`
    res.head.primaryMetric.scoreConfidence(1) shouldBe 57.86575103461448.`ops/ms`

    res.head.primaryMetric.scorePercentiles.`0.0` shouldBe 54.36184379994052.`ops/ms`
    res.head.primaryMetric.scorePercentiles.`50.0` shouldBe 56.56633939782403.`ops/ms`
    res.head.primaryMetric.scorePercentiles.`90.0` shouldBe 57.74905742825697.`ops/ms`
    res.head.primaryMetric.scorePercentiles.`95.0` shouldBe 57.81256474054073.`ops/ms`
    res.head.primaryMetric.scorePercentiles.`99.0` shouldBe 57.81256474054073.`ops/ms`
    res.head.primaryMetric.scorePercentiles.`99.9` shouldBe 57.81256474054073.`ops/ms`
    res.head.primaryMetric.scorePercentiles.`99.99` shouldBe 57.81256474054073.`ops/ms`
    res.head.primaryMetric.scorePercentiles.`99.999` shouldBe 57.81256474054073.`ops/ms`
    res.head.primaryMetric.scorePercentiles.`99.9999` shouldBe 57.81256474054073.`ops/ms`
    res.head.primaryMetric.scorePercentiles.`100.0`.value shouldBe 57.81256474054073

    res.head.primaryMetric.scoreUnit shouldBe OpsPerMilli

    res.head.primaryMetric.rawData(0)(0) shouldBe 54.36184379994052.`ops/ms`
    res.head.primaryMetric.rawData(0)(1) shouldBe 55.66235539884661.`ops/ms`
    res.head.primaryMetric.rawData(0)(2) shouldBe 57.09436514946753.`ops/ms`
    res.head.primaryMetric.rawData(0)(3) shouldBe 55.80167051108505.`ops/ms`
    res.head.primaryMetric.rawData(0)(4) shouldBe 56.62763605702134.`ops/ms`
    res.head.primaryMetric.rawData(0)(5) shouldBe 56.87925808378404.`ops/ms`
    res.head.primaryMetric.rawData(0)(6) shouldBe 56.50504273862672.`ops/ms`
    res.head.primaryMetric.rawData(0)(7) shouldBe 55.80293243055118.`ops/ms`
    res.head.primaryMetric.rawData(0)(8) shouldBe 57.81256474054073.`ops/ms`
    res.head.primaryMetric.rawData(0)(9) shouldBe 57.17749161770308.`ops/ms`
  }

  it should "parse correctly the `ComputationSpeed`s from geoflram-benchs.json" in {
    val res = JmhJsonResultParser.parse(raw("geoflram-benchs.json")).valueOr(null)

    res should not be null

    import ComputationSpeedConversions._

    res.last.primaryMetric.score shouldBe 73.87191376163163.`ns/op`
    res.last.primaryMetric.scoreError shouldBe 6.0230957854910425.`ns/op`

    res.last.primaryMetric.scoreConfidence(0) shouldBe 67.8488179761406.`ns/op`
    res.last.primaryMetric.scoreConfidence(1) shouldBe 79.89500954712267.`ns/op`

    res.last.primaryMetric.scorePercentiles.`0.0` shouldBe 65.32372352606266.`ns/op`
    res.last.primaryMetric.scorePercentiles.`50.0` shouldBe 72.3273356501524.`ns/op`
    res.last.primaryMetric.scorePercentiles.`90.0` shouldBe 86.54116108319077.`ns/op`
    res.last.primaryMetric.scorePercentiles.`95.0` shouldBe 87.53293041821553.`ns/op`
    res.last.primaryMetric.scorePercentiles.`99.0` shouldBe 87.57908969071674.`ns/op`
    res.last.primaryMetric.scorePercentiles.`99.9` shouldBe 87.57908969071674.`ns/op`
    res.last.primaryMetric.scorePercentiles.`99.99` shouldBe 87.57908969071674.`ns/op`
    res.last.primaryMetric.scorePercentiles.`99.999` shouldBe 87.57908969071674.`ns/op`
    res.last.primaryMetric.scorePercentiles.`99.9999` shouldBe 87.57908969071674.`ns/op`
    res.last.primaryMetric.scorePercentiles.`100.0`.value shouldBe 87.57908969071674

    res.last.primaryMetric.scoreUnit shouldBe NanoPerOp

    res.last.primaryMetric.rawData(0)(0) shouldBe 65.53301876211052.`ns/op`
    res.last.primaryMetric.rawData(0)(1) shouldBe 65.32372352606266.`ns/op`
    res.last.primaryMetric.rawData(0)(2) shouldBe 65.93572357348138.`ns/op`
    res.last.primaryMetric.rawData(0)(3) shouldBe 80.91689775078306.`ns/op`
    res.last.primaryMetric.rawData(0)(4) shouldBe 86.6559042406924.`ns/op`
    res.last.primaryMetric.rawData(0)(5) shouldBe 67.67257663914341.`ns/op`
    res.last.primaryMetric.rawData(0)(6) shouldBe 85.50847266567615.`ns/op`
    res.last.primaryMetric.rawData(0)(7) shouldBe 68.40210882357958.`ns/op`
    res.last.primaryMetric.rawData(0)(8) shouldBe 76.78097113563642.`ns/op`
    res.last.primaryMetric.rawData(0)(9) shouldBe 80.08433737637792.`ns/op`

    res.last.primaryMetric.rawData(1)(0) shouldBe 68.15861052836807.`ns/op`
    res.last.primaryMetric.rawData(1)(1) shouldBe 73.76312049687354.`ns/op`
    res.last.primaryMetric.rawData(1)(2) shouldBe 71.97045467027753.`ns/op`
    res.last.primaryMetric.rawData(1)(3) shouldBe 72.68421663002728.`ns/op`
    res.last.primaryMetric.rawData(1)(4) shouldBe 71.23585323564252.`ns/op`
    res.last.primaryMetric.rawData(1)(5) shouldBe 71.78191995465602.`ns/op`
    res.last.primaryMetric.rawData(1)(6) shouldBe 72.85279558126965.`ns/op`
    res.last.primaryMetric.rawData(1)(7) shouldBe 71.3588671147095.`ns/op`
    res.last.primaryMetric.rawData(1)(8) shouldBe 87.57908969071674.`ns/op`
    res.last.primaryMetric.rawData(1)(9) shouldBe 73.23961283654843.`ns/op`
  }

}
