package com.guizmaii.zeklin.github

import java.nio.charset.StandardCharsets

import com.guizmaii.zeklin.github.config.GithubAppConfigs
import io.circe.Json
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import monocle.Optional
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Hex
import org.http4s.client.Client
import org.http4s.headers.{ `Content-Type`, Accept, Authorization }
import org.http4s.util.CaseInsensitiveString
import org.http4s.{ Headers, MediaType }
import pdi.jwt.{ JwtAlgorithm, JwtCirce, JwtClaim }
import zio.clock.Clock
import zio.{ RIO, Task, ZIO }

import scala.concurrent.duration._

trait Github {
  val github: Github.Service[Clock]
}

object Github {

  trait Service[R <: Clock] {

    def isValidWebhookSignature(requestHeaders: Headers, rawRequestBody: String): RIO[R, Boolean]

    def authenticateAsZeklin: RIO[R, Json]

    def createAppInstallationAccessToken(installationId: String): RIO[R, Json]

  }

  private[github] final val githubSignatureHeadeKey = CaseInsensitiveString("x-hub-signature")

  private[github] final val UTF_8 = StandardCharsets.UTF_8
  private[github] final val SHA1  = "HmacSHA1"

  private[github] final val githubAppMediaType: MediaType =
    new MediaType("application", "vnd.github.machine-man-preview+json")

  import io.circe.optics.JsonPath._
  final val _installationId: Optional[Json, Long] = root.installation.id.long
  final val _action: Optional[Json, String]       = root.action.string

}

final class GithubLive(client: Client[Task], githubConfig: GithubAppConfigs) extends Github {
  import Github._
  import org.http4s._
  import zio.interop.catz._

  private val githubWebhookSecretSpec = new SecretKeySpec(githubConfig.webhookSecret.getBytes(Github.UTF_8), SHA1)

  private val newJwtToken: ZIO[Clock, Nothing, String] =
    ZIO.accessM { env =>
      import env.clock._

      for {
        now <- currentDateTime.map(_.toInstant)
        claim = JwtClaim(
          expiration = Some(now.plusSeconds(10.minutes.toSeconds).getEpochSecond),
          issuedAt = Some(now.getEpochSecond),
          issuer = Some(githubConfig.id)
        )
      } yield JwtCirce.encode(claim, githubConfig.privateKey, JwtAlgorithm.RS256)
    }

  private def newRequest(uri: Uri): ZIO[Clock, Nothing, Request[Task]] =
    for {
      token <- newJwtToken
    } yield Request[Task](
      method = Method.POST,
      uri = uri,
      headers = Headers.of(
        Authorization(Credentials.Token(AuthScheme.Bearer, token)),
        `Content-Type`(MediaType.application.json),
        Accept(githubAppMediaType)
      )
    )

  override final val github =
    new Github.Service[Clock] {

      /**
       * Algorithm comes from here: https://github.com/github-developer/github-app-template/blob/master/template_server.rb#L132
       */
      override def isValidWebhookSignature(requestHeaders: Headers, rawRequestBody: String): RIO[Clock, Boolean] = {

        /**
         * More infos:
         *   - https://github.com/danharper/hmac-examples
         *   - https://stackoverflow.com/a/9655275/2431728
         *
         * Rq: The Mac instance should be recreated on each call otherwise the results aren't consistent...
         */
        def sha1HexDigest(string: String): Task[String] =
          ZIO.effect {
            val instance = Mac.getInstance(SHA1, BouncyCastleProvider.PROVIDER_NAME)
            instance.init(githubWebhookSecretSpec)
            Hex.toHexString(instance.doFinal(string.getBytes(UTF_8)))
          }

        val headerT =
          ZIO
            .fromOption(requestHeaders.find(_.name == githubSignatureHeadeKey))
            .mapError { _ =>
              new RuntimeException(
                s"Malformed Github webhook request: Missing ${Github.githubSignatureHeadeKey} header"
              )
            }

        for {
          githubSignatureHeader <- headerT
          res <- githubSignatureHeader.value.split("=", 2) match {
                  case Array(method, theirDigest) if method == "sha1" =>
                    sha1HexDigest(rawRequestBody).map(_ == theirDigest)
                  case Array(method, _) if method != "sha1" =>
                    ZIO.fail(
                      new RuntimeException(
                        s"Malformed Github signature header - Signature method not handled: $githubSignatureHeader"
                      )
                    )
                  case _ =>
                    ZIO.fail(new RuntimeException(s"Malformed Github signature header value: $githubSignatureHeader"))
                }
        } yield res
      }

      /**
       * Algorithm comes from https://github.com/github-developer/github-app-template/blob/master/template_server.rb#L94
       */
      override def authenticateAsZeklin: RIO[Clock, Json] = {
        import org.http4s.implicits._
        import org.http4s.circe._

        for {
          req  <- newRequest(uri"https://api.github.com/app")
          resp <- client.fetchAs[Json](req)
        } yield resp
      }

      /*
       * TODO: Find the correct return type.
       */
      override def createAppInstallationAccessToken(installationId: String): RIO[Clock, Json] = {
        import org.http4s.circe._

        for {
          req <- newRequest(
                  Uri.unsafeFromString(s"https://api.github.com/app/installations/$installationId/access_tokens")
                )
          resp <- client.fetchAs[Json](req)
        } yield resp
      }
    }
}
