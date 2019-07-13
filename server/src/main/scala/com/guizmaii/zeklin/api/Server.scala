package com.guizmaii.zeklin.api

import java.security.SecureRandom

import cats.effect.ExitCode
import com.guizmaii.zeklin.accounts.DoobieAccountRepository
import com.guizmaii.zeklin.api.config.Config
import com.guizmaii.zeklin.api.inner.routes.AccountApi
import com.guizmaii.zeklin.api.outer.routes.UploadJmhResult
import com.guizmaii.zeklin.frontend.config.GithubConfigs
import com.guizmaii.zeklin.frontend.{ CallbacksRouter, FrontEndRouter }
import org.http4s.HttpApp
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.passay.PasswordGenerator
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

  type AppEnvironment = Environment with DoobieAccountRepository
  type AppTask[A]     = TaskR[AppEnvironment, A]

  private final def router[R <: AppEnvironment](config: GithubConfigs, passwordGenerator: PasswordGenerator) =
    Router(
      "/"         -> new FrontEndRouter[R](config, passwordGenerator).routes,
      "/callback" -> new CallbacksRouter[R](config).routes,
      "/api"      -> new UploadJmhResult[R].routes, // TODO: middlewares(publicApiRoutes),
      "/private"  -> new AccountApi[R].routes // TODO: middlewares(privateApiRoutes)
    ).orNotFound

  private final def app[R <: AppEnvironment](
    config: GithubConfigs,
    passwordGenerator: PasswordGenerator
  ): HttpApp[AppTask] =
    ResponseTiming[AppTask](router(config, passwordGenerator)) // TODO 1OOO: Can we put this `ResponseTiming` in the Module#middlewares ??

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] =
    (for {
      cfg         <- ZIO.fromEither(pureconfig.loadConfig[Config])
      passwordG   <- IO.effect(new PasswordGenerator(SecureRandom.getInstanceStrong))
      _           <- config.initDb(cfg.dbConfig)
      blockingEC  <- ZIO.environment[Blocking].flatMap(_.blocking.blockingExecutor).map(_.asEC)
      transactorR = config.makeTransactor(cfg.dbConfig, Platform.executor.asEC, blockingEC)
      server = ZIO.runtime[AppEnvironment].flatMap { implicit rts =>
        BlazeServerBuilder[AppTask]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(app(cfg.github, passwordG))
          .serve
          .compile[AppTask, AppTask, ExitCode]
          .drain
      }
      program <- transactorR.use { transactor =>
                  server.provideSome[Environment] { base =>
                    new Clock with Console with System with Random with Blocking with DoobieAccountRepository {
                      override protected val xa: doobie.Transactor[Task] = transactor

                      override val clock: Clock.Service[Any]       = base.clock
                      override val console: Console.Service[Any]   = base.console
                      override val system: System.Service[Any]     = base.system
                      override val random: Random.Service[Any]     = base.random
                      override val blocking: Blocking.Service[Any] = base.blocking
                    }
                  }
                }
    } yield program).foldM(err => putStrLn(s"Execution failed with: $err") *> ZIO.succeed(1), _ => ZIO.succeed(0))
}
