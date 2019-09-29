package com.guizmaii.zeklin.github

object config {

  final case class GithubAppConfigs(
    id: String,
    clientId: String,
    clientSecret: String,
    webhookSecret: String,
    privateKey: String
  ) {
    override def toString: String =
      s"GithubAppConfigs(id: $id, clientId: $clientId, clientSecret: *****, webhookSecret: *****, privateKey: *****)"
  }

}
