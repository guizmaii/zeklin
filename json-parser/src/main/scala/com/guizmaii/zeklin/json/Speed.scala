package com.guizmaii.zeklin.json
import squants.{Dimension, MetricSystem, PrimaryUnit, Quantity, SiUnit, UnitConverter, UnitOfMeasure}

/**
  * TODO: Find a better name ?
  */
final class Speed(val value: Double, val unit: SpeedUnit) extends Quantity[Speed] {
  override val dimension: Dimension[Speed] = Speed
}

object Speed extends Dimension[Speed] {
  def apply[N](n: N, unit: SpeedUnit)(implicit num: Numeric[N]): Speed = new Speed(num.toDouble(n), unit)

  override final val name: String                                       = "Speed"
  override final val primaryUnit: UnitOfMeasure[Speed] with PrimaryUnit = OpsPerSecond
  override final val siUnit: UnitOfMeasure[Speed] with SiUnit           = OpsPerSecond
  override final val units: Set[UnitOfMeasure[Speed]] =
    Set(OpsPerSecond, OpsPerMillisecond)
}

sealed trait SpeedUnit extends UnitOfMeasure[Speed] with UnitConverter {
  override def apply[N](n: N)(implicit num: Numeric[N]): Speed = new Speed(num.toDouble(n), this)
}

final case object OpsPerSecond extends SpeedUnit with PrimaryUnit with SiUnit {
  override final val symbol: String = "ops/s"
}

final case object OpsPerMillisecond extends SpeedUnit {
  override final val symbol: String                     = "ops/ms"
  override protected final val conversionFactor: Double = MetricSystem.Kilo
}
