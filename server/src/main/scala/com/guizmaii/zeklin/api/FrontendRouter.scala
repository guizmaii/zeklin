package com.guizmaii.zeklin.api

import java.net.URL

import cats.data._
import cats.effect.Blocker
import com.guizmaii.zeklin.github.Github
import io.circe.Json
import org.http4s.CacheDirective._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers._
import org.http4s.{ HttpRoutes, StaticFile }
import zio.RIO

object FrontendRouter {
  private final val supportedStaticExtensions = List(".html", ".js", ".map", ".css", ".png", ".ico")
  private final val cacheControlHeader        = `Cache-Control`(NonEmptyList.of(`no-cache`()))
}

final class FrontendRouter[R <: Github](blocker: Blocker) {
  import FrontendRouter._
  import cats.implicits._
  import org.http4s.circe._
  import zio.interop.catz._

  type Task[A] = RIO[R, A]

  private val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  private def getResource(pathInfo: String): Task[URL] = RIO(getClass.getResource(pathInfo))

  final val routes: HttpRoutes[Task] =
    HttpRoutes.of[Task] {
      case req if supportedStaticExtensions.exists(req.pathInfo.endsWith) =>
        StaticFile
          .fromResource[Task](req.pathInfo, blocker, req.some)
          .orElse(
            OptionT.liftF(getResource(req.pathInfo)).flatMap(StaticFile.fromURL[Task](_, blocker, req.some))
          )
          .map(_.putHeaders(cacheControlHeader))
          .fold(NotFound())(_.pure[Task])
          .flatten

      case GET -> Root / "hello" / name =>
        Ok(Json.obj("message" -> Json.fromString(s"Hello, $name")))
    }

}
