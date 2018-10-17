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
package com.it.uaa.oauth.validator.authorize;

import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.oauth.validator.AbstractOauthCodeValidator;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  token 校验器
 */
public class TokenClientDetailsValidator extends AbstractOauthCodeValidator {

    private static final Logger LOG = LoggerFactory.getLogger(TokenClientDetailsValidator.class);



    private final  OAuthAuthzRequest OAuthAuthzRequest;

    public TokenClientDetailsValidator(OAuthAuthzRequest oAuthAuthzRequest) {
        OAuthAuthzRequest = oAuthAuthzRequest;
    }

    /*
     * grant_type="implicit"   -> response_type="token"
     * ?response_type=token&scope=read,write&client_id=[client_id]&client_secret=[client_secret]&redirect_uri=[redirect_uri]
    * */
    @Override
    public OAuthResponse validateSelf(Oauth2ClientDetails clientDetails) throws OAuthSystemException {

        //validate client_secret
        final String clientSecret = OAuthAuthzRequest.getClientSecret();
        if (clientSecret == null || !clientSecret.equals(clientDetails.getClientSecret())) {
            return invalidClientSecretResponse();
        }

        //validate redirect_uri
        final String redirectURI = OAuthAuthzRequest.getRedirectURI();
        if (redirectURI == null || !redirectURI.equals(clientDetails.getRedirectUri())) {
            LOG.debug("Invalid redirect_uri '{}' by response_type = 'code', client_id = '{}'", redirectURI, clientDetails.getClientId());
            return invalidRedirectUriResponse();
        }

        //validate scope
        final Set<String> scopes = OAuthAuthzRequest.getScopes();
        if (scopes.isEmpty() || excludeScopes(scopes, clientDetails)) {
            return invalidScopeResponse();
        }
        return null;
    }


    @Override
    public org.apache.oltu.oauth2.as.request.OAuthAuthzRequest getRequest() {
        return OAuthAuthzRequest;
    }
}
