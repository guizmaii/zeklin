package com.guizmaii.zeklin.api

import cats.effect.{ Blocker, ContextShift }
import com.guizmaii.zeklin.github.config.GithubAppConfigs
import doobie.hikari._
import doobie.util.transactor.Transactor
import fs2.kafka.KafkaProducer
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
    blocker: Blocker
  ): Managed[Throwable, Transactor[Task]] = ZManaged {
    HikariTransactor
      .newHikariTransactor[Task](cfg.driver, cfg.url, cfg.user, cfg.password, connectEC, blocker)
      .allocated
      .map { case (transactor, cleanupM) => Reservation(ZIO.succeed(transactor), _ => cleanupM.orDie) }
      .uninterruptible
  }

  def makeHttpClient[R: Runtime](ec: ExecutionContext): Managed[Throwable, Client[Task]] =
    ZManaged {
      BlazeClientBuilder[Task](ec).allocated.map {
        case (client, cleanupM) =>
          Reservation(ZIO.succeed(Logger(logHeaders = true, logBody = true)(client)), _ => cleanupM.orDie)
      }.uninterruptible
    }

  def makeKafkaProducer[R: Runtime](
    bootstrapServers: String
  )(implicit blocker: Blocker, ctx: ContextShift[Task]): Managed[Throwable, KafkaProducer[Task, String, String]] =
    ZManaged {
      import fs2.kafka._

      val producerSettings =
        ProducerSettings[Task, String, String]
          .withBlocker(blocker)
          .withBootstrapServers(bootstrapServers)

      producerResource(producerSettings).allocated.map {
        case (client, cleanupM) => Reservation(ZIO.succeed(client), _ => cleanupM.orDie)
      }.uninterruptible
    }

}
