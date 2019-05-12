package com.guizmaii.zeklin

import scalaz.zio.ZIO

package object accounts extends AccountRepository.Service[AccountRepository] {

  override def createAccount(user: User): ZIO[AccountRepository, Nothing, Account] =
    ZIO.accessM(_.accountRepository.createAccount(user))

  override def getById(id: AccountId): ZIO[AccountRepository, Nothing, Option[Account]] =
    ZIO.accessM(_.accountRepository.getById(id))

}
