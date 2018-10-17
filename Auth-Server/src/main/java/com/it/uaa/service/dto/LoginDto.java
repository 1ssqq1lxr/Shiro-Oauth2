/*
 * Copyright (c) 2013 Andaily Information Technology Co. Ltd
 * www.andaily.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Andaily Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Andaily Information Technology Co. Ltd.
 */
package com.it.uaa.service.dto;

import org.apache.shiro.authc.UsernamePasswordToken;

import java.io.Serializable;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  LoginDto 类封装
 */
public class LoginDto implements Serializable {


    private String username;

    private String password;



    public LoginDto() {
    }

    public UsernamePasswordToken token() {
        return new UsernamePasswordToken(username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
