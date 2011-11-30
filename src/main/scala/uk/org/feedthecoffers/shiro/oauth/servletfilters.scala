package uk.org.feedthecoffers.shiro.oauth

import org.apache.shiro.web.servlet.OncePerRequestFilter
import javax.servlet.{ ServletRequest, ServletResponse, FilterChain }
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import java.net.URL

class LoginRequestServletFilter extends OncePerRequestFilter with grizzled.slf4j.Logging {

  @Override
  override protected def doFilterInternal(request: ServletRequest, response: ServletResponse, chain: FilterChain) = {
    /*debug("Checking if this is an HttpServletRequestprovider - can't do anything if not...")
    (request, response) match {
      case (httpRequest: HttpServletRequest, httpResponse: HttpServletResponse) =>
        val baseCallbackUrl = new URL(request.getScheme(),
          httpRequest.getServerName(),
          httpRequest.getServerPort(),
          "")
        val oauthProviderName = httpRequest.getParameter(OAuthLoginFilter.oauthProviderParameter)
        val oAuthType = OAuthType(oauthProviderName)
        val oauthProvider = ProviderFactory(oAuthType, baseCallbackUrl.toString)
        var oAuthUser = new OAuthUser(oAuthType = oAuthType, baseRedirectUri = baseCallbackUrl.toString)
        val authUrl = oauthProvider.buildRedirectUrl(oAuthUser)
        oAuthUser = new OAuthService().addAuthorizationUrl(oAuthUser)
        debug("OAuth authorisation URL: %s".format(authUrl))
        httpRequest.getSession.setAttribute("user", oAuthUser)
        httpResponse.sendRedirect(oAuthUser.oAuthAuthorizationUrl)
      case _ =>
        debug("Response is not a HttpServletResponse so cannot redirect it :-(")
    }*/

  }

}