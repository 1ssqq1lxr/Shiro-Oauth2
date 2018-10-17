package com.hjimi.resource.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.it.uaa.common.infrastructure.DateUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 18:08
 * @Description:
 */
@Data
@Entity
@Table(name = "oauth_access_token")
public class Oauth2AccessToken  {


    protected final long THOUSAND = 1000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "username")
    private String username;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "authentication_id")
    private String authenticationId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "token_type")
    private String tokenType ;

    @Column(name = "token_expired_seconds")
    private int tokenExpiredSeconds = 60 * 60 * 12;

    @Column(name = "refresh_token_expired_seconds")
    private int refreshTokenExpiredSeconds = 60 * 60 * 24 * 30;


    @Column(name = "create_time")
    protected Date createTime = DateUtils.now();

    public boolean tokenExpired() {
        final long time = createTime.getTime() + (this.tokenExpiredSeconds * THOUSAND);
        return time < DateUtils.now().getTime();
    }

}
