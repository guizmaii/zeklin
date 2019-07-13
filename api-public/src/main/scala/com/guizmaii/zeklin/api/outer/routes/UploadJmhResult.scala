package com.guizmaii.zeklin.api.outer.routes

import com.guizmaii.zeklin.jmh.json.JmhJsonResultAST.JmhResultAST
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import zio.TaskR

final class UploadJmhResult[R] {
  import org.http4s.circe.CirceEntityCodec._
  import zio.interop.catz._

  type Task[A] = TaskR[R, A]

  val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  final val routes: HttpRoutes[Task] =
    HttpRoutes.of[Task] {
      case req @ POST -> Root / "jmh" / "json" => req.as[List[JmhResultAST]].flatMap(_ => Ok())
    }

}
