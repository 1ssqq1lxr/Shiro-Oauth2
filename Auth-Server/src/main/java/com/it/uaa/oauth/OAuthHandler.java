package com.it.uaa.oauth;

import com.it.uaa.common.domain.shared.BeanProvider;
import com.it.uaa.domain.Oauth2AccessToken;
import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.service.OauthService;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  OAuthHandler 抽象类封装
 */
public abstract class OAuthHandler {



    protected transient OauthService oauthService = BeanProvider.getBean(OauthService.class);



    protected Oauth2ClientDetails clientDetails() {
        final String clientId = clientId();
        return oauthService.loadClientDetails(clientId);
    }



    protected OAuthResponse createTokenResponse(Oauth2AccessToken accessToken, boolean queryOrJson) throws OAuthSystemException {
        final Oauth2ClientDetails clientDetails = clientDetails();
        final OAuthASResponse.OAuthTokenResponseBuilder builder = OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .location(clientDetails.getRedirectUri())
                .setAccessToken(accessToken.getTokenId())
                .setRefreshToken(accessToken.getRefreshToken())
                .setExpiresIn(String.valueOf(accessToken.getTokenExpiredSeconds()))
                .setTokenType(accessToken.getTokenType());
        final String refreshToken = accessToken.getRefreshToken();
        if (StringUtils.isNotEmpty(refreshToken)) {
            builder.setRefreshToken(refreshToken);
        }
        return queryOrJson ? builder.buildQueryMessage() : builder.buildJSONMessage();
    }

    protected abstract String clientId();

}
