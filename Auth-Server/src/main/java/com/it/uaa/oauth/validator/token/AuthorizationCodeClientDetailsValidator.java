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
package com.it.uaa.oauth.validator.token;

import com.it.uaa.common.Oauth2RequestHolder;
import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.domain.Oauth2Code;
import com.it.uaa.oauth.validator.AbstractOauthTokenValidator;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  authorization code 校验器
 */
public class AuthorizationCodeClientDetailsValidator extends AbstractOauthTokenValidator {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationCodeClientDetailsValidator.class);


    /**
     * /com.it.uaa.oauth/token?client_id=unity-client&client_secret=unity&grant_type=authorization_code&code=zLl170&redirect_uri=redirect_uri
     */
    @Override
    protected OAuthResponse validateSelf(Oauth2ClientDetails clientDetails) throws OAuthSystemException {

        String clientId = clientDetails.getClientId();
        //validate grant_type
        final String grantType = grantType();
        if (!clientDetails.getGrantTypes().contains(grantType)) {
            LOG.debug("Invalid grant_type '{}', client_id = '{}'", grantType, clientId);
            return invalidGrantTypeResponse(grantType);
        }

        //validate client_secret
        final String clientSecret = Oauth2RequestHolder.getOauthRequest().getClientSecret();
        if (clientSecret == null || !clientSecret.equals(clientDetails.getClientSecret())) {
            LOG.debug("Invalid client_secret '{}', client_id = '{}'", clientSecret, clientId);
            return invalidClientSecretResponse();
        }


        //validate redirect_uri
        final String redirectURI = Oauth2RequestHolder.getOauthRequest().getRedirectURI();
        if (redirectURI == null || !redirectURI.equals(clientDetails.getRedirectUri())) {
            LOG.debug("Invalid redirect_uri '{}', client_id = '{}'", redirectURI, clientId);
            return invalidRedirectUriResponse();
        }
        //validate code
        String code = getCode();
        Oauth2Code oauthCode = oauthService.loadOauthCode(code, clientDetails());
        if (oauthCode == null) {
            LOG.debug("Invalid code '{}', client_id = '{}'", code,clientId);
            return invalidCodeResponse(code);
        }

        return null;
    }

    private OAuthResponse invalidCodeResponse(String code) throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("Invalid code '" + code + "'")
                .buildJSONMessage();
    }

    private String getCode() {
        return Oauth2RequestHolder.getOauthRequest().getCode();
    }
}
