package com.guizmaii.zeklin.api.inner.routes

import cats.effect.Effect
import io.circe.generic.auto._
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, MessageFailure}

final case class CreateReq(firstName: String, lastName: String, email: String)

final class AccountApi[F[_]: Effect] extends Http4sDsl[F] {

  import cats.implicits._
  import org.http4s.circe.CirceEntityCodec._

  final val routes: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ PUT -> Root / "account" =>
        req.as[CreateReq].flatMap(createAccount).flatMap {
          case Right(_)                => Created()
          case Left(e: MessageFailure) => e.toHttpResponse(req.httpVersion)
        }
    }

  // TODO 0: Rewrite with ZIO.
  private def createAccount(req: CreateReq): F[Either[MessageFailure, Unit]] = ???

}
