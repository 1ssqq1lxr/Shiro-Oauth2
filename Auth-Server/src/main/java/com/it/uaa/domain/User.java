package com.it.uaa.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 17:54
 * @Description:
 */
@Data
@Entity
@Table(name = "oauth_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username")
    private String  username;

    @Column(name="password")
    private String password;

    @Column(name="status")
    private boolean status; // '账号状态，1=正常，0=锁定',

    @Column(name="is_delete")
    private boolean isDelete; //'是否已删除，1：已删除；0：未删除',

    @Column(name="phone")
    private String phone;
    
}
