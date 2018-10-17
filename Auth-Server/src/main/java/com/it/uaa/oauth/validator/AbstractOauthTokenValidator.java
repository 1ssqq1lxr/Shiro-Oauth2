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
package com.it.uaa.oauth.validator;

import com.it.uaa.common.Oauth2RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  AbstractOauthTokenValidator 校验器
 */
@Slf4j
public abstract class AbstractOauthTokenValidator extends AbstractClientDetailsValidator {


    protected String grantType() {
        return Oauth2RequestHolder.getOauthRequest().getGrantType();
    }


    protected OAuthResponse invalidGrantTypeResponse(String grantType) throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("Invalid grant_type '" + grantType + "'")
                .buildJSONMessage();
    }

    //true is invalided
    protected boolean invalidUsernamePassword() {
        final String username = Oauth2RequestHolder.getOauthRequest().getUsername();
        final String password = Oauth2RequestHolder.getOauthRequest().getPassword();
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        } catch (Exception e) {
            log.debug("Login failed by username: " + username, e);
            return true;
        }
        return false;
    }

    protected OAuthResponse invalidUsernamePasswordResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("Bad credentials")
                .buildJSONMessage();
    }

}
