package com.guizmaii.zeklin.json
import org.scalatest.{FlatSpec, Matchers}

class ComputationSpeedSpec extends FlatSpec with Matchers {

  behavior of "ComputationSpeed"

  it should "convert correctly between ops/s => ops/ms" in {
    ComputationSpeed(1, OpsPerSecond) shouldBe ComputationSpeed(0.001, OpsPerMillisecond)
  }

  it should "convert correctly between ops/ms => ops/s" in {
    ComputationSpeed(1000, OpsPerMillisecond) shouldBe ComputationSpeed(1000000, OpsPerSecond)
  }

  import ComputationSpeedConversions._

  it should "provides a nice 1.`ops/s` function" in {
    1.`ops/s` shouldBe ComputationSpeed(1, OpsPerSecond)
  }

  it should "provides a nice 1.`ops/ms` function" in {
    1.`ops/ms` shouldBe ComputationSpeed(1, OpsPerMillisecond)
  }

}
