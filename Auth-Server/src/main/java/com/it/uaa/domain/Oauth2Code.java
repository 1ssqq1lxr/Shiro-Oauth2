package com.it.uaa.domain;

import com.it.uaa.common.infrastructure.DateUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 18:22
 * @Description:
 */
@Data
@Entity
@Table(name = "oauth_code")
public class Oauth2Code  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "username")
    private String username;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "create_time")
    protected Date createTime = DateUtils.now();
}
