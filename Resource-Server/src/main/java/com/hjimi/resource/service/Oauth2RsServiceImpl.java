package com.hjimi.resource.service;

import com.hjimi.resource.domain.Oauth2AccessToken;
import com.hjimi.resource.domain.Oauth2ClientDetails;
import com.hjimi.resource.repository.Oauth2AccessTokenRepository;
import com.hjimi.resource.repository.Oauth2ClientDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Auther: lxr
 * @Date: 2018/10/8 20:56
 * @Description:
 */
@Service
public class Oauth2RsServiceImpl implements Oauth2RsService {

    private final Oauth2AccessTokenRepository oauth2AccessTokenRepository;

    private final Oauth2ClientDetailsRepository oauth2ClientDetailsRepository;

    public Oauth2RsServiceImpl(Oauth2AccessTokenRepository oauth2AccessTokenRepository, Oauth2ClientDetailsRepository oauth2ClientDetailsRepository) {
        this.oauth2AccessTokenRepository = oauth2AccessTokenRepository;
        this.oauth2ClientDetailsRepository = oauth2ClientDetailsRepository;
    }

    @Override
    public Optional<Oauth2AccessToken> loadAccessTokenByTokenId(String tokenId) {
        return oauth2AccessTokenRepository.findByTokenId(tokenId);
    }

    @Override
    public Optional<Oauth2ClientDetails> loadClientDetails(String clientId) {
        return oauth2ClientDetailsRepository.findByClientId(clientId);
    }
}
