package com.guizmaii.zeklin.frontend

import com.guizmaii.zeklin.github.Github
import com.guizmaii.zeklin.github.installation_event.Payload
import org.http4s.dsl.Http4sDsl
import org.http4s.{ HttpRoutes, Response }
import zio.clock.Clock
import zio.console.Console
import zio.{ TaskR, ZIO }

final class WebhookRouter[R <: Console with Github with Clock] {

  import org.http4s.QueryParamDecoder._
  import zio.interop.catz._
  import org.http4s.circe.CirceEntityDecoder._

  type Task[A] = TaskR[R, A]

  val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  object CodeQueryParamMatcher  extends QueryParamDecoderMatcher[String]("code")
  object StateQueryParamMatcher extends QueryParamDecoderMatcher[String]("state")

  val routes: HttpRoutes[Task] =
    HttpRoutes.of[Task] {
      case req @ POST -> Root =>
        ZIO.accessM[R] { env =>
          import env.github._

          def continue(parsedBody: Payload): Task[Response[Task]] =
            ZIO.accessM { env =>
              import env.console._
              import env.github._

              for {
                resp0 <- authenticateApp
                resp1 <- authenticateAppInstallation(parsedBody.installation.id)
                _     <- putStrLn(s"""
                                 | resp0: $resp0
                                 | resp1: $resp1
                                 |""".stripMargin)
                res   <- Ok("Hello Webhook")
              } yield res
            }

          for {
            rawBody   <- req.as[String]
            parseBody <- req.as[Payload]
            valid     <- isValidWebhookSignature(req.headers, rawBody)
            res       <- if (valid) continue(parseBody) else Forbidden("")
          } yield res
        }

      case GET -> Root / "githubapp" :? CodeQueryParamMatcher(code) +& StateQueryParamMatcher(state) =>
        ZIO.accessM[R] { env =>
          import env.console._
          import env.github._

          for {
            _     <- putStrLn(s"Hello Github $code, state: $state")
            token <- authenticateApp
            _     <- putStrLn("-------------")
            _     <- putStrLn(s"accessToken: $token")
            _     <- putStrLn("-------------")
            res   <- Ok("Hello Github")
          } yield res
        }

    }
}
