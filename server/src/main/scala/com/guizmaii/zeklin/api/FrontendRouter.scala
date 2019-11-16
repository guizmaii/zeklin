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
       |  <link rel="shortcut icon" type="image/png" href="assets/favicon.png"/>
       |
       |  <script src="https://www.gstatic.com/firebasejs/ui/4.2.0/firebase-ui-auth.js"></script>
       |  <link type="text/css" rel="stylesheet" href="https://www.gstatic.com/firebasejs/ui/4.2.0/firebase-ui-auth.css" />
       |</head>
       |<body>
       |  <div id="app"></div>
       |  <div id="firebaseui-auth-container"></div>
       |
       |  ${scripts(env)}
       |
       |  <!-- Firebase App (the core Firebase SDK) is always required and must be listed first -->
       |  <script src="https://www.gstatic.com/firebasejs/7.2.2/firebase-app.js"></script>
       |
       |  <!-- If you enabled Analytics in your project, add the Firebase SDK for Analytics -->
       |  <script src="https://www.gstatic.com/firebasejs/7.2.2/firebase-analytics.js"></script>
       |
       |  <!-- Add Firebase products that you want to use -->
       |  <script src="https://www.gstatic.com/firebasejs/7.2.2/firebase-auth.js"></script>
       |  <script type="text/javascript" src="assets/firebase.js"></script>
       |</body>
       |</html>
       |""".stripMargin
}

final class FrontendRouter[R <: Github with Console](implicit env: Env, blocker: Blocker, runtime: Runtime[R]) {
  import FrontendRouter._
  import org.http4s.circe._
  import org.http4s.server.staticcontent._
  import zio.interop.catz._

  type Task[A] = RIO[R, A]

  private val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  private val index: String = html(env)
  private val resources     = resourceService[Task](ResourceService.Config("/public", blocker, pathPrefix = "/assets"))

  final val routes: HttpRoutes[Task] = {
    HttpRoutes.of[Task] {
      case GET -> Root / "hello" / name     => Ok(Json.obj("message" -> Json.fromString(s"Hello, $name")))
      case req @ GET -> Root / "assets" / _ => resources.run(req).getOrElseF(NotFound())
      case GET -> Root                      => Ok(index).map(_.withHeaders(indexHeaders))
    }
  }

}
