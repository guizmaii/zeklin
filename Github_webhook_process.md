# Github App Webhooks

#### App installation

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
    body=""  /* See event_installation.json file */
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
    body=""  /* See event_integration_installation.json file */
```

The only difference between their body is the `"requester": null,` present in the `x-github-event: installation` event.

#### Random event ?

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