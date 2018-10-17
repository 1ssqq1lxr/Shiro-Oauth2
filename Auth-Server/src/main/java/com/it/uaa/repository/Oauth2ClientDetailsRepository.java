package com.it.uaa.repository;

import com.it.uaa.domain.Oauth2ClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 18:29
 * @Description:
 */
@Repository
public interface Oauth2ClientDetailsRepository extends JpaRepository<Oauth2ClientDetails, Long> {
    Optional<Oauth2ClientDetails> findByClientId(String clientId);
}
