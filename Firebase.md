# Firebase

### Authentication

Example of received data in the Firebase auth `signInSuccessWithAuthResult` callback:

```json
{
  "user": {
    "uid": "7qsdqsdhWwtmTQA7qdqdqsdqvO42",
    "displayName": "Jules Ivanic",
    "photoURL": "https://avatars3.githubusercontent.com/u/1193670?v=4",
    "email": "jules.ivanic@gmail.com",
    "emailVerified": false,
    "phoneNumber": null,
    "isAnonymous": false,
    "tenantId": null,
    "providerData": [
      {
        "uid": "1191230",
        "displayName": "Jules Ivanic",
        "photoURL": "https://avatars3.githubusercontent.com/u/1193670?v=4",
        "email": "jules.ivanic@gmail.com",
        "phoneNumber": null,
        "providerId": "github.com"
      }
    ],
    "apiKey": "AZERTYpFRENQSDQSDQGDQ8BFD7pF12345",
    "appName": "[DEFAULT]-firebaseui-temp",
    "authDomain": "zeklin-12345.firebaseapp.com",
    "stsTokenManager": {
      "apiKey": "AZERTYpFRENQSDQSDQGDQ8BFD7pF12345",
      "refreshToken": "QJDHKQJLDK+MQSLD%L£QSD%LQ-0mKjknn1tA6BLAln2DVeZXF5or3j-QSDJQKSDL.Q.SD?/Q.SD?QMSLD-2VjDzHfFtkXusR1j1-QLMSDLQSDMQSLDKMQKSLDKMQKSDLKMQLSKD-fhBMOiXFU1HCjodk9pchEN88rvOZhPOmDXL8gZptFGSZQI64zR1sjN_f-QSLMDQSDJMQSDLQLSDL-qsdqsdqlsmdklqKMLQKDBGOA",
      "accessToken": "QS%DMQS%DLMQS%MLGQKGBJLKSDLKQ%SMDK%Q.QLMSDKQMK%GQMDL%MKD%LQMSLDK%QLSMDL%QSMDL%QSD.QSDLQS%MDQ%KMDMLQS%DLMQLJGMQSJF-A9-QSDL%QMLGKDGJLDKFHLKQDJK?LQS?DCJBZLCKLJKSMQLLQDNSQ?.DNQSDQBS?.D.Q?SBDB?QD?Q?DSBQSDB.",
      "expirationTime": 1572006348986
    },
    "redirectEventId": null,
    "lastLoginAt": "1572002748768",
    "createdAt": "1572001728075"
  },
  "credential": {
    "providerId": "github.com",
    "signInMethod": "github.com",
    "oauthAccessToken": "12345678934b86f1ff61b51234567892e02"
  },
  "operationType": "signIn",
  "additionalUserInfo": {
    "providerId": "github.com",
    "isNewUser": false,
    "profile": {
      "gists_url": "https://api.github.com/users/guizmaii/gists{/gist_id}",
      "repos_url": "https://api.github.com/users/guizmaii/repos",
      "following_url": "https://api.github.com/users/guizmaii/following{/other_user}",
      "bio": null,
      "created_at": "2011-11-14T13:51:08Z",
      "login": "guizmaii",
      "type": "User",
      "blog": "http://guizmaii.github.io/",
      "subscriptions_url": "https://api.github.com/users/guizmaii/subscriptions",
      "updated_at": "2019-10-25T08:17:10Z",
      "site_admin": false,
      "company": null,
      "id": 1193123,
      "public_repos": 135,
      "gravatar_id": "",
      "email": null,
      "organizations_url": "https://api.github.com/users/guizmaii/orgs",
      "hireable": true,
      "starred_url": "https://api.github.com/users/guizmaii/starred{/owner}{/repo}",
      "followers_url": "https://api.github.com/users/guizmaii/followers",
      "public_gists": 53,
      "url": "https://api.github.com/users/guizmaii",
      "received_events_url": "https://api.github.com/users/guizmaii/received_events",
      "followers": 30,
      "avatar_url": "https://avatars3.githubusercontent.com/u/1193670?v=4",
      "events_url": "https://api.github.com/users/guizmaii/events{/privacy}",
      "html_url": "https://github.com/guizmaii",
      "following": 82,
      "name": "Jules Ivanic",
      "location": "France",
      "node_id": "MDQqdqsdFJMOTM2NzA="
    },
    "username": "guizmaii"
  }
}
```