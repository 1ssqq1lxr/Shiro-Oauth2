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
import com.it.uaa.oauth.validator.token.ClientCredentialsClientDetailsValidator;
import com.it.uaa.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  客户端模式
 */
@Slf4j
public class ClientCredentialsTokenHandler extends AbstractOAuthTokenHandler {



    @Override
    public boolean support(OAuthTokenxRequest tokenRequest) throws OAuthProblemException {
        final String grantType = tokenRequest.getGrantType();
        return GrantType.CLIENT_CREDENTIALS.toString().equalsIgnoreCase(grantType);
    }

    /**
     * /com.it.uaa.oauth/token?client_id=credentials-client&client_secret=credentials-secret&grant_type=client_credentials&scope=read write
     * <p/>
     * Response access_token
     * If exist Oauth2AccessToken and it is not expired, return it
     * otherwise, return a new Oauth2AccessToken
     * <p/>
     * {"access_token":"38187f32-e4fb-4650-8e4a-99903b66f20e","token_type":"bearer","expires_in":7}
     */
    @Override
    public void handleAfterValidation() throws OAuthProblemException, OAuthSystemException {

        Oauth2AccessToken accessToken = oauthService.retrieveClientCredentialsAccessToken(clientDetails(),
                Oauth2RequestHolder.getOauthRequest().getScopes());
        final OAuthResponse tokenResponse = createTokenResponse(accessToken, false);

        log.debug("'client_credentials' response: {}", tokenResponse);
        WebUtils.writeOAuthJsonResponse(Oauth2RequestHolder.getHttpServletResponse(), tokenResponse);


    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new ClientCredentialsClientDetailsValidator();
    }

}
