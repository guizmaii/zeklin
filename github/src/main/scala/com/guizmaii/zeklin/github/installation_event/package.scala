package com.guizmaii.zeklin.github.installation_event

import java.time.Instant

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

sealed trait InstallationEventAction
object InstallationEventAction {
  final case object Created                extends InstallationEventAction
  final case object Deleted                extends InstallationEventAction
  final case object NewPermissionsAccepted extends InstallationEventAction

  /*
   * https://dzone.com/articles/5-useful-circe-feature-you-may-have-overlooked
   */
  import io.circe.generic.extras.semiauto.deriveEnumerationDecoder
  implicit final val decoder: Decoder[InstallationEventAction] = deriveEnumerationDecoder
}

final case class InstallationEvent(
  event: String,
  payload: Payload
)

final case class Payload(
  action: String,
  installation: Installation,
  repositories: List[Repositories],
  sender: Account
)

final case class Installation(
  id: Int,
  account: Account,
  repository_selection: String,
  access_tokens_url: String,
  repositories_url: String,
  html_url: String,
  app_id: Int,
  target_id: Int,
  target_type: String,
  permissions: Permissions,
  events: List[String],
  created_at: Instant,
  updated_at: Instant,
  single_file_name: String
)

final case class Permissions(
  metadata: String,
  contents: String,
  issues: String
)

final case class Account(
  login: String,
  id: Int,
  node_id: String,
  avatar_url: String,
  gravatar_id: Option[String],
  url: String,
  html_url: String,
  followers_url: String,
  following_url: String,
  gists_url: String,
  starred_url: String,
  subscriptions_url: String,
  organizations_url: String,
  repos_url: String,
  events_url: String,
  received_events_url: String,
  `type`: String,
  site_admin: Boolean
)

final case class Repositories(
  id: Int,
  name: String,
  full_name: String,
  `private`: Boolean
)

object InstallationEvent {
  implicit final val decoder: Decoder[InstallationEvent] = deriveDecoder
}
object Payload {
  implicit final val decoder: Decoder[Payload] = deriveDecoder
}
object Installation {
  import io.circe.java8.time._
  implicitly[Decoder[Instant]]
  implicit final val decoder: Decoder[Installation] = deriveDecoder
}
object Permissions {
  implicit final val decoder: Decoder[Permissions] = deriveDecoder
}
object Account {
  implicit final val decoder: Decoder[Account] = deriveDecoder
}
object Repositories {
  implicit final val decoder: Decoder[Repositories] = deriveDecoder
}
