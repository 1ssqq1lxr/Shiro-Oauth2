package com.it.uaa.repository;

import com.it.uaa.domain.Oauth2AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 18:29
 * @Description:
 */
@Repository
public interface Oauth2AccessTokenRepository extends JpaRepository<Oauth2AccessToken, Long> {

    Optional<Oauth2AccessToken> findByClientIdAndUsernameAndAuthenticationId(String clientId, String username, String authenticationId);

    Optional<Oauth2AccessToken> findByClientIdAndRefreshToken(String clientId, String refreshToken);

}
