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
package com.it.uaa.infrastructure;


import com.it.uaa.domain.Oauth2AccessToken;
import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.domain.Oauth2Code;
import com.it.uaa.oauth.OauthCacheRepository;
import com.it.uaa.repository.Oauth2AccessTokenRepository;
import com.it.uaa.repository.Oauth2ClientDetailsRepository;
import com.it.uaa.repository.Oauth2CodeRepository;
import com.it.uaa.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description: oauth2 集中服务提供
 */
@Service
@Transactional(readOnly=true)
public class Oauth2Respository implements OauthCacheRepository {

    private static final Logger LOG = LoggerFactory.getLogger(Oauth2Respository.class);

    private final UserRepository userRepository;

    private final Oauth2AccessTokenRepository oauth2AccessTokenRepository;

    private  final Oauth2ClientDetailsRepository oauth2ClientDetailsRepository;

    private final Oauth2CodeRepository oauth2CodeRepository;

    public Oauth2Respository(UserRepository userRepository, Oauth2AccessTokenRepository oauth2AccessTokenRepository, Oauth2ClientDetailsRepository oauth2ClientDetailsRepository, Oauth2CodeRepository oauth2CodeRepository) {
        this.userRepository = userRepository;
        this.oauth2AccessTokenRepository = oauth2AccessTokenRepository;
        this.oauth2ClientDetailsRepository = oauth2ClientDetailsRepository;
        this.oauth2CodeRepository = oauth2CodeRepository;
    }

    @Override
    @Transactional
    public int saveOauthCode(Oauth2Code oauthCode) {
        return oauth2CodeRepository.save(oauthCode).getId().intValue();
    }

    @Override
    public Optional<Oauth2Code> findOauthCodeByUsernameClientId(String username, String clientId) {
        return  oauth2CodeRepository.findByClientIdAndUsername(clientId,username);
    }

    @Override
    @Transactional
    public void deleteOauthCode(Oauth2Code oauthCode) {
         oauth2CodeRepository.delete(oauthCode.getId());
    }

    @Override
    @Transactional
    public int saveAccessToken(Oauth2AccessToken accessToken) {
        return oauth2AccessTokenRepository.save(accessToken).getId().intValue();
    }

    @Override
    public Optional<Oauth2AccessToken> findAccessToken(String clientId, String username, String authenticationId) {
        return oauth2AccessTokenRepository.findByClientIdAndUsernameAndAuthenticationId(clientId, username, authenticationId);
    }

    @Override
    @Transactional
    public void deleteAccessToken(Oauth2AccessToken accessToken) {
        oauth2AccessTokenRepository.delete(accessToken.getId());
    }

    @Override
    public Optional<Oauth2Code> findOauthCode(String code, String clientId) {
        return oauth2CodeRepository.findByClientIdAndCode(clientId,code);
    }

    @Override
    public Optional<Oauth2AccessToken> findAccessTokenByRefreshToken(String refreshToken, String clientId) {
        return oauth2AccessTokenRepository.findByClientIdAndRefreshToken(clientId,refreshToken);
    }

    @Override
    public Optional<Oauth2ClientDetails> findClientDetails(String clientId) {
        return oauth2ClientDetailsRepository.findByClientId(clientId);
    }


}
