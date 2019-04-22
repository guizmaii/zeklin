package com.guizmaii.zeklin

import java.util.UUID

import cats.effect.Sync
import eidos.Id
import eidos.id.Format.{UUID => IdUUID}
import eidos.id.Label.CustomLabel

package object accounts {

  final case object AccountId extends CustomLabel with IdUUID {
    override val label: String = "AccountId"

    def random[F[_]: Sync]: F[Option[Id[AccountId]]] = Sync[F].delay(Id.of[AccountId](UUID.randomUUID().toString))
  }
  type AccountId = AccountId.type

}
