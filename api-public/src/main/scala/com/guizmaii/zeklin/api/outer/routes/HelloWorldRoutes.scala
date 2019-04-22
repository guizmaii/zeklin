package com.guizmaii.zeklin.api.outer.routes

import cats.effect.Sync
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

final class HelloWorldRoutes[F[_]: Sync] extends Http4sDsl[F] {
  import org.http4s.circe._

  final val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        Ok(Json.obj("message" -> Json.fromString(s"Hello, $name")))
    }
}
