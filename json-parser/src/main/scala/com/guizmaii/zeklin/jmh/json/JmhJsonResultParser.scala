package com.guizmaii.zeklin.jmh.json

import cats.data.ValidatedNel
import io.circe

object JmhJsonResultAST {

  import io.circe._
  import io.circe.generic.semiauto._

  final case class Params(
    heightCount: Int,
    lengthCount: Int,
    widthCount: Int
  )

  final case class ScorePercentiles(
    `0.0`: ComputationSpeed,
    `50.0`: ComputationSpeed,
    `90.0`: ComputationSpeed,
    `95.0`: ComputationSpeed,
    `99.0`: ComputationSpeed,
    `99.9`: ComputationSpeed,
    `99.99`: ComputationSpeed,
    `99.999`: ComputationSpeed,
    `99.9999`: ComputationSpeed,
    `100.0`: ComputationSpeed
  )

  final case class Score(
    score: ComputationSpeed,
    scoreError: ComputationSpeed,
    scoreConfidence: Array[ComputationSpeed],
    scorePercentiles: ScorePercentiles,
    scoreUnit: ComputationSpeedUnit,
    rawData: Array[Array[ComputationSpeed]]
  )

  final case class JmhResultAST(
    jmhVersion: String,
    benchmark: String,
    mode: String,
    threads: Int,
    forks: Int,
    jvm: String,
    jvmArgs: Array[String],
    jdkVersion: String,
    vmName: String,
    vmVersion: String,
    warmupIterations: Int,
    warmupTime: String,
    warmupBatchSize: Int,
    measurementIterations: Int,
    measurementTime: String,
    measurementBatchSize: Int,
    params: Option[Params],
    primaryMetric: Score,
    secondaryMetrics: JsonObject // TODO: Find what's inside !
  )

  object Params {
    implicit final val decoder: Decoder[Params] = deriveDecoder
  }

  object Score {
    implicit final val decoder: Decoder[Score] = (c: HCursor) =>
      for {
        score           <- c.downField("score").as[Double]
        scoreError      <- c.downField("scoreError").as[Double]
        scoreConfidence <- c.downField("scoreConfidence").as[Array[Double]]
        cc              = c.downField("scorePercentiles")
        `0`             <- cc.downField("0.0").as[Double]
        `50`            <- cc.downField("50.0").as[Double]
        `90`            <- cc.downField("90.0").as[Double]
        `95`            <- cc.downField("95.0").as[Double]
        `99`            <- cc.downField("99.0").as[Double]
        `99.9`          <- cc.downField("99.9").as[Double]
        `99.99`         <- cc.downField("99.99").as[Double]
        `99.999`        <- cc.downField("99.999").as[Double]
        `99.9999`       <- cc.downField("99.9999").as[Double]
        `100`           <- cc.downField("100.0").as[Double]
        unit            <- c.downField("scoreUnit").as[ComputationSpeedUnit]
        rawData         <- c.downField("rawData").as[Array[Array[Double]]]
      } yield Score(
        score = unit.apply(score),
        scoreError = unit.apply(scoreError),
        scoreConfidence = scoreConfidence.map((d: Double) => unit.apply(d)), // Without the `Double` type annotation, scalac doesn't infer it...
        scorePercentiles = ScorePercentiles(
          `0.0` = unit.apply(`0`),
          `50.0` = unit.apply(`50`),
          `90.0` = unit.apply(`90`),
          `95.0` = unit.apply(`95`),
          `99.0` = unit.apply(`99`),
          `99.9` = unit.apply(`99.9`),
          `99.99` = unit.apply(`99.99`),
          `99.999` = unit.apply(`99.999`),
          `99.9999` = unit.apply(`99.9999`),
          `100.0` = unit.apply(`100`)
        ),
        scoreUnit = unit,
        rawData = rawData.map(_.map((d: Double) => unit.apply(d))) // Without the `Double` type annotation, scalac doesn't infer it...
      )
  }

  object JmhResultAST {
    implicit final val decoder: Decoder[JmhResultAST] = deriveDecoder
  }
}

object JmhJsonResultParser {

  import JmhJsonResultAST._
  import io.circe.parser._

  final def parse(string: String): ValidatedNel[circe.Error, Array[JmhResultAST]] =
    decodeAccumulating[Array[JmhResultAST]](string)

}
