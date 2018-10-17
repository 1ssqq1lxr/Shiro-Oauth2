package com.hjimi.resource.exception;

/**
 * @Auther: lxr
 * @Date: 2018/10/8 16:26
 * @Description:
 */
public class OauthTokenException extends RuntimeException{

    public OauthTokenException(String message) {
        super(message);
    }
}
