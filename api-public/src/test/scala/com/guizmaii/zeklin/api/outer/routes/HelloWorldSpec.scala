package com.guizmaii.zeklin.api.outer.routes

import org.http4s._
import org.scalatest.{ FreeSpec, Matchers }
import scalaz.zio.{ DefaultRuntime, Task }

class HelloWorldSpec extends FreeSpec with Matchers {
  import org.http4s.implicits._
  import scalaz.zio.interop.catz._

  private val runtime = new DefaultRuntime {}
  private val service = new HelloWorldRoutes[Any]

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
