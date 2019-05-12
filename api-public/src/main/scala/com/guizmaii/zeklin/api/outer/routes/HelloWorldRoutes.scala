package com.guizmaii.zeklin.api.outer.routes

import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import scalaz.zio.TaskR

final class HelloWorldRoutes[R] {
  import org.http4s.circe._
  import scalaz.zio.interop.catz._

  type Task[A] = TaskR[R, A]

  val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  final val routes: HttpRoutes[Task] =
    HttpRoutes.of[Task] {
      case GET -> Root / "hello" / name =>
        Ok(Json.obj("message" -> Json.fromString(s"Hello, $name")))
    }
}
