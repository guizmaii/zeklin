# Github App Webhooks

#### App installation

At app Installation, you'll receive these 2 webhooks:

  - One with `x-github-event: installation` header

```json
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: installation, 
        x-github-delivery: b23dc5c0-e18a-11e9-8a5a-99799ce08c06, 
        x-hub-signature: sha1=e77ab0aca6e61b8f895b41afb323c77a265703c9, 
        x-request-id: 659c0929-114e-481c-ab9a-55bbac64abce, 
        x-forwarded-for: 192.30.252.90, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 0, 
        x-request-start: 1569632142643, 
        total-route-time: 0, 
        content-length: 2550, 
        timestamp: 1569632142646
    ) 
    body="
    {
        "action":"created",
        "installation":
        {
            "id":2258044,
            "account":
            {
                "login":"guizmaii",
                "id":1193670,
                "node_id":"MDQ6VXNlcjExOTM2NzA=",
                "avatar_url":"https://avatars3.githubusercontent.com/u/1193670?v=4",
                "gravatar_id":"",
                "url":"https://api.github.com/users/guizmaii",
                "html_url":"https://github.com/guizmaii",
                "followers_url":"https://api.github.com/users/guizmaii/followers",
                "following_url":"https://api.github.com/users/guizmaii/following{/other_user}",
                "gists_url":"https://api.github.com/users/guizmaii/gists{/gist_id}",
                "starred_url":"https://api.github.com/users/guizmaii/starred{/owner}{/repo}",
                "subscriptions_url":"https://api.github.com/users/guizmaii/subscriptions",
                "organizations_url":"https://api.github.com/users/guizmaii/orgs",
                "repos_url":"https://api.github.com/users/guizmaii/repos",
                "events_url":"https://api.github.com/users/guizmaii/events{/privacy}",
                "received_events_url":"https://api.github.com/users/guizmaii/received_events",
                "type":"User",
                "site_admin":false
            },
            "repository_selection":"selected",
            "access_tokens_url":"https://api.github.com/app/installations/2258044/access_tokens",
            "repositories_url":"https://api.github.com/installation/repositories",
            "html_url":"https://github.com/settings/installations/2258044",
            "app_id":35516,
            "target_id":1193670,
            "target_type":"User",
            "permissions":
            {
                "administration":"read",
                "checks":"write",
                "contents":"read",
                "metadata":"read",
                "pull_requests":"write"
            },
            "events":[],
            "created_at":1569632142,
            "updated_at":1569632142,
            "single_file_name":null
        },
        "repositories":
        [
            {
                "id":163209183,
                "node_id":"MDEwOlJlcG9zaXRvcnkxNjMyMDkxODM=",
                "name":"zeklin",
                "full_name":"guizmaii/zeklin","private":false
            }
        ],
        "requester":null,
        "sender":
        {
            "login":"guizmaii",
            "id":1193670,
            "node_id":"MDQ6VXNlcjExOTM2NzA=",
            "avatar_url":"https://avatars3.githubusercontent.com/u/1193670?v=4",
            "gravatar_id":"",
            "url":"https://api.github.com/users/guizmaii",
            "html_url":"https://github.com/guizmaii",
            "followers_url":"https://api.github.com/users/guizmaii/followers",
            "following_url":"https://api.github.com/users/guizmaii/following{/other_user}",
            "gists_url":"https://api.github.com/users/guizmaii/gists{/gist_id}",
            "starred_url":"https://api.github.com/users/guizmaii/starred{/owner}{/repo}",
            "subscriptions_url":"https://api.github.com/users/guizmaii/subscriptions",
            "organizations_url":"https://api.github.com/users/guizmaii/orgs",
            "repos_url":"https://api.github.com/users/guizmaii/repos",
            "events_url":"https://api.github.com/users/guizmaii/events{/privacy}",
            "received_events_url":"https://api.github.com/users/guizmaii/received_events",
            "type":"User",
            "site_admin":false
        }
    }
    "
```

  - One with `x-github-event: integration_installation` header

```json
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: integration_installation, 
        x-github-delivery: b23dc5c0-e18a-11e9-8dff-cb54d3b1229d, 
        x-hub-signature: sha1=ed36018c0fb13feeb7caedc5dbb713699408d72b, 
        x-request-id: 8decf471-cabd-4427-89b0-010e1e0e54cc, 
        x-forwarded-for: 140.82.115.251, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 1, 
        x-request-start: 1569632142741, 
        total-route-time: 0, 
        content-length: 2533, 
        timestamp: 1569632142743
    ) 
    body="
    {
        "action":"created",
        "installation":
        {
            "id":2258044,
            "account":
            {
                "login":"guizmaii",
                "id":1193670,
                "node_id":"MDQ6VXNlcjExOTM2NzA=",
                "avatar_url":"https://avatars3.githubusercontent.com/u/1193670?v=4",
                "gravatar_id":"",
                "url":"https://api.github.com/users/guizmaii",
                "html_url":"https://github.com/guizmaii",
                "followers_url":"https://api.github.com/users/guizmaii/followers",
                "following_url":"https://api.github.com/users/guizmaii/following{/other_user}",
                "gists_url":"https://api.github.com/users/guizmaii/gists{/gist_id}",
                "starred_url":"https://api.github.com/users/guizmaii/starred{/owner}{/repo}",
                "subscriptions_url":"https://api.github.com/users/guizmaii/subscriptions",
                "organizations_url":"https://api.github.com/users/guizmaii/orgs",
                "repos_url":"https://api.github.com/users/guizmaii/repos",
                "events_url":"https://api.github.com/users/guizmaii/events{/privacy}",
                "received_events_url":"https://api.github.com/users/guizmaii/received_events",
                "type":"User",
                "site_admin":false
            },
            "repository_selection":"selected",
            "access_tokens_url":"https://api.github.com/app/installations/2258044/access_tokens",
            "repositories_url":"https://api.github.com/installation/repositories",
            "html_url":"https://github.com/settings/installations/2258044",
            "app_id":35516,
            "target_id":1193670,
            "target_type":"User",
            "permissions":
            {
                "administration":"read",
                "checks":"write",
                "contents":"read",
                "metadata":"read",
                "pull_requests":"write"
            },
            "events":[],
            "created_at":1569632142,
            "updated_at":1569632142,
            "single_file_name":null
        },
        "repositories":
        [
            {
                "id":163209183,
                "node_id":"MDEwOlJlcG9zaXRvcnkxNjMyMDkxODM=",
                "name":"zeklin",
                "full_name":"guizmaii/zeklin",
                "private":false
            }
        ],
        "sender":
        {
            "login":"guizmaii",
            "id":1193670,
            "node_id":"MDQ6VXNlcjExOTM2NzA=",
            "avatar_url":"https://avatars3.githubusercontent.com/u/1193670?v=4",
            "gravatar_id":"",
            "url":"https://api.github.com/users/guizmaii",
            "html_url":"https://github.com/guizmaii",
            "followers_url":"https://api.github.com/users/guizmaii/followers",
            "following_url":"https://api.github.com/users/guizmaii/following{/other_user}",
            "gists_url":"https://api.github.com/users/guizmaii/gists{/gist_id}",
            "starred_url":"https://api.github.com/users/guizmaii/starred{/owner}{/repo}",
            "subscriptions_url":"https://api.github.com/users/guizmaii/subscriptions",
            "organizations_url":"https://api.github.com/users/guizmaii/orgs",
            "repos_url":"https://api.github.com/users/guizmaii/repos",
            "events_url":"https://api.github.com/users/guizmaii/events{/privacy}",
            "received_events_url":"https://api.github.com/users/guizmaii/received_events",
            "type":"User",
            "site_admin":false
        }
    }
    "
```

They have *almost* the same body.
