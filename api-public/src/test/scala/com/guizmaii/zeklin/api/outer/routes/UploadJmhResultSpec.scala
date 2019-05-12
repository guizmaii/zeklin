package com.guizmaii.zeklin.api.outer.routes

import org.http4s._
import org.scalatest.{FreeSpec, Matchers}
import scalaz.zio.{DefaultRuntime, Task}

import scala.io.{Codec, Source}

object Helpers {

  def data(path: String): String = Source.fromResource(path)(Codec.UTF8).mkString

}

class UploadJmhResultSpec extends FreeSpec with Matchers {
  import Helpers._
  import org.http4s.implicits._
  import scalaz.zio.interop.catz._

  private val runtime = new DefaultRuntime {}
  private val service = new UploadJmhResult[Any]

  private val request: Request[Task] =
    Request[Task](method = Method.POST, uri = uri"/jmh/json").withEntity(data("aaa-benchs.json"))

  def call(request: Request[Task]): Response[Task] =
    runtime.unsafeRun(service.routes.orNotFound(request))

  "/api/jmh/json" - {
    "return 200" in {
      call(request).status shouldBe Status.Ok
    }
    "return hello world" in {
      runtime.unsafeRun(call(request).as[String]) shouldBe ""
    }
  }

}
