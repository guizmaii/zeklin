package com.guizmaii.zeklin.api.inner.routes

import com.guizmaii.zeklin.accounts.AccountRepository
import io.circe.generic.auto._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ HttpRoutes, MessageFailure }
import zio.RIO
import zio.interop.catz._

final case class CreateReq(firstName: String, lastName: String, email: String)

final class AccountApi[R <: AccountRepository] {

  import com.guizmaii.zeklin.accounts._
  import org.http4s.circe.CirceEntityCodec._

  type Task[A] = RIO[R, A]

  val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  final val routes: HttpRoutes[Task] =
    HttpRoutes.of[Task] {
      case req @ PUT -> Root / "account" =>
        req
          .as[CreateReq]
          .map(r => User(firstName = r.firstName, lastName = r.lastName, email = r.email))
          .flatMap(AccountRepository.createAccount)
          .foldM(
            { case e: MessageFailure => e.toHttpResponse(req.httpVersion) },
            account => Created(account.id.value)
          )

      case GET -> Root / "account" / UUIDVar(id) =>
        for {
          account  <- AccountRepository.getById(AccountId(id))
          response <- account.fold(NotFound())(Ok(_))
        } yield response
    }

}
