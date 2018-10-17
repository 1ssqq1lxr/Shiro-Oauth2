package com.hjimi.resource.service;

import com.hjimi.resource.domain.Oauth2AccessToken;
import com.hjimi.resource.domain.Oauth2ClientDetails;

import java.util.Optional;

/**
 * @Auther: lxr
 * @Date: 2018/10/8 15:10
 * @Description:
 */
public interface Oauth2RsService {

    Optional<Oauth2AccessToken> loadAccessTokenByTokenId(String tokenId);

    Optional<Oauth2ClientDetails> loadClientDetails(String clientId);

}
