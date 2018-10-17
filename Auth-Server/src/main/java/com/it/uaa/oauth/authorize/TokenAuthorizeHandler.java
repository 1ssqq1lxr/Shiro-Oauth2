package com.it.uaa.oauth.authorize;

import com.it.uaa.domain.Oauth2AccessToken;
import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.oauth.OAuthAuthxRequest;
import com.it.uaa.oauth.validator.AbstractClientDetailsValidator;
import com.it.uaa.oauth.validator.authorize.TokenClientDetailsValidator;
import com.it.uaa.web.WebUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description: token 模式处理授权
 */
public class TokenAuthorizeHandler extends AbstractAuthorizeHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthorizeHandler.class);


    public TokenAuthorizeHandler(OAuthAuthxRequest oauthRequest, HttpServletResponse response) {
        super(oauthRequest, response);
    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new TokenClientDetailsValidator(oauthRequest);
    }


    @Override
    protected void handleResponse() throws OAuthSystemException, IOException {

        if (forceNewAccessToken()) {
            forceTokenResponse();
        } else {
            Oauth2AccessToken accessToken = oauthService.retrieveAccessToken(clientDetails(), oauthRequest.getScopes(), false);
            if (accessToken.tokenExpired()) {
                expiredTokenResponse(accessToken);
            } else {
                normalTokenResponse(accessToken);
            }
        }
    }

    private void forceTokenResponse() throws OAuthSystemException {
        Oauth2AccessToken accessToken = oauthService.retrieveNewAccessToken(clientDetails(), oauthRequest.getScopes());
        normalTokenResponse(accessToken);
    }

    private void normalTokenResponse(Oauth2AccessToken accessToken) throws OAuthSystemException {

         OAuthResponse oAuthResponse = createTokenResponse(accessToken, true);
        LOG.debug("'token' response: {}", oAuthResponse);

        WebUtils.writeOAuthQueryResponse(response, oAuthResponse);
    }

    private void expiredTokenResponse(Oauth2AccessToken accessToken) throws OAuthSystemException {
        Oauth2ClientDetails clientDetails = clientDetails();
        LOG.debug("Oauth2AccessToken {} is expired", accessToken);

         OAuthResponse oAuthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                .setError(OAuthError.ResourceResponse.EXPIRED_TOKEN)
                .setErrorDescription("access_token '" + accessToken.getClientId() + "' expired")
                .setErrorUri(clientDetails.getRedirectUri())
                .buildJSONMessage();

        WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
    }

    private boolean forceNewAccessToken() {
         Oauth2ClientDetails clientDetails = clientDetails();
        if (clientDetails.isTrusted()) {
            return userFirstLogged;
        } else {
            return userFirstApproved;
        }
    }
}
