/*
 * Copyright (c) 2015 MONKEYK Information Technology Co. Ltd
 * www.monkeyk.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * MONKEYK Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with MONKEYK Information Technology Co. Ltd.
 */
package com.it.uaa.service.business;

import com.it.uaa.common.domain.Oauth2Type;
import com.it.uaa.domain.Oauth2AccessToken;
import com.it.uaa.domain.Oauth2ClientDetails;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  PasswordAccessTokenRetriever 类封装
 */
@Component
public class PasswordAccessTokenRetriever extends AbstractAccessTokenHandler {

    //grant_type=password Oauth2AccessToken
    public Oauth2AccessToken retrieve(Oauth2ClientDetails clientDetails, Set<String> scopes, String username)  {
        String scope = OAuthUtils.encodeScopes(scopes);
        final String clientId = clientDetails.getClientId();
        final String authenticationId = authenticationIdGenerator.generate(clientId, username, Oauth2Type.PASSWORD.toString()+scope);
        return createAndSaveAccessToken(clientDetails, clientDetails.supportRefreshToken(), username, authenticationId, Oauth2Type.PASSWORD);
    }


}
