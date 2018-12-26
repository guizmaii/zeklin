package com.guizmaii.zeklin.json
import org.scalatest.{FlatSpec, Matchers}

class ComputationSpeedSpec extends FlatSpec with Matchers {

  behavior of "ComputationSpeed"

  // ops/s => ops/ms => ops/ns

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

  // s/op => ms/op => ns/op

  it should "convert correctly between s/op => ms/op" in {
    ComputationSpeed(1, SecondPerOp) shouldBe ComputationSpeed(1.0e-3, MilliPerOp)
  }
  it should "convert correctly between s/op => ns/op" in {
    ComputationSpeed(1, SecondPerOp) shouldBe ComputationSpeed(1.0e-9, NanoPerOp)
  }
  it should "convert correctly between ms/op => s/op" in {
    ComputationSpeed(1, MilliPerOp) shouldBe ComputationSpeed(1.0e3, SecondPerOp)
  }
  it should "convert correctly between ns/op => ms/op" in {
    ComputationSpeed(1, NanoPerOp) shouldBe ComputationSpeed(1.0e6, MilliPerOp)
  }
  it should "convert correctly between ns/op => s/op" in {
    ComputationSpeed(1, NanoPerOp) shouldBe ComputationSpeed(1.0e9, SecondPerOp)
  }

  // ops/s => s/op

  it should "1.0e3 ops/s ==> 1 ms/op" in {
    ComputationSpeed(1.0e3, OpsPerSecond) shouldBe ComputationSpeed(1, MilliPerOp)
  }
  it should "1.0e9 ops/s ==> 1 ns/op" in {
    ComputationSpeed(1.0e9, OpsPerSecond) shouldBe ComputationSpeed(1, NanoPerOp)
  }
  it should "1.0e6 ops/ms ==> 1 ns/op" in {
    ComputationSpeed(1.0e6, OpsPerMilli) shouldBe ComputationSpeed(1, NanoPerOp)
  }
  it should "1 ops/ns ==> 1 ns/op" in {
    ComputationSpeed(1, OpsPerNano) shouldBe ComputationSpeed(1, NanoPerOp)
  }

  // s/op => ops/s

  it should "1 ns/op ==> 1 ops/ns" in {
    ComputationSpeed(1, NanoPerOp) shouldBe ComputationSpeed(1, OpsPerNano)
  }
  it should "1 ns/op ==> 1.0e6 ops/ms" in {
    ComputationSpeed(1, NanoPerOp) shouldBe ComputationSpeed(1.0e6, OpsPerMilli)
  }
  it should "1 ns/op ==> 1.0e9 ops/s" in {
    ComputationSpeed(1, NanoPerOp) shouldBe ComputationSpeed(1.0e9, OpsPerSecond)
  }
  it should "1 ms/op ==> 1 ops/ms" in {
    ComputationSpeed(1, MilliPerOp) shouldBe ComputationSpeed(1, OpsPerMilli)
  }
  it should "1 ms/op ==> 1.0e3 ops/s" in {
    ComputationSpeed(1, MilliPerOp) shouldBe ComputationSpeed(1.0e3, OpsPerSecond)
  }
  it should "1 s/op ==> 1 ops/s" in {
    ComputationSpeed(1, SecondPerOp) shouldBe ComputationSpeed(1, OpsPerSecond)
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

  it should "provides a nice 1.`s/op` function" in {
    1.`s/op` shouldBe ComputationSpeed(1, SecondPerOp)
  }

  it should "provides a nice 1.`ms/op` function" in {
    1.`ms/op` shouldBe ComputationSpeed(1, MilliPerOp)
  }

  it should "provides a nice 1.`ns/op` function" in {
    1.`ns/op` shouldBe ComputationSpeed(1, NanoPerOp)
  }
}
