package com.guizmaii.zeklin.api

import com.guizmaii.zeklin.frontend.config.GithubAppConfigs
import doobie.hikari._
import doobie.util.transactor.Transactor
import org.flywaydb.core.Flyway
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.client.middleware.Logger
import zio._
import zio.interop.catz._

import scala.concurrent.ExecutionContext

object config {

  final case class Config(dbConfig: DBConfig, github: GithubAppConfigs)

  final case class DBConfig(
    url: String,
    driver: String,
    user: String,
    password: String
  )

  def initDb(cfg: DBConfig): Task[Unit] =
    ZIO.effect {
      Flyway
        .configure()
        .dataSource(cfg.url, cfg.user, cfg.password)
        .load()
        .migrate()
    }.unit

  def makeTransactor(
    cfg: DBConfig,
    connectEC: ExecutionContext,
    transactEC: ExecutionContext
  ): Managed[Throwable, Transactor[Task]] = Managed {
    HikariTransactor
      .newHikariTransactor[Task](cfg.driver, cfg.url, cfg.user, cfg.password, connectEC, transactEC)
      .allocated
      .map { case (transactor, cleanupM) => Reservation(ZIO.succeed(transactor), cleanupM.orDie) }
      .uninterruptible
  }

  def makeHttpClient[R](ec: ExecutionContext)(implicit runtime: Runtime[R]): Managed[Throwable, Client[Task]] =
    Managed {
      BlazeClientBuilder[Task](ec).allocated.map {
        case (client, cleanupM) =>
          Reservation(ZIO.succeed(Logger(logHeaders = true, logBody = true)(client)), cleanupM.orDie)
      }.uninterruptible
    }

}
