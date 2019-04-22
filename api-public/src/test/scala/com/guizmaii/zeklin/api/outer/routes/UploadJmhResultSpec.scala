package com.guizmaii.zeklin.api.outer.routes

import cats.effect.IO
import org.http4s._
import org.scalatest.{FreeSpec, Matchers}

import scala.io.{Codec, Source}

object Helpers {

  def data(path: String): String = Source.fromResource(path)(Codec.UTF8).mkString

}

class UploadJmhResultSpec extends FreeSpec with Matchers {
  import Helpers._
  import org.http4s.implicits._

  private val service = new UploadJmhResult[IO]

  private val request: Request[IO] =
    Request[IO](method = Method.POST, uri = Uri.uri("/jmh/json"))
      .withEntity(data("aaa-benchs.json"))

  def call(request: Request[IO]): Response[IO] = service.routes.orNotFound(request).unsafeRunSync()

  "/api/jmh/json" - {
    "return 200" in {
      call(request).status shouldBe Status.Ok
    }
    "return hello world" in {
      call(request).as[String].unsafeRunSync() shouldBe ""
    }
  }

}
