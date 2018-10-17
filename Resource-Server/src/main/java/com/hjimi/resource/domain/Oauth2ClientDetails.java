package com.hjimi.resource.domain;

import com.it.uaa.common.infrastructure.DateUtils;
import lombok.Data;

import javax.persistence.*;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.util.Date;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 18:15
 * @Description:
 */
@Data
@Entity
@Table(name = "oauth_client_detailss")
public class Oauth2ClientDetails {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_ids")
    private String resourceIds;

    @Column(name = "scope")
    private String scope;

    @Column(name = "grant_types")
    private String grantTypes;

    @Column(name = "roles")
    private String roles;

    @Column(name = "access_token_validity")
    private Integer accessTokenValidity;

    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidity;

    @Column(name = "trusted")
    private boolean trusted = false;

    @Column(name = "archived")
    private boolean archived = false;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "redirect_uri")
    private String redirectUri;

    @Column(name = "client_uri")
    private String clientUri;

    @Column(name = "description")
    private String description;

    @Column(name = "client_icon_uri")
    private String iconUri;

    @Column(name = "create_time")
    protected Date createTime = DateUtils.now();

    public boolean supportRefreshToken() {
        return this.grantTypes != null && this.grantTypes.contains(GrantType.REFRESH_TOKEN.toString());
    }

}
