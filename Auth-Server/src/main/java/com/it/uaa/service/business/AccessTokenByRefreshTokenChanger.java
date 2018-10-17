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

import com.it.uaa.domain.Oauth2AccessToken;
import com.it.uaa.exception.OauthDefindException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  AccessTokenByRefreshTokenChanger 类封装
 */
@Component
public class AccessTokenByRefreshTokenChanger extends AbstractAccessTokenHandler {


    public Oauth2AccessToken change(String refreshToken, String clientId) throws OAuthSystemException {
        Oauth2AccessToken oldToken = oauthRepository.findAccessTokenByRefreshToken(refreshToken, clientId).orElseThrow(()->new OauthDefindException("未发现refresh token"));
        if(oldToken.refreshTokenExpired()){
            throw new OauthDefindException("refresh token 已经过期");
        }
        oldToken.setRefreshToken(oAuthIssuer.refreshToken());
        oldToken.setTokenId(oAuthIssuer.accessToken());
        oldToken.setCreateTime(new Date());
        return oldToken;
    }
}
