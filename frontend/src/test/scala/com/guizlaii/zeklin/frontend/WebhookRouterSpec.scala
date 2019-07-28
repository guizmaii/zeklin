package com.guizlaii.zeklin.frontend

import com.guizmaii.zeklin.frontend.WebhookRouter
import com.guizmaii.zeklin.github.Github
import org.http4s._
import org.scalatest.{ FreeSpec, Matchers }
import zio.clock.Clock
import zio.console.Console
import zio.{ DefaultRuntime, TaskR, ZIO }

class WebhookRouterSpec extends FreeSpec with Matchers {
  import org.http4s.implicits._
  import zio.interop.catz._

  object TestGithub extends Github {
    override val github: Github.Service[Clock] = null
  }

  type TestEnv = Console with Clock with Github
  type Task[A] = TaskR[TestEnv, A]

  private val runtime = new DefaultRuntime {}
  private val service = new WebhookRouter[TestEnv]

  private def request(name: String) =
    Request[Task](Method.GET, Uri.fromString(s"/hello/$name").right.get)

  private def call(request: Request[Task]): Task[Response[Task]] = service.routes.orNotFound(request)

  private def run[T](task: ZIO[TestEnv, Throwable, T]): T =
    runtime.unsafeRun(
      task.provideSome[DefaultRuntime#Environment](
        base =>
          new Console with Clock with Github {
            override val console: Console.Service[Any] = base.console
            override val github: Github.Service[Clock] = TestGithub.github
            override val clock: Clock.Service[Any]     = base.clock
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
