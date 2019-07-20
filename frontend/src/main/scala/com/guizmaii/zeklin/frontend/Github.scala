package com.guizmaii.zeklin.frontend

import com.guizmaii.zeklin.frontend.config.GithubAppConfigs
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }
import org.http4s.client.Client
import org.http4s.headers.{ `Content-Type`, Accept }
import org.passay.{ CharacterRule, EnglishCharacterData, PasswordGenerator }
import zio.{ IO, Task, ZIO }

trait Github {
  val github: Github.Service[Any]
}

final case class OAuthToken(access_token: String, token_type: String, scope: String)
object OAuthToken {
  implicit final val decoder: Decoder[OAuthToken] = deriveDecoder[OAuthToken]
}

final case class NewOAuthRequest(
  client_id: String,
  client_secret: String,
  code: String,
  state: String
)
object NewOAuthRequest {
  implicit final val encoder: Encoder[NewOAuthRequest] = deriveEncoder[NewOAuthRequest]
}

object Github {
  trait Service[R] {

    def authorizeURl: ZIO[Any, Throwable, String]

    def fetchAccessToken(
      code: String,
      state: String
    ): ZIO[Any, Throwable, OAuthToken]

  }

  def authorizeURl: ZIO[Github, Throwable, String] = ZIO.accessM[Github](_.github.authorizeURl)

  def fetchAccessToken(
    code: String,
    state: String
  ): ZIO[Github, Throwable, OAuthToken] =
    ZIO.accessM(_.github.fetchAccessToken(code, state))

}

class GithubLive(client: Client[Task], config: GithubAppConfigs, passwordGenerator: PasswordGenerator) extends Github {
  import org.http4s._
  import org.http4s.circe.CirceEntityCodec._
  import zio.interop.catz._

  private val digits = new CharacterRule(EnglishCharacterData.Digit)
  private val alpha  = new CharacterRule(EnglishCharacterData.Alphabetical)
  private val lower  = new CharacterRule(EnglishCharacterData.LowerCase)
  private val upper  = new CharacterRule(EnglishCharacterData.UpperCase)

  private val newState: Task[String] = IO.effect(passwordGenerator.generatePassword(40, digits, alpha, lower, upper))

  override final val github: Github.Service[Any] =
    new Github.Service[Any] {
      override def fetchAccessToken(code: String, state: String): ZIO[Any, Throwable, OAuthToken] = {
        val req = Request[Task](
          method = Method.POST,
          uri = uri"https://github.com/login/oauth/access_token",
          headers = Headers.of(
            `Content-Type`(MediaType.application.json),
            Accept(MediaType.application.json)
          )
        ).withEntity(
          NewOAuthRequest(
            client_id = config.clientId,
            client_secret = config.clientSecret,
            code = code,
            state = state
          )
        )

        client.fetchAs[OAuthToken](req)
      }

      override def authorizeURl: ZIO[Any, Throwable, String] =
        newState.map { state =>
          s"https://github.com/login/oauth/authorize?scope=user:email&client_id=${config.clientId}&state=$state"
        }
    }
}
