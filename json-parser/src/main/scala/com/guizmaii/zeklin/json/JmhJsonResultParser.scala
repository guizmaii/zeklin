package com.guizmaii.zeklin.json
import cats.data.ValidatedNel
import io.circe

object JmhJsonResultAST {

  import io.circe._
  import io.circe.generic.semiauto._

  final case class Params(
      heightCount: String,
      lengthCount: String,
      widthCount: String
  )

  final case class ScorePercentiles(
      `0.0`: Double,
      `50.0`: Double,
      `90.0`: Double,
      `95.0`: Double,
      `99.0`: Double,
      `99.9`: Double,
      `99.99`: Double,
      `99.999`: Double,
      `99.9999`: Double,
      `100.0`: Double
  )

  final case class Score(
      score: Double,
      scoreError: Double,
      scoreConfidence: Array[Double],
      scorePercentiles: ScorePercentiles,
      scoreUnit: String,
      rawData: Array[Array[Double]]
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
    implicit final lazy val decoder: Decoder[Params] = deriveDecoder[Params]
    implicit final lazy val encoder: Encoder[Params] = deriveEncoder[Params]
  }

  object ScorePercentiles {
    implicit final lazy val decoder: Decoder[ScorePercentiles] = deriveDecoder[ScorePercentiles]
    implicit final lazy val encoder: Encoder[ScorePercentiles] = deriveEncoder[ScorePercentiles]
  }

  object Score {
    implicit final lazy val decoder: Decoder[Score] = deriveDecoder[Score]
    implicit final lazy val encoder: Encoder[Score] = deriveEncoder[Score]
  }

  object JmhResultAST {
    implicit final lazy val encoder: Encoder[JmhResultAST] = deriveEncoder[JmhResultAST]
    implicit final lazy val decoder: Decoder[JmhResultAST] = deriveDecoder[JmhResultAST]
  }
}

object JmhJsonResultParser {

  import JmhJsonResultAST._
  import io.circe.parser._

  final def parse(string: String): ValidatedNel[circe.Error, Array[JmhResultAST]] =
    decodeAccumulating[Array[JmhResultAST]](string)

}
