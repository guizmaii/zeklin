package com.guizmaii.zeklin.github

import java.nio.charset
import java.nio.charset.StandardCharsets

import com.guizmaii.zeklin.github.config.GithubAppConfigs
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Hex
import org.http4s.client.Client
import org.http4s.headers.{ `Content-Type`, Accept, Authorization }
import org.http4s.util.CaseInsensitiveString
import org.http4s.{ Headers, MediaType }
import pdi.jwt.{ JwtAlgorithm, JwtCirce, JwtClaim }
import zio.clock.Clock
import zio.{ Task, TaskR, ZIO }

import scala.concurrent.duration._

trait Github {
  val github: Github.Service[Clock]
}

object Github {

  trait Service[R <: Clock] {

    def isValidWebhookSignature(requestHeaders: Headers, rawRequestBody: String): TaskR[R, Boolean]

    def authenticateApp: TaskR[R, String]

    def authenticateAppInstallation(installationId: Long): TaskR[R, String]

  }

  final val githubSignatureHeadeKey = CaseInsensitiveString("x-hub-signature")

  final val UTF_8: charset.Charset = StandardCharsets.UTF_8
  final val SHA1: String           = "HmacSHA1"

  private[github] final val githubAppMediaType: MediaType =
    new MediaType("application", "vnd.github.machine-man-preview+json")

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
      override def isValidWebhookSignature(requestHeaders: Headers, rawRequestBody: String): TaskR[Clock, Boolean] = {

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
      override def authenticateApp: TaskR[Clock, String] =
        for {
          req  <- newRequest(uri"https://api.github.com/app")
          resp <- client.fetchAs[String](req)
        } yield resp

      override def authenticateAppInstallation(installationId: Long): TaskR[Clock, String] =
        for {
          req <- newRequest(
                  Uri.unsafeFromString(s"https://api.github.com/app/installations/$installationId/access_tokens")
                )
          resp <- client.fetchAs[String](req)
        } yield resp
    }
}
