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
import com.it.uaa.domain.Oauth2Code;
import com.it.uaa.exception.OauthDefindException;
import org.springframework.stereotype.Component;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  AuthorizationCodeAccessTokenRetriever 类封装
 */
@Component
public class AuthorizationCodeAccessTokenRetriever extends AbstractAccessTokenHandler {


    public Oauth2AccessToken retrieve(Oauth2ClientDetails clientDetails, String code)  {
        Oauth2Code oauthCode = loadOauthCode(code,clientDetails.getClientId());
        String username = oauthCode.getUsername();
        String clientId = clientDetails.getClientId();
        String authenticationId = authenticationIdGenerator.generate(clientId, username, Oauth2Type.CODE.toString());
        return createAndSaveAccessToken(clientDetails, clientDetails.supportRefreshToken(), username, authenticationId, Oauth2Type.CODE);
    }

    private Oauth2Code loadOauthCode(String code,String clientId) {
        return oauthRepository.findOauthCode(code, clientId).orElseThrow(()->new OauthDefindException("未发现auth code"));
    }
}
