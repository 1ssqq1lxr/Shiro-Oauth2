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
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  NewAccessTokenRetriever 类封装
 */
@Component
public class NewAccessTokenRetriever extends AbstractAccessTokenHandler {

    //Always return new Oauth2AccessToken, exclude refreshToken
    public Oauth2AccessToken retrieve(Oauth2ClientDetails clientDetails, Set<String> scopes) throws OAuthSystemException {
        String  username = currentUsername();
        String clientId = clientDetails.getClientId();
        String scope = OAuthUtils.encodeScopes(scopes);
        String authenticationId = authenticationIdGenerator.generate(clientId, username, scope);
        oauthRepository.findAccessToken(clientId, username, authenticationId)
                .ifPresent(token->oauthRepository.deleteAccessToken(token));
        return createAndSaveAccessToken(clientDetails, false, username, authenticationId, Oauth2Type.RESFRESH_TOKEN);
    }

}
