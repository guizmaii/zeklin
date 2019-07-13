package com.guizmaii.zeklin.accounts

import java.util.UUID

import doobie.h2.implicits._
import doobie.implicits._
import doobie.{ Transactor, _ }
import zio.interop.catz._
import zio.{ Task, ZIO }

final case class User(firstName: String, lastName: String, email: String)
final case class AccountId(value: UUID) extends AnyVal
final case class Account(id: AccountId, user: User)

trait AccountRepository {
  val accountRepository: AccountRepository.Service[Any]
}

object AccountRepository {
  trait Service[R] {
    def createAccount(user: User): ZIO[R, Nothing, Account]

    def getById(id: AccountId): ZIO[R, Nothing, Option[Account]]
  }

  def createAccount(user: User): ZIO[AccountRepository, Nothing, Account] =
    ZIO.accessM(_.accountRepository.createAccount(user))

  def getById(id: AccountId): ZIO[AccountRepository, Nothing, Option[Account]] =
    ZIO.accessM(_.accountRepository.getById(id))

}

trait DoobieAccountRepository extends AccountRepository {
  import DoobieAccountRepository._

  protected def xa: Transactor[Task]

  override final val accountRepository: AccountRepository.Service[Any] =
    new AccountRepository.Service[Any] {
      override final def createAccount(user: User): ZIO[Any, Nothing, Account] =
        SQL
          .create(user)
          .withUniqueGeneratedKeys[UUID]("id")
          .map(id => Account(id = AccountId(id), user = user))
          .transact(xa)
          .orDie

      override final def getById(id: AccountId): ZIO[Any, Nothing, Option[Account]] =
        SQL
          .getById(id)
          .option
          .transact(xa)
          .orDie
    }
}

object DoobieAccountRepository {

  object SQL {
    def create(user: User): Update0 =
      sql"""
        INSERT INTO ACCOUNTS (first_name, last_name, email)
        VALUES (${user.firstName}, ${user.lastName}, ${user.email})
      """.update

    def getById(id: AccountId): Query0[Account] =
      sql"""
        SELECT * FROM ACCOUNTS WHERE id = ${id.value}
      """.query[Account]
  }

}
