/*
 * Copyright (c) 2013 Andaily Information Technology Co. Ltd
 * www.andaily.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Andaily Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Andaily Information Technology Co. Ltd.
 */
package com.it.uaa.oauth.token;

import com.it.uaa.common.Oauth2RequestHolder;
import com.it.uaa.domain.Oauth2AccessToken;
import com.it.uaa.oauth.OAuthTokenxRequest;
import com.it.uaa.oauth.validator.AbstractClientDetailsValidator;
import com.it.uaa.oauth.validator.token.RefreshTokenClientDetailsValidator;
import com.it.uaa.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  refresh token 模式
 */
@Slf4j
public class RefreshTokenHandler extends AbstractOAuthTokenHandler {



    @Override
    public boolean support(OAuthTokenxRequest tokenRequest) throws OAuthProblemException {
        final String grantType = tokenRequest.getGrantType();
        return GrantType.REFRESH_TOKEN.toString().equalsIgnoreCase(grantType);
    }

    /**
     * URL demo:
     * /com.it.uaa.oauth/token?client_id=mobile-client&client_secret=mobile&grant_type=refresh_token&refresh_token=b36f4978-a172-4aa8-af89-60f58abe3ba1
     *
     * @throws OAuthProblemException
     */
    @Override
    public void handleAfterValidation() throws  OAuthSystemException {

        final String refreshToken =  Oauth2RequestHolder.getOauthRequest().getRefreshToken();
        Oauth2AccessToken accessToken = oauthService.changeAccessTokenByRefreshToken(refreshToken,  Oauth2RequestHolder.getOauthRequest().getClientId());

        final OAuthResponse tokenResponse = createTokenResponse(accessToken, false);

        log.debug("'refresh_token' response: {}", tokenResponse);
        WebUtils.writeOAuthJsonResponse( Oauth2RequestHolder.getHttpServletResponse(), tokenResponse);

    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new RefreshTokenClientDetailsValidator();
    }

}
