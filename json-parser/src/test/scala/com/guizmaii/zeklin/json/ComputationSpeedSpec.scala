package com.guizmaii.zeklin.json
import org.scalatest.{FlatSpec, Matchers}

class ComputationSpeedSpec extends FlatSpec with Matchers {

  behavior of "ComputationSpeed"

  it should "convert correctly between ops/s => ops/ms" in {
    ComputationSpeed(1, OpsPerSecond) shouldBe ComputationSpeed(1.0e-3, OpsPerMilli)
  }
  it should "convert correctly between ops/s => ops/ns" in {
    ComputationSpeed(1, OpsPerSecond) shouldBe ComputationSpeed(1.0e-9, OpsPerNano)
  }
  it should "convert correctly between ops/ms => ops/s" in {
    ComputationSpeed(1, OpsPerMilli) shouldBe ComputationSpeed(1.0e3, OpsPerSecond)
  }
  it should "convert correctly between ops/ns => ops/ms" in {
    ComputationSpeed(1, OpsPerNano) shouldBe ComputationSpeed(1.0e6, OpsPerMilli)
  }
  it should "convert correctly between ops/ns => ops/s" in {
    ComputationSpeed(1, OpsPerNano) shouldBe ComputationSpeed(1.0e9, OpsPerSecond)
  }

  import ComputationSpeedConversions._

  it should "provides a nice 1.`ops/s` function" in {
    1.`ops/s` shouldBe ComputationSpeed(1, OpsPerSecond)
  }

  it should "provides a nice 1.`ops/ms` function" in {
    1.`ops/ms` shouldBe ComputationSpeed(1, OpsPerMilli)
  }

  it should "provides a nice 1.`ops/ns` function" in {
    1.`ops/ns` shouldBe ComputationSpeed(1, OpsPerNano)
  }

}
