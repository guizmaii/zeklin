package com.guizmaii.zeklin.frontend

import java.net.URL

import cats.data._
import com.guizmaii.zeklin.frontend.js.JSMethods
import io.circe.Json
import org.http4s.CacheDirective._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers._
import org.http4s.{ Charset, HttpRoutes, MediaType, StaticFile }
import scalatags.Text.TypedTag
import scalatags.Text.all.Modifier
import zio.TaskR

import scala.concurrent.ExecutionContext.global

object FrontEndRouter {

  private[FrontEndRouter] final val supportedStaticExtensions = List(".html", ".js", ".map", ".css", ".png", ".ico")
  private[FrontEndRouter] final val cacheControlHeader        = `Cache-Control`(NonEmptyList.of(`no-cache`()))

  private[FrontEndRouter] val jsScript = "zeklin-frontend-fastopt.js"
  private[FrontEndRouter] val jsDeps   = "zeklin-frontend-jsdeps.js"

  private[FrontEndRouter] def template(
    headContent: Seq[Modifier],
    bodyContent: Seq[Modifier],
    scripts: Seq[Modifier],
    cssComps: Seq[Modifier]
  ): TypedTag[String] = {
    import scalatags.Text.all._

    html(
      head(
        headContent,
        cssComps,
        link(rel := "shortcut icon", media := "image/png", href := "/assets/images/favicon.png")
      ),
      body(
        bodyContent,
        List(
          script(src := jsScript),
          script(src := jsDeps)
        ) ++ scripts
      )
    )
  }

}

final class FrontEndRouter[R] {
  import cats.implicits._
  import org.http4s.circe._
  import zio.interop.catz._
  import FrontEndRouter._

  type Task[A] = TaskR[R, A]

  val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  private def getResource(pathInfo: String): Task[URL] = TaskR.apply(getClass.getResource(pathInfo))

  private val index: Seq[Modifier] = {
    import scalatags.Text.all._

    List(
      h1(
        style := "align: center;",
        "Zeklin"
      )
    )
  }

  private val buttonTag: Seq[Modifier] = {
    import scalatags.Text.all._
    List(
      button(
        id := "click-me-button",
        `type` := "button",
        onclick := s"${JSMethods.clickedMessage}()",
        "Click Me"
      )
    )
  }

  final val routes: HttpRoutes[Task] =
    HttpRoutes.of[Task] {
      case GET -> Root =>
        Ok(template(Seq(), index ++ buttonTag, Seq(), Seq()).render)
          .map(
            _.withContentType(`Content-Type`(MediaType.text.html, Charset.`UTF-8`))
              .putHeaders(cacheControlHeader)
          )

      case req if supportedStaticExtensions.exists(req.pathInfo.endsWith) =>
        StaticFile
          .fromResource[Task](req.pathInfo, global, req.some)
          .orElse(OptionT.liftF(getResource(req.pathInfo)).flatMap(StaticFile.fromURL[Task](_, global, req.some)))
          .map(_.putHeaders(cacheControlHeader))
          .fold(NotFound())(_.pure[Task])
          .flatten

      case GET -> Root / "hello" / name =>
        Ok(Json.obj("message" -> Json.fromString(s"Hello, $name")))
    }
}
