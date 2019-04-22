package com.guizmaii.zeklin.api

import cats.effect.{ConcurrentEffect, IOApp, Timer}
import com.guizmaii.zeklin.api.inner.routes.AccountApi
import com.guizmaii.zeklin.api.outer.routes.{HelloWorldRoutes, UploadJmhResult}
import fs2.Stream
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.{AutoSlash, GZip, Logger}
import org.http4s.{HttpApp, HttpRoutes}

/**
  * Inspired by https://github.com/gvolpe/advanced-http4s/blob/master/src/main/scala/com/github/gvolpe/http4s/server/Module.scala
  */
final class Module[F[_]: ConcurrentEffect: Timer] {

  def middlewares: HttpRoutes[F] => HttpRoutes[F] = { service: HttpRoutes[F] =>
    GZip(service)
  } compose { service: HttpRoutes[F] =>
    AutoSlash(service)
  } compose { service: HttpRoutes[F] =>
    Logger.httpRoutes(logHeaders = true, logBody = true)(service)
  }

  val helloWorldRoutes: HttpRoutes[F] = new HelloWorldRoutes[F].routes

  val publicApiRoutes: HttpRoutes[F] = new UploadJmhResult[F].routes

  val privateApiRoutes: HttpRoutes[F] = new AccountApi[F].routes

}

/**
  * Inspired by https://github.com/gvolpe/advanced-http4s/blob/master/src/main/scala/com/github/gvolpe/http4s/server/Server.scala
  */
object Server extends IOApp {
  import cats.effect._
  import cats.implicits._
  import org.http4s.implicits._
  import org.http4s.server.middleware._

  private final def app[F[_]: ConcurrentEffect](ctx: Module[F]): HttpApp[F] = {
    import ctx._

    Router(
      "/"        -> helloWorldRoutes,
      "/api"     -> middlewares(publicApiRoutes),
      "/private" -> middlewares(privateApiRoutes)
    ).orNotFound
  }

  final def stream[F[_]: ConcurrentEffect: Timer]: fs2.Stream[F, ExitCode] =
    for {
      ctx      <- Stream.emit(new Module[F])
      app      <- Stream.emit(ResponseTiming[F](app(ctx))) // TODO 1OOO: Can we put this `ResponseTiming` in the Module#middlewares ??
      exitCode <- BlazeServerBuilder[F].bindHttp(8080, "0.0.0.0").withHttpApp(app).serve
    } yield exitCode

  override final def run(args: List[String]): IO[ExitCode] =
    stream[IO].compile.drain.as(ExitCode.Success)

}
