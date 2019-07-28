package com.guizmaii.zeklin.github.installation_event

import cats.scalatest.ValidatedMatchers
import org.scalatest.{ FlatSpec, Matchers }

class DecodersSpec extends FlatSpec with Matchers with ValidatedMatchers {

  behavior of "InstallationEvent decoder"

  it should "decode the 'installation' event" in {
    val json: String =
      """
        |{
        |  "event":"installation",
        |  "payload":{
        |    "action":"created",
        |    "installation":{
        |      "id":1332619,
        |      "account":{
        |        "login":"guizmaii",
        |        "id":1193670,
        |        "node_id":"MDQ6VXNlcjExOTM2NzA=",
        |        "avatar_url":"https://avatars3.githubusercontent.com/u/1193670?v=4",
        |        "gravatar_id":"",
        |        "url":"https://api.github.com/users/guizmaii",
        |        "html_url":"https://github.com/guizmaii",
        |        "followers_url":"https://api.github.com/users/guizmaii/followers",
        |        "following_url":"https://api.github.com/users/guizmaii/following{/other_user}",
        |        "gists_url":"https://api.github.com/users/guizmaii/gists{/gist_id}",
        |        "starred_url":"https://api.github.com/users/guizmaii/starred{/owner}{/repo}",
        |        "subscriptions_url":"https://api.github.com/users/guizmaii/subscriptions",
        |        "organizations_url":"https://api.github.com/users/guizmaii/orgs",
        |        "repos_url":"https://api.github.com/users/guizmaii/repos",
        |        "events_url":"https://api.github.com/users/guizmaii/events{/privacy}",
        |        "received_events_url":"https://api.github.com/users/guizmaii/received_events",
        |        "type":"User",
        |        "site_admin":false
        |      },
        |      "repository_selection":"selected",
        |      "access_tokens_url":"https://api.github.com/app/installations/1332619/access_tokens",
        |      "repositories_url":"https://api.github.com/installation/repositories",
        |      "html_url":"https://github.com/settings/installations/1332619",
        |      "app_id":35516,
        |      "target_id":1193670,
        |      "target_type":"User",
        |      "permissions":{
        |        "checks":"write",
        |        "pull_requests":"write",
        |        "administration":"read",
        |        "contents":"read",
        |        "metadata":"read"
        |      },
        |      "events":[
        |
        |      ],
        |      "created_at":1564224045,
        |      "updated_at":1564224045,
        |      "single_file_name":null
        |    },
        |    "repositories":[
        |      {
        |        "id":163209183,
        |        "node_id":"MDEwOlJlcG9zaXRvcnkxNjMyMDkxODM=",
        |        "name":"zeklin",
        |        "full_name":"guizmaii/zeklin",
        |        "private":false
        |      }
        |    ],
        |    "sender":{
        |      "login":"guizmaii",
        |      "id":1193670,
        |      "node_id":"MDQ6VXNlcjExOTM2NzA=",
        |      "avatar_url":"https://avatars3.githubusercontent.com/u/1193670?v=4",
        |      "gravatar_id":"",
        |      "url":"https://api.github.com/users/guizmaii",
        |      "html_url":"https://github.com/guizmaii",
        |      "followers_url":"https://api.github.com/users/guizmaii/followers",
        |      "following_url":"https://api.github.com/users/guizmaii/following{/other_user}",
        |      "gists_url":"https://api.github.com/users/guizmaii/gists{/gist_id}",
        |      "starred_url":"https://api.github.com/users/guizmaii/starred{/owner}{/repo}",
        |      "subscriptions_url":"https://api.github.com/users/guizmaii/subscriptions",
        |      "organizations_url":"https://api.github.com/users/guizmaii/orgs",
        |      "repos_url":"https://api.github.com/users/guizmaii/repos",
        |      "events_url":"https://api.github.com/users/guizmaii/events{/privacy}",
        |      "received_events_url":"https://api.github.com/users/guizmaii/received_events",
        |      "type":"User",
        |      "site_admin":false
        |    }
        |  }
        |}
        |""".stripMargin

    import io.circe.parser._
    decodeAccumulating[Payload](json) should be(valid)
  }

}
