package com.guizmaii.zeklin.jmh.json

import io.circe.{ Decoder, Encoder, Json }
import squants.{ Dimension, MetricSystem, PrimaryUnit, Quantity, SiUnit, UnitConverter, UnitOfMeasure }

/**
 * TODO: Find a better name ?
 */
final class ComputationSpeed(override final val value: Double, override final val unit: ComputationSpeedUnit)
    extends Quantity[ComputationSpeed] {
  override final val dimension: Dimension[ComputationSpeed] = ComputationSpeed
}

object ComputationSpeed extends Dimension[ComputationSpeed] {
  final def apply[N](n: N, unit: ComputationSpeedUnit)(implicit num: Numeric[N]): ComputationSpeed =
    new ComputationSpeed(num.toDouble(n), unit)

  override final val name: String                                                  = "ComputationSpeed"
  override final val primaryUnit: UnitOfMeasure[ComputationSpeed] with PrimaryUnit = OpsPerSecond
  override final val siUnit: UnitOfMeasure[ComputationSpeed] with SiUnit           = OpsPerSecond
  override final val units: Set[UnitOfMeasure[ComputationSpeed]] =
    Set(OpsPerSecond, OpsPerMilli, OpsPerNano, SecondPerOp, MilliPerOp, NanoPerOp)
}

object ComputationSpeedConversions {
  implicit final class ComputationSpeedOps[N](private final val n: N) extends AnyVal {
    final def `ops/s`(implicit N: Numeric[N]): ComputationSpeed  = ComputationSpeed(n, OpsPerSecond)
    final def `ops/ms`(implicit N: Numeric[N]): ComputationSpeed = ComputationSpeed(n, OpsPerMilli)
    final def `ops/ns`(implicit N: Numeric[N]): ComputationSpeed = ComputationSpeed(n, OpsPerNano)
    final def `s/op`(implicit N: Numeric[N]): ComputationSpeed   = ComputationSpeed(n, SecondPerOp)
    final def `ms/op`(implicit N: Numeric[N]): ComputationSpeed  = ComputationSpeed(n, MilliPerOp)
    final def `ns/op`(implicit N: Numeric[N]): ComputationSpeed  = ComputationSpeed(n, NanoPerOp)
  }
}

sealed trait ComputationSpeedUnit extends UnitOfMeasure[ComputationSpeed] with UnitConverter {
  override final def apply[N](n: N)(implicit num: Numeric[N]): ComputationSpeed = ComputationSpeed(n, this)
}
object ComputationSpeedUnit {
  implicit final val encoder: Encoder[ComputationSpeedUnit] = (v: ComputationSpeedUnit) => Json.fromString(v.symbol)
  implicit final val decoder: Decoder[ComputationSpeedUnit] =
    Decoder.decodeString.emap { s =>
      ComputationSpeed.symbolToUnit(s) match {
        case Some(value) => Right(value.asInstanceOf[ComputationSpeedUnit])
        case None        => Left(s"Unsupported unit of measure: $s")
      }
    }
}

final case object OpsPerSecond extends ComputationSpeedUnit with PrimaryUnit with SiUnit {
  override final val symbol: String = "ops/s"
}

final case object OpsPerMilli extends ComputationSpeedUnit {
  override final val symbol: String                     = "ops/ms"
  override protected final val conversionFactor: Double = MetricSystem.Kilo
}

final case object OpsPerNano extends ComputationSpeedUnit {
  override final val symbol: String                     = "ops/ns"
  override protected final val conversionFactor: Double = MetricSystem.Giga
}

final case object SecondPerOp extends ComputationSpeedUnit {
  override final val symbol: String                     = "s/op"
  override protected final val conversionFactor: Double = 1
}

final case object MilliPerOp extends ComputationSpeedUnit {
  override final val symbol: String                     = "ms/op"
  override protected final val conversionFactor: Double = MetricSystem.Kilo
}

final case object NanoPerOp extends ComputationSpeedUnit {
  override final val symbol: String                     = "ns/op"
  override protected final val conversionFactor: Double = MetricSystem.Giga
}
