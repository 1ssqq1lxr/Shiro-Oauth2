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
import com.it.uaa.oauth.validator.token.AuthorizationCodeClientDetailsValidator;
import com.it.uaa.web.WebUtils;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  授权码模式
 */
public class AuthorizationCodeTokenHandler extends AbstractOAuthTokenHandler {


    @Override
    public boolean support(OAuthTokenxRequest tokenRequest)  {
        final String grantType = tokenRequest.getGrantType();
        return GrantType.AUTHORIZATION_CODE.toString().equalsIgnoreCase(grantType);
    }

    /*
    *
    * /com.it.uaa.oauth/token?client_id=unity-client&client_secret=unity&grant_type=authorization_code&code=zLl170&redirect_uri=redirect_uri
    * */
    @Override
    public void handleAfterValidation() throws OAuthProblemException, OAuthSystemException {

        //response token, always new
        responseToken();

        //remove code lastly
        removeCode();
    }

    private void removeCode() {
        final String code = Oauth2RequestHolder.getOauthRequest().getCode();
        oauthService.removeOauthCode(code, clientDetails());
    }

    private void responseToken() throws OAuthSystemException {
        Oauth2AccessToken accessToken = oauthService.retrieveAuthorizationCodeAccessToken(clientDetails(), Oauth2RequestHolder.getOauthRequest().getCode());
        OAuthResponse tokenResponse = createTokenResponse(accessToken, false);
        WebUtils.writeOAuthJsonResponse(Oauth2RequestHolder.getHttpServletResponse(), tokenResponse);
    }

    @Override
    protected AbstractClientDetailsValidator getValidator() {
        return new AuthorizationCodeClientDetailsValidator();
    }

}
