package com.guizmaii.zeklin.frontend

object FrontEndRouter {

  private[FrontEndRouter] val jsScript = "zeklin-frontend-fastopt.js"
  private[FrontEndRouter] val jsDeps   = "zeklin-frontend-jsdeps.js"

  /*


  /**
 * https://getbootstrap.com/docs/4.3/components/navbar/#nav
 */
  private[FrontEndRouter] final val navbar = {

    tags2.nav(
      cls := "navbar is-white is-spaced",
      role := "navigation",
      aria.label := "main navigation",
      div(
        cls := "navbar-brand",
        a(
          cls := "navbar-item is-size-1 has-text-primary has-text-weight-bold is-family-code",
          href := "http://localhost:8080/",
          "Zeklin"
        ),
        a(
          role := "button",
          cls := "navbar-burger burger",
          aria.label := "menu",
          aria.expanded := "false",
          data.target := "navbarBasicExample",
          span(aria.hidden := true),
          span(aria.hidden := true),
          span(aria.hidden := true)
        )
      ),
      div(
        cls := "navbar-menu has-text-weight-semibold ",
        div(
          cls := "navbar-end",
          a(cls := "navbar-item", "Home"),
          a(cls := "navbar-item", "Documentation"),
          a(cls := "navbar-item", "Pricing"),
          a(cls := "navbar-item", "Contacts")
        )
      )
    )
  }

  private[FrontEndRouter] def template(
    headContent: Seq[Modifier],
    bodyContent: Seq[Modifier],
    scripts: Seq[Modifier],
    cssComps: Seq[Modifier]
  ): TypedTag[String] =
    html(
      head(
        meta(charset := "utf-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1"),
        tags2.title("Zeklin"),
        headContent,
        cssComps,
        link(rel := "shortcut icon", media := "image/png", href := "/assets/images/favicon.png"),
        link(
          rel := "stylesheet",
          href := "https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.5/css/bulma.min.css"
        ),
        link(
          rel := "stylesheet",
          href := "https://cdn.jsdelivr.net/npm/bulma-helpers@0.3.8/css/bulma-helpers.min.css"
        ),
        script(defer, src := "https://use.fontawesome.com/releases/v5.3.1/js/all.js")
      ),
      body(
        header(navbar),
        bodyContent,
        List(
          script(src := jsScript),
          script(src := jsDeps)
        ) ++ scripts
      )
    )

}

final class FrontEndRouter[R <: Github](blocker: Blocker) {
  import FrontEndRouter._

  type Task[A] = RIO[R, A]

  private val dsl: Http4sDsl[Task] = Http4sDsl[Task]

  private val index: List[TypedTag[String]] = List.empty

  private def loginButton: List[TypedTag[String]] =
    List(
      div(
        cls := "columns is-mobile is-centered",
        div(
          cls := "column is-half has-text-centered",
          p(
            cls := "has-margin-20",
            """
              |Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
              |Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
              |Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
              |Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
              |""".stripMargin
          ),
          a(
            id := "login-link",
            href := "https://github.com/apps/zeklin-dev",
            target := "_blank",
            `type` := "button",
            cls := "button is-success is-large is-outlined has-margin-20",
            span(cls := "icon", i(cls := "fab fa-github")),
            span(cls := "is-family-code", "Install Zeklin")
          )
        )
      )
    )

  final val routes: HttpRoutes[Task] =
    HttpRoutes.of[Task] {
      case GET -> Root =>
        for {
          resp <- Ok(template(Seq(), index ++ loginButton, Seq(), Seq()).render)
                   .map(
                     _.withContentType(`Content-Type`(MediaType.text.html, Charset.`UTF-8`))
                       .putHeaders(cacheControlHeader)
                   )
        } yield resp

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

 */
}
