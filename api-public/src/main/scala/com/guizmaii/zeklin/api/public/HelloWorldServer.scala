package com.guizmaii.zeklin.api.public

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object HelloWorldServer extends IOApp {
  import cats.implicits._

  def run(args: List[String]): IO[ExitCode] =
    ServerStream.stream[IO].compile.drain.as(ExitCode.Success)
}

object ServerStream {
  import org.http4s.implicits._

  final def helloWorldRoutes[F[_]: Effect]: HttpRoutes[F] = new HelloWorldRoutes[F].routes

  final def stream[F[_]: ConcurrentEffect: Timer]: fs2.Stream[F, ExitCode] =
    BlazeServerBuilder[F]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(Router("/" -> helloWorldRoutes).orNotFound)
      .serve
}
