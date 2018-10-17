package com.it.uaa.repository;

import com.it.uaa.domain.Oauth2Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 18:29
 * @Description:
 */
@Repository
public interface Oauth2CodeRepository extends JpaRepository<Oauth2Code, Long> {

    Optional<Oauth2Code> findByClientIdAndUsername(String clientId, String username);

    Optional<Oauth2Code> findByClientIdAndCode(String clientId, String code);
}
