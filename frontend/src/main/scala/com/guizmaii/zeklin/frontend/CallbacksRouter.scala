package com.guizmaii.zeklin.frontend

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import zio.console.Console
import zio.{ TaskR, ZIO }

final class CallbacksRouter[R <: Console with Github] {

  import org.http4s.QueryParamDecoder._
  import zio.interop.catz._

  type Task[A] = TaskR[R, A]

  val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  object CodeQueryParamMatcher  extends QueryParamDecoderMatcher[String]("code")
  object StateQueryParamMatcher extends QueryParamDecoderMatcher[String]("state")

  val routes: HttpRoutes[Task] =
    HttpRoutes.of[Task] {
      case GET -> Root / "githubapp" :? CodeQueryParamMatcher(code) +& StateQueryParamMatcher(state) =>
        ZIO.accessM[R] { env =>
          import env.console._
          import env.github._

          for {
            _     <- putStrLn(s"Hello Github $code")
            token <- fetchAccessToken(code, state)
            _     <- putStrLn("-------------")
            _     <- putStrLn(s"accessToken: ${token}")
            _     <- putStrLn("-------------")
            res   <- Ok("Hello Github")
          } yield res
        }

      case GET -> Root / "accesstoken" =>
        Ok("Github: Accessed!")
    }
}
