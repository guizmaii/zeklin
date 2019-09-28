package com.guizmaii.zeklin.frontend

import cats.data.{ Kleisli, OptionT }
import com.guizmaii.zeklin.github.Github
import com.guizmaii.zeklin.modules.KafkaProducerModule
import fs2.kafka.{ ProducerRecord, ProducerRecords }
import io.circe.{ Json, Printer }
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.{ AuthedRoutes, HttpRoutes, Request }
import zio.clock.Clock
import zio.console.Console
import zio.{ IO, RIO, ZIO }

object WebhookRouter {
  final case class JsonFieldMissing(fieldPath: String) extends RuntimeException(s"'$fieldPath' field is missing")

  implicit final class OptionOps[A](val option: Option[A]) extends AnyVal {
    def asIO[E](ifEmpty: => E): IO[E, A] = ZIO.fromOption(option).mapError(_ => ifEmpty)
  }
}

final class WebhookRouter[R <: Console with Github with Clock with KafkaProducerModule] {
  import WebhookRouter._
  import zio.interop.catz._

  type Task[A] = RIO[R, A]

  private val dsl: Http4sDsl[Task] = Http4sDsl[Task]
  import dsl._

  // TODO: Add tests for the webhook signature verification mecanism
  private val authWebhook: Kleisli[Task, Request[Task], Either[String, Request[Task]]] =
    Kleisli { request =>
      ZIO.accessM[R] { env =>
        import env._

        for {
          rawBody <- request.as[String]
          valid   <- github.isValidWebhookSignature(request.headers, rawBody)
        } yield if (valid) Right(request) else Left("Invalid Webhook Signature")
      }
    }

  private val onFailure: AuthedRoutes[String, Task]           = Kleisli(req => OptionT.liftF(Forbidden(req.authInfo)))
  private val middleware: AuthMiddleware[Task, Request[Task]] = AuthMiddleware(authWebhook, onFailure)

  private val githubAppWebhookTopicName = "github-app-webhook-events"

  private def kafkaMessage(id: Long, json: Json): ProducerRecords[String, String, Unit] = {
    val record = ProducerRecord(githubAppWebhookTopicName, id.toString, json.printWith(Printer.noSpaces))
    ProducerRecords.one(record)
  }

  private val authedRoutes: AuthedRoutes[Request[Task], Task] =
    AuthedRoutes.of[Request[Task], Task] {
      case POST -> Root as req =>
        ZIO.accessM[R] { env =>
          import env._
          import org.http4s.circe._

          for {
            json           <- req.as[Json]
            installationId <- Github._installationId.getOption(json).asIO(JsonFieldMissing("root.installation.id"))
            _              <- kafkaProducer.instance.produce(kafkaMessage(installationId, json))
            res            <- Ok("Done")
          } yield res
        }
    }

  val routes: HttpRoutes[Task] = middleware(authedRoutes)

}
