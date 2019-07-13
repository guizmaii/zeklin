package com.guizmaii.zeklin.frontend

object config {

  final case class GithubConfigs(personalAccessToken: String, app: GithubAppConfigs)

  final case class GithubAppConfigs(id: String, clientId: String, clientSecret: String)

}
