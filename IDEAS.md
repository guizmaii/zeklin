# Ideas

#### Jargon used in this document

 - **Github Event**         *(GE)*    : Message pushed in the system by the Github webhooks
 - **Zeklin Webhook Event** *(ZWE)*   : GE and the associated headers
 - **Kafka Topic**          *(KT)*    : A Kafka Topic
 
### 0. Authorizing Github Events

 1. Receive GE
 2. Create a ZWE
 
    A ZWE is composed of 2 parts:
     - headers: contains the headers received with the GE
     - body   : contains the GE data
 
 3. Put the ZWE in a KT: `zeklin-github-webhook-event` (properties: TOFIND)
 4. With a Kafka Stream, from `zeklin-github-webhook-event`, validate the `x-hub-signature` header value
    1. if invalid   : put the ZWE in `zeklin-github-webhook-event-invalid` (properties: TOFIND)
    2. if valid     : put the ZWE in `zeklin-github-webhook-event-valid` (properties: non compacted, last forever)

### 1. Maintaining the "current state" of an installation

 1. Read valid ZWE from `zeklin-github-webhook-event-valid`
 2. With a Kafka Stream (KTable), compute the current state
 3. Persist that current state in a KT `zeklin-github-installation-state` (properties: compacted, last forever)