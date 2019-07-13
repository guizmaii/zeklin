package com.guizmaii.zeklin.frontend

import com.guizmaii.zeklin.frontend.config.GithubConfigs
import github4s.cats.effect.SyncCaptureInstance
import github4s.free.interpreters.Interpreters
import github4s.{ Github, HttpRequestBuilderExtensionJVM }
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import scalaj.http.HttpResponse
import zio.console.Console
import zio.{ TaskR, ZIO }

final class CallbacksRouter[R <: Console](config: GithubConfigs) {

  import github4s.Github._
  import org.http4s.QueryParamDecoder._
  import zio.interop.catz._

  object ZioInterpreter extends HttpRequestBuilderExtensionJVM with SyncCaptureInstance {
    implicit def intInstanceSyncScalaJ: Interpreters[zio.Task, HttpResponse[String]] =
      new Interpreters[zio.Task, HttpResponse[String]]
  }
  import ZioInterpreter._

  type Task[A] = TaskR[R, A]

  val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  object CodeQueryParamMatcher  extends QueryParamDecoderMatcher[String]("code")
  object StateQueryParamMatcher extends QueryParamDecoderMatcher[String]("state")

  val github = Github(None)

  val routes: HttpRoutes[Task] =
    HttpRoutes.of[Task] {
      case GET -> Root / "githubapp" :? CodeQueryParamMatcher(code) +& StateQueryParamMatcher(state) =>
        ZIO.accessM[R] { env =>
          import env.console._

          for {
            _ <- putStrLn(s"Hello Github $code")
            token <- github.auth
                      .getAccessToken(
                        config.app.clientId,
                        config.app.clientSecret,
                        code,
                        "http://localhost:8080/callback/accesstoken",
                        state
                      )
                      .exec[zio.Task, HttpResponse[String]](
                        Map("Accept" -> "application/json", "Content-Type" -> "application/json")
                      )

            _   <- putStrLn("-------------")
            _   <- putStrLn(s"accessToken: ${token}")
            _   <- putStrLn("-------------")
            res <- Ok("Hello Github")
          } yield res
        }

      case GET -> Root / "accesstoken" =>
        Ok("Github: Accessed!")
    }
}
