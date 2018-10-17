package com.it.uaa.oauth;

import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  OAuthTokenxRequest 类封装
 */
public class OAuthTokenxRequest extends OAuthTokenRequest {

    public OAuthTokenxRequest(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        super(request);
    }

    public HttpServletRequest request() {
        return this.request;
    }




}
