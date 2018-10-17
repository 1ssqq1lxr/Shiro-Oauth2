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
import com.it.uaa.oauth.validator.token.PasswordClientDetailsValidator;
import com.it.uaa.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  密码模式
 */
@Slf4j
public class PasswordTokenHandler extends AbstractOAuthTokenHandler {



    @Override
    public boolean support(OAuthTokenxRequest tokenRequest) throws OAuthProblemException {
        final String grantType = tokenRequest.getGrantType();
        return GrantType.PASSWORD.toString().equalsIgnoreCase(grantType);
    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new PasswordClientDetailsValidator();
    }

    /**
     * /com.it.uaa.oauth/token?client_id=mobile-client&client_secret=mobile&grant_type=password&scope=read,write&username=mobile&password=mobile
     * <p/>
     * Response access_token
     * If exist Oauth2AccessToken and it is not expired, return it
     * otherwise, return a new Oauth2AccessToken(include refresh_token)
     * <p/>
     * {"token_type":"Bearer","expires_in":33090,"refresh_token":"976aeaf7df1ee998f98f15acd1de63ea","access_token":"7811aff100eb7dadec132f45f1c01727"}
     */
    @Override
    public void handleAfterValidation() throws  OAuthSystemException {

        Oauth2AccessToken accessToken = oauthService.retrievePasswordAccessToken(clientDetails(),
                Oauth2RequestHolder.getOauthRequest().getScopes(),  Oauth2RequestHolder.getOauthRequest().getUsername());
        final OAuthResponse tokenResponse = createTokenResponse(accessToken, false);

        log.debug("'password' response: {}", tokenResponse);
        WebUtils.writeOAuthJsonResponse( Oauth2RequestHolder.getHttpServletResponse(), tokenResponse);

    }
}
