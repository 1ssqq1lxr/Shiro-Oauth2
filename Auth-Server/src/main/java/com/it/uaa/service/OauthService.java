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
package com.it.uaa.service;

import com.it.uaa.domain.Oauth2AccessToken;
import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.domain.Oauth2Code;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import java.util.Optional;
import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  OauthService 类封装
 */
public interface OauthService {

    Oauth2ClientDetails loadClientDetails(String clientId);

    String retrieveAuthCode(Oauth2ClientDetails clientDetails) throws OAuthSystemException;

    Oauth2AccessToken retrieveAccessToken(Oauth2ClientDetails clientDetails, Set<String> scopes, boolean includeRefreshToken) throws OAuthSystemException;

    Oauth2AccessToken retrieveNewAccessToken(Oauth2ClientDetails clientDetails, Set<String> scopes) throws OAuthSystemException;

    Oauth2Code loadOauthCode(String code, Oauth2ClientDetails clientDetails);

    boolean removeOauthCode(String code, Oauth2ClientDetails clientDetails);

    Oauth2AccessToken retrieveAuthorizationCodeAccessToken(Oauth2ClientDetails clientDetails, String code) throws OAuthSystemException;

    Oauth2AccessToken retrievePasswordAccessToken(Oauth2ClientDetails clientDetails, Set<String> scopes, String username) throws OAuthSystemException;

    Oauth2AccessToken retrieveClientCredentialsAccessToken(Oauth2ClientDetails clientDetails, Set<String> scopes) throws OAuthSystemException;

    Optional<Oauth2AccessToken> loadAccessTokenByRefreshToken(String refreshToken, String clientId);

    Oauth2AccessToken changeAccessTokenByRefreshToken(String refreshToken, String clientId) throws OAuthSystemException;
}
