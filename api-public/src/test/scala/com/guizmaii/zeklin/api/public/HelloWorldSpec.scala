package com.guizmaii.zeklin.api.public

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.scalatest.{FreeSpec, Matchers}

class HelloWorldSpec extends FreeSpec with Matchers {

  "HelloWorld" - {
    "return 200" in {
      retHelloWorld.status shouldBe Status.Ok
    }
    "return hello world" in {
      retHelloWorld.as[String].unsafeRunSync() shouldBe "{\"message\":\"Hello, world\"}"
    }
  }

  private[this] val retHelloWorld: Response[IO] = {
    val getHW = Request[IO](Method.GET, Uri.uri("/hello/world"))
    new HelloWorldRoutes[IO].routes.orNotFound(getHW).unsafeRunSync()
  }

}
