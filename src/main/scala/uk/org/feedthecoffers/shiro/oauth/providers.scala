package uk.org.feedthecoffers.shiro.oauth

import java.io.ByteArrayInputStream
import org.scribe.builder.api.GoogleApi
import org.scribe.model.OAuthRequest
import java.net.URL
import org.scribe.builder.api.TwitterApi
import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.Api
import org.scribe.model.Verifier
import org.scribe.model.Token
import org.scribe.model.Verb
import org.apache.shiro.util.StringUtils
import org.scribe.oauth.OAuthService

class ProviderFactory {

  var providerMap = Map[String, Provider]()

  def getProvider(providerName: String, callbackBaseUrl: String = null): Provider = {
    providerName match {
      case null => throw new IllegalArgumentException("Provider name must not be null")
      case s: String =>
        try {
          providerMap(s.toLowerCase)
        } catch {
          case e: NoSuchElementException => throw new IllegalArgumentException("Unsupported OAuth provider: %s".format(providerName))
        }
    }

  }
}

trait Provider {

  def redirectUrl: String

}

abstract class AbstractProvider extends Provider with grizzled.slf4j.Logging {

  var callbackBaseUrl: String = null

  var callbackUrlPrefix: String = null

  var oAuthKey: String = null

  var oAuthSecret: String = null

  var oAuthScope: String = null

  private var oAuthService: OAuthService = null

  protected def providerName: String

  protected def providerApiClass: Class[_ <: Api]

  def redirectUrl = {
    val token = getOAuthService.getRequestToken
    trace("requestToken: %s".format(token))

    val redirectUrl = getOAuthService.getAuthorizationUrl(token)
    trace("Redirect URL: %s".format(redirectUrl))

    redirectUrl
  }

  protected def callbackUrl = {
    val finalCallbackBaseUrl = if (StringUtils.hasLength(callbackBaseUrl)) callbackBaseUrl else "http://localhost:9090"
    "%s%s%s".format(finalCallbackBaseUrl, callbackUrlPrefix, providerName.toLowerCase)
  }

  def getOAuthService = {
    if (oAuthService == null) {
      val serviceBuilder = new ServiceBuilder()
        .provider(providerApiClass)
        .apiKey(oAuthKey)
        .apiSecret(oAuthSecret)
        .callback(callbackUrl)
        
      if (oAuthScope != null) {
        serviceBuilder.scope(oAuthScope)
      }
      oAuthService = serviceBuilder.build
    }
    oAuthService
  }

}

class GoogleProvider extends AbstractProvider {

  override protected def providerName: String = "google"

  override protected def providerApiClass: Class[_ <: Api] = classOf[GoogleApi]

}

class TwitterProvider extends AbstractProvider {

  override protected def providerName: String = "twitter"

  override protected def providerApiClass: Class[_ <: Api] = classOf[TwitterApi]

}
