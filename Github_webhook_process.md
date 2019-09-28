# Github App Webhooks

#### Github App - App installation

At app Installation, you'll receive these 2 webhooks:

  - One with `x-github-event: installation` header

```
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
    body=""  /* See event_installation_app_install.json file */
```

  - One with `x-github-event: integration_installation` header

```
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
    body=""  /* See event_integration_installation_app_install.json file */
```

The only difference between their body is the `"requester": null,` present in the `x-github-event: installation` event.

#### Github App - App deletion (uninstall)

At app deletion (uninstall), you'll receive these 2 webhooks:

  - One with `x-github-event: installation` header

```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: installation, 
        x-github-delivery: eb2f4e8a-e1cb-11e9-9753-077501bcd535, 
        x-hub-signature: sha1=fc4abef36b74665a7747178439d536ce4010d846, 
        x-request-id: 35bcbf2d-7cb2-4946-b1e0-224e96e9ef9e, 
        x-forwarded-for: 140.82.115.245, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 0, 
        x-request-start: 1569660155444, 
        total-route-time: 0, 
        content-length: 2424, 
        timestamp: 1569660155446
    )
    body=""  /* See event_installation_app_deleted.json file */
```

  - One with `x-github-event: integration_installation` header

```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: integration_installation, 
        x-github-delivery: eb2820a6-e1cb-11e9-970b-55d49d32657b, 
        x-hub-signature: sha1=fc4abef36b74665a7747178439d536ce4010d846, 
        x-request-id: eadc09bf-f364-4fc1-aec3-0c5495d06474, 
        x-forwarded-for: 192.30.252.88, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 1, 
        x-request-start: 1569660155436, 
        total-route-time: 0, 
        content-length: 2424, 
        timestamp: 1569660155438
    )
    body=""  /* See event_integration_installation_app_deleted.json file */
```

The events body are identical.

#### Github App - Add a repo to the App

These events are triggered when a repo is added to the Github App.

  - First one, `x-github-event: installation_repositories` event
  
```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: installation_repositories, 
        x-github-delivery: 70c757b0-e1c5-11e9-90d9-db311bf2cb9d, 
        x-hub-signature: sha1=5e7ddd83a40cbadd0417493c5e322dcb14a06b39, 
        x-request-id: 75971a0f-a569-41b8-9a14-42d7e992c411, 
        x-forwarded-for: 140.82.115.248, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 0, 
        x-request-start: 1569657373160, 
        total-route-time: 0, 
        content-length: 2635, 
        timestamp: 1569657373162
    ) 
    body=""  /* See event_installation_repositories_added.json file */
```

  - Second one, `x-github-event: integration_installation_repositories` event
  
```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: integration_installation_repositories, 
        x-github-delivery: 70c757b0-e1c5-11e9-9210-0d99602bc205, 
        x-hub-signature: sha1=5e7ddd83a40cbadd0417493c5e322dcb14a06b39, 
        x-request-id: a9e29ff5-f2bb-4cb1-9db6-f5085b8c1b57, 
        x-forwarded-for: 192.30.252.97, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 0, 
        x-request-start: 1569657373091, 
        total-route-time: 0, 
        content-length: 2635, 
        timestamp: 1569657373094
    ) 
    body=""  /* See event_integration_installation_repositories_added.json file */
```

The body of these events seems to be identical.

#### Github App - Remove a repo from the App

These events are triggered when a repo is added to the Github App.

  - First one, `x-github-event: installation_repositories` event
  
```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: installation_repositories, 
        x-github-delivery: e8a850f8-e1c6-11e9-847d-fa17b8ca849f, 
        x-hub-signature: sha1=da213704fe3248df706d77935caf03f02da137a2, 
        x-request-id: 45685033-88e5-40a0-9f63-cd63152fd210, 
        x-forwarded-for: 192.30.252.99, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 0, 
        x-request-start: 1569658003753, 
        total-route-time: 0, 
        content-length: 2677, 
        timestamp: 1569658003756
    )
    body=""  /* See event_installation_repositories_removed.json file */
```

  - Second one, `x-github-event: integration_installation_repositories` event
  
```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: integration_installation_repositories, 
        x-github-delivery: e8a850f8-e1c6-11e9-8853-4ea474e4d286, 
        x-hub-signature: sha1=da213704fe3248df706d77935caf03f02da137a2, 
        x-request-id: 4f0e8c5b-a845-42c8-9817-23165d694036, 
        x-forwarded-for: 140.82.115.244, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 0, 
        x-request-start: 1569658003662, 
        total-route-time: 0, 
        content-length: 2677, 
        timestamp: 1569658003665
    )
    body=""  /* See event_integration_installation_repositories_removed.json file */
```

The body of these events seems to be identical.

#### Github App - Go from "some repo only" to "all repos"

These events are triggered when the user choose to change the access rights of the App to its repo, 
from some of his repos to all his repos.

  - First one, `x-github-event: installation_repositories` event
  
```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: installation_repositories, 
        x-github-delivery: 41122dd0-e1c8-11e9-8370-d08b4aab4423, 
        x-hub-signature: sha1=8d731c92907298d9468a1355e268f74ca6bfa2f9, 
        x-request-id: 5658b2c9-8a8a-4787-a421-d542f78d2e65, 
        x-forwarded-for: 192.30.252.88, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 0, 
        x-request-start: 1569658582904, 
        total-route-time: 0, 
        content-length: 20108, 
        timestamp: 1569658582906
    )
    body=""  /* See event_installation_repositories_from_some_to_all.json file */
```

  - Second one, `x-github-event: integration_installation_repositories` event
  
```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: integration_installation_repositories, 
        x-github-delivery: 41122dd0-e1c8-11e9-97d0-17830efb127d, 
        x-hub-signature: sha1=11cb3f39afe3ce979a4b286e2284be92e0ee6b51, 
        x-request-id: 568a5249-d0bf-45b6-af5f-1a7d1064f741, 
        x-forwarded-for: 192.30.252.98, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 1, 
        x-request-start: 1569658582918, 
        total-route-time: 0, 
        content-length: 20103, 
        timestamp: 1569658582923
    )
    body=""  /* See event_integration_installation_repositories_from_some_to_all.json file */
```

Only difference between their body:
In the `installation_repositories` event the `"repository_selection"` is set to `"selected"`
while in the `integration_installation_repositories` event, it's set to `"all"`

TODO: Find Why?


#### Github App - Go from "all repos" to "some repo only"

These events are triggered when the user choose to change the access rights of the App to its repo, 
from all his repos to some of his repos.

  - First one, `x-github-event: integration_installation_repositories` event
  
```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: integration_installation_repositories, 
        x-github-delivery: 07913130-e1ca-11e9-86cd-f4e558452a4e, 
        x-hub-signature: sha1=34a32484c574d670637385979a3a090f5739fa3c, 
        x-request-id: 3381c1cf-ce8c-427c-a622-8f244935d015, 
        x-forwarded-for: 192.30.252.91, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 0, 
        x-request-start: 1569659344008, 
        total-route-time: 0, 
        content-length: 2592, 
        timestamp: 1569659344010
    )
    body=""  /* See event_integration_installation_repositories_from_all_to_some_added.json file */
```

  - Second one, `x-github-event: installation_repositories` event
  
```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: installation_repositories, 
        x-github-delivery: 07913130-e1ca-11e9-8c1c-15f214e75a9a, 
        x-hub-signature: sha1=34a32484c574d670637385979a3a090f5739fa3c, 
        x-request-id: cd9c6b2e-48b6-4f20-9b68-7d28b3716ef0, 
        x-forwarded-for: 140.82.115.251, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 1, 
        x-request-start: 1569659344038, 
        total-route-time: 0, 
        content-length: 2592, 
        timestamp: 1569659344040
    )
    body=""  /* See event_installation_repositories_from_all_to_some_added.json file */
```

  - Third one, `x-github-event: integration_installation_repositories` event
  
```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: integration_installation_repositories, 
        x-github-delivery: 079dd8ae-e1ca-11e9-8283-0aa404bedfa6, 
        x-hub-signature: sha1=b2fa1ab6b769ed202670897fbb285c15849b294a, 
        x-request-id: a21c2bcd-3355-435f-9a6d-06586d1b079e, 
        x-forwarded-for: 140.82.115.248, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 0, 
        x-request-start: 1569659344216, 
        total-route-time: 0, 
        content-length: 20319, 
        timestamp: 1569659344218
    )
    body=""  /* See event_integration_installation_repositories_from_all_to_some_removed.json file */
```

  - Fourth one, `x-github-event: installation_repositories` event
  
```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        ccept: */*, 
        x-github-event: installation_repositories, 
        x-github-delivery: 079dd8ae-e1ca-11e9-89f1-001d1d4487be, 
        x-hub-signature: sha1=b2fa1ab6b769ed202670897fbb285c15849b294a, 
        x-request-id: 008a66d4-1468-493a-a738-2373aac722e8, 
        x-forwarded-for: 192.30.252.90, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 1, 
        x-request-start: 1569659344236, 
        total-route-time: 0, 
        content-length: 20319, 
        timestamp: 1569659344279
    )
    body=""  /* See event_installation_repositories_from_all_to_some_removed.json file */
```

The events body are identical.

#### Git commit events

These `check_suite` events seems to happen when there's a commit.

  - First one

```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: check_suite, 
        x-github-delivery: 1ec06510-e1bb-11e9-9611-fe24f8c737f2, 
        x-hub-signature: sha1=e56e141da168fb458aa86b4bef823d5dedc7d407, 
        x-request-id: 174560b8-a9ed-4b08-8dc2-07c1a94f20bc, 
        x-forwarded-for: 140.82.115.245, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 0, 
        x-request-start: 1569652940534, 
        total-route-time: 0, 
        content-length: 8344, 
        timestamp: 1569652940537
    ) 
    body=""  /* See event_check_suite_0.json file */
```

  - Second one

```
INFO  o.h.s.m.Logger - HTTP/1.1 POST /webhook 
    Headers(
        host: smee.io, 
        Accept-Encoding: gzip, deflate, 
        user-agent: GitHub-Hookshot/3afdf3c, 
        content-type: application/json, 
        connection: close, 
        accept: */*, 
        x-github-event: check_suite, 
        x-github-delivery: 633d81a0-e1bb-11e9-9e91-7773f3307b54, 
        x-hub-signature: sha1=57c6051d1230dae0dd18026aebf883fe669dd3dd, 
        x-request-id: 0cd3f02a-6a38-48e3-96e8-61960f2f2c3d, 
        x-forwarded-for: 140.82.115.244, 
        x-forwarded-proto: https, 
        x-forwarded-port: 443, 
        via: 1.1 vegur, 
        connect-time: 3, 
        x-request-start: 1569653055395, 
        total-route-time: 0, 
        content-length: 8353, 
        timestamp: 1569653055412
    ) 
    body=""  /* See event_check_suite_1.json file */
```