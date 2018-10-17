package com.it.uaa.exception;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 11:03
 * @Description: 自定义异常
 */
public class OauthDefindException  extends  RuntimeException {
    public OauthDefindException(String message) {
        super(message);
    }
}
