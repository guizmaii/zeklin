package com.guizmaii.zeklin.api

import cats.effect.Blocker
import com.guizmaii.zeklin.api.config.Env
import com.guizmaii.zeklin.github.Github
import io.circe.Json
import org.http4s.dsl.Http4sDsl
import org.http4s.headers._
import org.http4s.{ Charset, Headers, HttpRoutes, MediaType }
import zio.console.Console
import zio.{ RIO, Runtime }

object FrontendRouter {
  private final val indexHeaders: Headers = Headers.of(`Content-Type`(MediaType.text.html, Charset.`UTF-8`))

  private final def scripts(env: Env): String =
    if (env == Env.Prod)
      """<script type="text/javascript" src="assets/frontend-opt-bundle.js"></script>"""
    else
      """
        |<script type="text/javascript" src="assets/frontend-fastopt-library.js"></script>
        |<script type="text/javascript" src="assets/frontend-fastopt-loader.js"></script>
        |<script type="text/javascript" src="assets/frontend-fastopt.js"></script>
        |""".stripMargin

  private final def html(env: Env): String =
    s"""
       |<!DOCTYPE html>
       |<html>
       |<head>
       |  <meta charset="UTF-8">
       |  <title>Zeklin</title>
       |  <link rel="shortcut icon" type="image/png" href="assets/images/favicon.png"/>
       |</head>
       |<body>
       |  <div id="app"></div>
       |
       |  ${scripts(env)}
       |</body>
       |</html>
       |""".stripMargin
}

final class FrontendRouter[R <: Github with Console](implicit env: Env, blocker: Blocker, runtime: Runtime[R]) {
  import FrontendRouter._
  import org.http4s.circe._
  import zio.interop.catz._
  import org.http4s.server.staticcontent._

  type Task[A] = RIO[R, A]

  private val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  final val routes: HttpRoutes[Task] = {
    HttpRoutes.of[Task] {
      case req @ GET -> Root / "assets" / _ =>
        resourceService[Task](ResourceService.Config("/public", blocker, pathPrefix = "/assets"))
          .run(req)
          .getOrElseF(NotFound())
      case GET -> Root / "hello" / name => Ok(Json.obj("message" -> Json.fromString(s"Hello, $name")))
      case GET -> Root                  => Ok(html(env)).map(_.withHeaders(indexHeaders))
    }
  }

}
