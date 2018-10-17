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

import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.domain.Oauth2Code;
import com.it.uaa.exception.OauthDefindException;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.stereotype.Component;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  AuthCodeRetriever 类封装
 */
@Slf4j
@Component
public class AuthCodeRetriever extends AbstractOAuthHolder {



    public String retrieve(Oauth2ClientDetails clientDetails ) {
        final String username = currentUsername();
        oauthRepository.findOauthCodeByUsernameClientId(username, clientDetails.getClientId())
                .ifPresent(code-> oauthRepository.deleteOauthCode(code));
        return createOauthCode(clientDetails).getCode();
    }


    private Oauth2Code createOauthCode(Oauth2ClientDetails clientDetails ){
        final String authCode;
        try {
            authCode = oAuthIssuer.authorizationCode();
            String username = currentUsername();
            Oauth2Code oauth2Code = new Oauth2Code();
            oauth2Code.setClientId(clientDetails.getClientId());
            oauth2Code.setCode(authCode);
            oauth2Code.setUsername(username);
            oauthRepository.saveOauthCode(oauth2Code);
            return oauth2Code;
        } catch (OAuthSystemException e) {
            log.error("provide code error ",e);
        }
        throw  new OauthDefindException("生成oauth code报错");
    }


}
