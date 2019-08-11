package com.guizmaii.zeklin.frontend

import cats.data.{ Kleisli, OptionT }
import com.guizmaii.zeklin.github.Github
import io.circe.Json
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.{ AuthedRoutes, HttpRoutes, Request, Response }
import zio.clock.Clock
import zio.console.Console
import zio.{ IO, RIO, ZIO }

object WebhookRouter {
  final case class JsonFieldMissing(fieldPath: String) extends RuntimeException(s"'$fieldPath' field is missing")
}

final class WebhookRouter[R <: Console with Github with Clock] {

  import WebhookRouter._
  import zio.interop.catz._

  type Task[A] = RIO[R, A]

  private val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  private def asTask[E, A](opt: Option[A])(ifEmpty: => E): IO[E, A] = ZIO.fromOption(opt).mapError(_ => ifEmpty)

  // TODO: Add tests for the webhook signature verification mecanism
  private val authWebhook: Kleisli[Task, Request[Task], Either[String, Request[Task]]] =
    Kleisli { request =>
      ZIO.accessM[R] { env =>
        import env.github._

        for {
          rawBody <- request.as[String]
          valid   <- isValidWebhookSignature(request.headers, rawBody)
        } yield if (valid) Right(request) else Left("Invalid Webhook Signature")
      }
    }

  private val onFailure: AuthedRoutes[String, Task]           = Kleisli(req => OptionT.liftF(Forbidden(req.authInfo)))
  private val middleware: AuthMiddleware[Task, Request[Task]] = AuthMiddleware(authWebhook, onFailure)

  private val authedRoutes: AuthedRoutes[Request[Task], Task] =
    AuthedRoutes.of[Request[Task], Task] {
      case POST -> Root as req =>
        def create(id: Long): Task[Response[Task]] = Ok(s"TODO Created $id")
        def delete(id: Long): Task[Response[Task]] = Ok(s"TODO Deleted $id")

        import org.http4s.circe._

        req.as[Json].flatMap { json =>
          val action         = asTask(Github._action.getOption(json))(JsonFieldMissing("root.action"))
          val installationId = asTask(Github._installationId.getOption(json))(JsonFieldMissing("root.installation.id"))

          (action <*> installationId).flatMap {
            case ("created", id) => create(id)
            case ("deleted", id) => delete(id)
            case (action, _)     => NotImplemented(s"$action Not Handled For Now")
          }
        }

    }

  val routes: HttpRoutes[Task] = middleware(authedRoutes)

}
