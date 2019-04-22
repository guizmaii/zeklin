package com.guizmaii.zeklin.api.publik.routes

import cats.effect.Effect
import com.guizmaii.zeklin.jmh.json.JmhJsonResultAST.JmhResultAST
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

final class UploadJmhResult[F[_]: Effect] extends Http4sDsl[F] {

  import cats.syntax.flatMap._
  import org.http4s.circe.CirceEntityCodec._

  final val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "jmh" / "json" => req.as[List[JmhResultAST]].flatMap(_ => Ok("TATA"))
    }

}
