package com.guizmaii.zeklin.github.installation_event

import com.guizmaii.zeklin.github.{ Github, WebhookRouter }
import com.guizmaii.zeklin.modules.KafkaProducerModule
import org.http4s._
import org.scalatest.{ FreeSpec, Matchers }
import zio.clock.Clock
import zio.console.Console
import zio.{ DefaultRuntime, RIO, ZIO }

class WebhookRouterSpec extends FreeSpec with Matchers {
  import org.http4s.implicits._
  import zio.interop.catz._

  type TestEnv = Console with Clock with Github with KafkaProducerModule
  type Task[A] = RIO[TestEnv, A]

  private val runtime = new DefaultRuntime {}
  private val service = new WebhookRouter[TestEnv]

  private def request(name: String) =
    Request[Task](Method.GET, Uri.fromString(s"/hello/$name").right.get)

  private def call(request: Request[Task]): Task[Response[Task]] = service.routes.orNotFound(request)

  private def run[T](task: ZIO[TestEnv, Throwable, T]): T =
    runtime.unsafeRun(
      task.provideSome(
        base =>
          new Console with Clock with Github with KafkaProducerModule {
            override val console: Console.Service[Any]                   = base.console
            override val clock: Clock.Service[Any]                       = base.clock
            override val github: Github.Service[Clock]                   = null
            override val kafkaProducer: KafkaProducerModule.Service[Any] = null
          }
      )
    )

  "/hello/:name" - {
    "return 200" in {
      run(call(request("World"))).status shouldBe Status.Ok
    }
    "return hello :name" in {
      val name = "Jules"
      run(call(request(name)).flatMap(_.as[String])) shouldBe s"""{"message":"Hello, $name"}"""
    }
  }

}
