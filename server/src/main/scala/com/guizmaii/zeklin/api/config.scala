package com.guizmaii.zeklin.api

import doobie.hikari._
import doobie.util.transactor.Transactor
import org.flywaydb.core.Flyway
import scalaz.zio._
import scalaz.zio.interop.catz._

import scala.concurrent.ExecutionContext

object config {

  final case class Config(dbConfig: DBConfig)

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

}
