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
package com.it.uaa.oauth;


import com.it.uaa.common.domain.shared.Repository;
import com.it.uaa.domain.Oauth2AccessToken;
import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.domain.Oauth2Code;

import java.util.Optional;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  接口 封装
 */
public interface OauthCacheRepository extends Repository {


    int saveOauthCode(Oauth2Code oauthCode);

    Optional<Oauth2Code> findOauthCodeByUsernameClientId(String username, String clientId);

    void deleteOauthCode(Oauth2Code oauthCode);

    int saveAccessToken(Oauth2AccessToken accessToken);

    Optional<Oauth2AccessToken> findAccessToken(String clientId, String username, String authenticationId);

    void deleteAccessToken(Oauth2AccessToken accessToken);

    Optional<Oauth2Code> findOauthCode(String code, String clientId);

    Optional<Oauth2AccessToken> findAccessTokenByRefreshToken(String refreshToken, String clientId);

    Optional<Oauth2ClientDetails> findClientDetails(String clientId);
}