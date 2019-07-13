package com.guizlaii.zeklin.frontend

import com.guizmaii.zeklin.frontend.CallbacksRouter
import com.guizmaii.zeklin.frontend.config.{ GithubAppConfigs, GithubConfigs }
import org.http4s._
import org.scalatest.{ FreeSpec, Matchers }
import zio.{ DefaultRuntime, TaskR }

class FrontEndRouterSpec extends FreeSpec with Matchers {
  import org.http4s.implicits._
  import zio.interop.catz._

  private val githubConfigs = GithubConfigs("random", GithubAppConfigs("random", "random", "random"))
  private val runtime       = new DefaultRuntime {}
  private val service       = new CallbacksRouter[DefaultRuntime#Environment](githubConfigs)

  type Task[A] = TaskR[DefaultRuntime#Environment, A]

  private def request(name: String): Request[Task] =
    Request[Task](Method.GET, Uri.fromString(s"/hello/$name").right.get)

  private def call(request: Request[Task]): Response[Task] =
    runtime.unsafeRun(service.routes.orNotFound(request))

  "/hello/:name" - {
    "return 200" in {
      call(request("World")).status shouldBe Status.Ok
    }
    "return hello :name" in {
      val name = "Jules"
      runtime.unsafeRun(call(request(name)).as[String]) shouldBe s"""{"message":"Hello, $name"}"""
    }
  }

}
