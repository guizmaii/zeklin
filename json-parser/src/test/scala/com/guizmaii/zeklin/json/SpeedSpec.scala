package com.guizmaii.zeklin.json
import org.scalatest.{FlatSpec, Matchers}

class SpeedSpec extends FlatSpec with Matchers {

  behavior of "Speed"

  it should "convert correctly between ops/s => ops/ms" in {
    Speed(1, OpsPerSecond) shouldBe Speed(0.001, OpsPerMillisecond)
  }

  it should "convert correctly between ops/ms => ops/s" in {
    Speed(1000, OpsPerMillisecond) shouldBe Speed(1000000, OpsPerSecond)
  }

}
