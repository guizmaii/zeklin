package com.guizmaii.zeklin.accounts

import cats.data.EitherT
import cats.effect.Sync
import cats.effect.concurrent.Ref
import eidos.Id

final case class User(firstName: String, lastName: String, email: String)
final case class Account(id: Id[AccountId], user: User)

object Accounts {
  final case object AccountCreationException extends RuntimeException("Account creation failed")
  type AccountCreationException = AccountCreationException.type
}

final class Accounts {

  import Accounts._

  // TODO 0: Rewrite with ZIO.
  def create[F[_]: Sync: AccountRepository](user: User): F[Either[AccountCreationException, Account]] =
    (for {
      id <- EitherT.fromOptionF(AccountId.random[F], AccountCreationException)
      account = Account(id = id, user = user)
      _ <- EitherT.liftF(AccountRepository[F].save(account))
    } yield account).value

}

final class AccountRepository[F[_]](db: Ref[F, Map[Id[AccountId], Account]]) {

  def save(account: Account): F[Unit] = db.update(_ + (account.id -> account))

}

object AccountRepository {

  implicit def apply[F[_]](implicit s: AccountRepository[F]): AccountRepository[F] = s

}
