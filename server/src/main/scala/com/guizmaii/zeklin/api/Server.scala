package com.guizmaii.zeklin.api

import java.security.Security

import cats.effect.ExitCode
import com.guizmaii.zeklin.accounts.DoobieAccountRepository
import com.guizmaii.zeklin.api.config.Config
import com.guizmaii.zeklin.api.inner.routes.AccountApi
import com.guizmaii.zeklin.api.outer.routes.UploadJmhResult
import com.guizmaii.zeklin.frontend.{ FrontEndRouter, WebhookRouter }
import com.guizmaii.zeklin.github.{ Github, GithubLive }
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.http4s.HttpApp
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import pureconfig.{ ConfigReader, Exported }
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.{ putStrLn, Console }
import zio.random.Random
import zio.system.System

/**
 * Inspired by:
 *   - https://github.com/gvolpe/advanced-http4s/blob/master/src/main/scala/com/github/gvolpe/http4s/server/Server.scala
 *   - https://github.com/mschuwalow/zio-todo-backend/blob/develop/app/src/main/scala/com/schuwalow/zio/todo/Main.scala
 */
object Server extends App {
  import org.http4s.implicits._
  import org.http4s.server.middleware._
  import pureconfig.generic.auto._
  import zio.interop.catz._
  implicitly[Exported[ConfigReader[Config]]] // ⚠️ Without, Intellij removes `pureconfig.generic.auto._` import...

  type AppEnvironment = Environment with DoobieAccountRepository with Github
  type AppTask[A]     = RIO[AppEnvironment, A]

  private final def router[R <: AppEnvironment] =
    Router(
      "/"        -> new FrontEndRouter[R].routes,
      "/webhook" -> new WebhookRouter[R].routes,
      "/api"     -> new UploadJmhResult[R].routes, // TODO: middlewares(publicApiRoutes),
      "/private" -> new AccountApi[R].routes // TODO: middlewares(privateApiRoutes)
    ).orNotFound

  private final def app[R <: AppEnvironment]: HttpApp[AppTask] =
    Logger.httpApp(logHeaders = true, logBody = true)(ResponseTiming[AppTask](router)) // TODO 1OOO: Can we put this `ResponseTiming` in the Module#middlewares ??

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] =
    (for {
      _           <- ZIO.effect(Security.addProvider(new BouncyCastleProvider())) // https://stackoverflow.com/a/18912362
      cfg         <- ZIO.fromEither(pureconfig.loadConfig[Config])
      _           <- config.initDb(cfg.dbConfig)
      blockingEC  <- ZIO.environment[Blocking].flatMap(_.blocking.blockingExecutor).map(_.asEC)
      transactorR = config.makeTransactor(cfg.dbConfig, Platform.executor.asEC, blockingEC)
      httpClientR <- ZIO.runtime[Environment].map { implicit rts =>
                      config.makeHttpClient(Platform.executor.asEC)
                    }
      server = ZIO.runtime[AppEnvironment].flatMap { implicit rts =>
        BlazeServerBuilder[AppTask]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(app)
          .serve
          .compile[AppTask, AppTask, ExitCode]
          .drain
      }
      program <- (httpClientR <*> transactorR).use {
                  case (httpClient, transactor) =>
                    server.provideSome[Environment] { base =>
                      new Clock with Console with System with Random with Blocking with DoobieAccountRepository
                      with Github {
                        override protected val xa: doobie.Transactor[Task] = transactor

                        override val clock: Clock.Service[Any]       = base.clock
                        override val console: Console.Service[Any]   = base.console
                        override val system: System.Service[Any]     = base.system
                        override val random: Random.Service[Any]     = base.random
                        override val blocking: Blocking.Service[Any] = base.blocking
                        override val github: Github.Service[Clock] =
                          new GithubLive(httpClient, cfg.github).github
                      }
                    }
                }
    } yield program).foldM(err => putStrLn(s"Execution failed with: $err") *> ZIO.succeed(1), _ => ZIO.succeed(0))
}
