package com.guizmaii.zeklin.api.publik

import cats.data.Kleisli
import cats.effect._
import com.guizmaii.zeklin.api.publik.routes.{HelloWorldRoutes, UploadJmhResult}
import org.http4s.{HttpApp, HttpRoutes, Request, Response}
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object Server extends IOApp {
  import cats.implicits._

  override final def run(args: List[String]): IO[ExitCode] =
    ServerStream.stream[IO].compile.drain.as(ExitCode.Success)
}

object ServerStream {
  import org.http4s.implicits._
  import org.http4s.server.middleware._

  private final def helloWorldRoutes[F[_]: Effect]: (String, HttpRoutes[F]) = "/" -> new HelloWorldRoutes[F].routes
  private final def publicApiRoutes[F[_]: Effect]: (String, HttpRoutes[F]) =
    "/api" -> new UploadJmhResult[F].routes

  private final def router[F[_]: ConcurrentEffect]: Kleisli[F, Request[F], Response[F]] =
    Router[F](helloWorldRoutes, publicApiRoutes).orNotFound

  private final def app[F[_]: ConcurrentEffect: Timer]: HttpApp[F] =
    ResponseTiming[F](
      Logger.httpApp(logHeaders = true, logBody = true)(router)
    )

  final def stream[F[_]: ConcurrentEffect: Timer]: fs2.Stream[F, ExitCode] = {
    BlazeServerBuilder[F]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(app)
      .serve
  }
}
