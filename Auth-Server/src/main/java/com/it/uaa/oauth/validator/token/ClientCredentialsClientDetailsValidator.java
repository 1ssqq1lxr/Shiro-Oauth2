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
import com.it.uaa.oauth.validator.AbstractOauthTokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  client 校验器
 */
@Slf4j
public class ClientCredentialsClientDetailsValidator extends AbstractOauthTokenValidator {


    /*
    * /com.it.uaa.oauth/token?client_id=credentials-client&client_secret=credentials-secret&grant_type=client_credentials&scope=read write
    * */
    @Override
    protected OAuthResponse validateSelf(Oauth2ClientDetails clientDetails) throws OAuthSystemException {
        String clientId = clientDetails.getClientId();
        //validate grant_type
        final String grantType = grantType();
        if (!clientDetails.getGrantTypes().contains(grantType)) {
            log.debug("Invalid grant_type '{}', client_id = '{}'", grantType, clientId);
            return invalidGrantTypeResponse(grantType);
        }

        //validate client_secret
        final String clientSecret =  Oauth2RequestHolder.getOauthRequest().getClientSecret();
        if (clientSecret == null || !clientSecret.equals(clientDetails.getClientSecret())) {
            log.debug("Invalid client_secret '{}', client_id = '{}'", clientSecret, clientId);
            return invalidClientSecretResponse();
        }

        //validate scope
        final Set<String> scopes =  Oauth2RequestHolder.getOauthRequest().getScopes();
        if (scopes.isEmpty() || excludeScopes(scopes, clientDetails)) {
            return invalidScopeResponse();
        }

        return null;
    }


}
