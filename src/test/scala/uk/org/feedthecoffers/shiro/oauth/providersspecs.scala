package uk.org.feedthecoffers.shiro.oauth

import org.specs2.matcher._
import org.specs2.mock.Mockito
import org.junit.Test
import org.specs2.mutable.SpecificationWithJUnit
import org.junit.matchers.JUnitMatchers

trait JUnitSpecification extends SpecificationWithJUnit with JUnitMustMatchers 

class ProviderFactoryTest extends JUnitSpecification {

  override def is =
    
    "Specification for the ProviderFactory".title                                                   ^
                                                                                                    p^
    "A ProviderFactory should"                                                                      ^
      "return a valid provider when passed a known provider name"                                   ^ testKnownProvider^
      "throw an IllegalArgumentException when passed an unknown provider"                           ^ testUnknownProvider^
      "throw an IllegalArgumentException when passed an null argument"                              ^ testNullProvider^
                                                                                                    endp^
                                                                                                    end

  def testUnknownProvider = new ProviderFactory().getProvider("testUnknownProvider") must throwA(new IllegalArgumentException("Unsupported OAuth provider: %s".format("testUnknownProvider")))

  def testNullProvider = new ProviderFactory().getProvider(null) must throwA(new IllegalArgumentException("Provider name must not be null"))

  def testKnownProvider = {
    val provider = new GoogleProvider
    provider.oAuthKey = "anonymous"
    provider.oAuthSecret = "anonymous"
    val factory = new ProviderFactory
    factory.providerMap = Map[String, Provider]("testKnownProvider".toLowerCase -> provider)
    factory.getProvider("testKnownProvider") must equalTo(provider)
  }
}

class GoogleProviderTest extends JUnitSpecification {
  
  override def is =
    "Specification for GoogleProvider".title														^
    																								p^
    "A GoogleProvider should"																		^
      "return a valid redirect URL"																	^ testRedirectUrl^
    																								endp^
    																								end

  def provider: AbstractProvider = {
    val provider = new GoogleProvider
    provider.oAuthKey = "anonymous"
    provider.oAuthSecret = "anonymous"
    provider.oAuthScope = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile"
    provider
  }
  
  def testRedirectUrl = provider.redirectUrl must startWith("https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=")
}
