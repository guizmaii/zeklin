package com.guizmaii.zeklin.api.publik.routes

import cats.effect.IO
import org.http4s._
import org.scalatest.{FreeSpec, Matchers}

class HelloWorldSpec extends FreeSpec with Matchers {
  import org.http4s.implicits._

  val service                                  = new HelloWorldRoutes[IO]
  def request(name: String): Request[IO]       = Request[IO](Method.GET, Uri.fromString(s"/hello/$name").right.get)
  def call(request: Request[IO]): Response[IO] = service.routes.orNotFound(request).unsafeRunSync()

  "/hello/:name" - {
    "return 200" in {
      call(request("World")).status shouldBe Status.Ok
    }
    "return hello :name" in {
      val name = "Jules"
      call(request(name)).as[String].unsafeRunSync() shouldBe s"""{"message":"Hello, $name"}"""
    }
  }

}
