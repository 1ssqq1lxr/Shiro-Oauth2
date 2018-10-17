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
import com.it.uaa.common.domain.shared.BeanProvider;
import com.it.uaa.domain.Oauth2AccessToken;
import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.exception.OauthDefindException;
import com.it.uaa.oauth.AuthenticationIdGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  AbstractAccessTokenHandler 类封装
 */
@Slf4j
public abstract class AbstractAccessTokenHandler extends AbstractOAuthHolder {



    protected transient AuthenticationIdGenerator authenticationIdGenerator = BeanProvider.getBean(AuthenticationIdGenerator.class);


    protected Oauth2AccessToken createAndSaveAccessToken(Oauth2ClientDetails clientDetails, boolean includeRefreshToken, String username, String authenticationId, Oauth2Type oauth2Type)  {
        try{
            Oauth2AccessToken accessToken= new Oauth2AccessToken();
            accessToken.setAuthenticationId(authenticationId);
            accessToken.setUsername(username);
            accessToken.setTokenId(oAuthIssuer.accessToken());
            accessToken.setRefreshTokenExpiredSeconds(clientDetails.getRefreshTokenValidity());
            accessToken.setClientId(clientDetails.getClientId());
            accessToken.setTokenType(oauth2Type.toString());
            if (includeRefreshToken) {
                accessToken.setRefreshToken(oAuthIssuer.refreshToken());
            }
            this.oauthRepository.saveAccessToken(accessToken);
            return accessToken;
        }
        catch (Exception e){
            log.error("token",e);
        }
        throw  new OauthDefindException("生成token报错");

    }

    protected boolean needCreated(Oauth2AccessToken accessToken) {
        boolean needCreate = false;
        if (accessToken == null) {
            needCreate = true;
        } else if (accessToken.tokenExpired()) {
            oauthRepository.deleteAccessToken(accessToken);
            needCreate = true;
        }
        return needCreate;
    }

}
