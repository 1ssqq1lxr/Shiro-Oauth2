package com.it.uaa.oauth;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  AuthenticationIdGenerator 生成
 */
public interface AuthenticationIdGenerator {

    String generate(String clientId, String username, String scope);

}