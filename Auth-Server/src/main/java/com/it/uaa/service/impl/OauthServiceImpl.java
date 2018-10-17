package com.it.uaa.service.impl;

import com.it.uaa.domain.Oauth2AccessToken;
import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.domain.Oauth2Code;
import com.it.uaa.exception.OauthDefindException;
import com.it.uaa.oauth.OauthCacheRepository;
import com.it.uaa.service.OauthService;
import com.it.uaa.service.business.*;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  OauthServiceImpl 类封装
 */
@Service("oauthService")
public class OauthServiceImpl implements OauthService {

    private final OauthCacheRepository oauthRepository;

    private final AccessTokenRetriever retriever;

    private final AuthCodeRetriever authCodeRetriever;

    private final AuthorizationCodeAccessTokenRetriever authorizationCodeAccessTokenRetriever;

    private final PasswordAccessTokenRetriever passwordAccessTokenRetriever;

    private final ClientCredentialsAccessTokenRetriever clientCredentialsAccessTokenRetriever;

    private final  AccessTokenByRefreshTokenChanger accessTokenByRefreshTokenChanger;

    public OauthServiceImpl(OauthCacheRepository oauthRepository, AccessTokenRetriever retriever, AuthCodeRetriever authCodeRetriever, AuthorizationCodeAccessTokenRetriever authorizationCodeAccessTokenRetriever, PasswordAccessTokenRetriever passwordAccessTokenRetriever, ClientCredentialsAccessTokenRetriever clientCredentialsAccessTokenRetriever, AccessTokenByRefreshTokenChanger accessTokenByRefreshTokenChanger) {
        this.oauthRepository = oauthRepository;
        this.retriever = retriever;
        this.authCodeRetriever = authCodeRetriever;
        this.authorizationCodeAccessTokenRetriever = authorizationCodeAccessTokenRetriever;
        this.passwordAccessTokenRetriever = passwordAccessTokenRetriever;
        this.clientCredentialsAccessTokenRetriever = clientCredentialsAccessTokenRetriever;
        this.accessTokenByRefreshTokenChanger = accessTokenByRefreshTokenChanger;
    }

    @Override
    public Oauth2ClientDetails loadClientDetails(String clientId) {
        return oauthRepository.findClientDetails(clientId).orElseThrow(()->new OauthDefindException("未发现client datails"));
    }


    @Override
    public String retrieveAuthCode(Oauth2ClientDetails clientDetails)  {
        return authCodeRetriever.retrieve(clientDetails);
    }


    @Override
    public Oauth2AccessToken retrieveAccessToken(Oauth2ClientDetails clientDetails, Set<String> scopes, boolean includeRefreshToken)  {
        return retriever.retrieve(clientDetails,scopes,includeRefreshToken);
    }

    //Always return new Oauth2AccessToken, exclude refreshToken
    @Override
    public Oauth2AccessToken retrieveNewAccessToken(Oauth2ClientDetails clientDetails, Set<String> scopes) {
        return retriever.retrieve(clientDetails,scopes,true);
    }

    @Override
    public Oauth2Code loadOauthCode(String code, Oauth2ClientDetails clientDetails) {
        final String clientId = clientDetails.getClientId();
        return oauthRepository.findOauthCode(code, clientId).orElseThrow(()->new OauthDefindException("未发现client code"));
    }

    @Override
    public boolean removeOauthCode(String code, Oauth2ClientDetails clientDetails) {
            final Oauth2Code oauthCode = loadOauthCode(code, clientDetails);
            oauthRepository.deleteOauthCode(oauthCode);
            return true;
    }

    //Always return new Oauth2AccessToken
    @Override
    public Oauth2AccessToken retrieveAuthorizationCodeAccessToken(Oauth2ClientDetails clientDetails, String code) throws OAuthSystemException {
        return authorizationCodeAccessTokenRetriever.retrieve(clientDetails,code);
    }

    //grant_type=password Oauth2AccessToken
    @Override
    public Oauth2AccessToken retrievePasswordAccessToken(Oauth2ClientDetails clientDetails, Set<String> scopes, String username) throws OAuthSystemException {
        return passwordAccessTokenRetriever.retrieve(clientDetails,scopes,username);
    }


    //grant_type=client_credentials
    @Override
    public Oauth2AccessToken retrieveClientCredentialsAccessToken(Oauth2ClientDetails clientDetails, Set<String> scopes) throws OAuthSystemException {
        return clientCredentialsAccessTokenRetriever.retrieve(clientDetails,scopes);
    }

    @Override
    public Optional<Oauth2AccessToken> loadAccessTokenByRefreshToken(String refreshToken, String clientId) {
        return oauthRepository.findAccessTokenByRefreshToken(refreshToken, clientId);
    }

    /*
    * Get Oauth2AccessToken
    * Generate a new Oauth2AccessToken from existed(exclude token,refresh_token)
    * Update access_token,refresh_token, expired.
    * Save and remove old
    * */
    @Override
    public Oauth2AccessToken changeAccessTokenByRefreshToken(String refreshToken, String clientId) throws OAuthSystemException {
        return accessTokenByRefreshTokenChanger.change(refreshToken,clientId);
    }


}
