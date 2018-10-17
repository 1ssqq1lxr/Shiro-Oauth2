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

import com.it.uaa.domain.Oauth2ClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  AbstractOauthCodeValidator 校验器
 */
@Slf4j
public abstract class AbstractOauthCodeValidator extends AbstractClientDetailsValidator {


    @Override
    protected Oauth2ClientDetails clientDetails() {
        return oauthService.loadClientDetails( getRequest().getClientId());
    }

    @Override
    protected OAuthResponse invalidClientErrorResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                .setErrorDescription("Invalid client_id '" + getRequest().getClientId() + "'")
                .buildJSONMessage();
    }

    @Override
    protected OAuthResponse invalidRedirectUriResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.INVALID_REQUEST)
                .setErrorDescription("Invalid redirect_uri '" +  getRequest().getRedirectURI() + "'")
                .buildJSONMessage();
    }

    @Override
    protected OAuthResponse invalidScopeResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.INVALID_SCOPE)
                .setErrorDescription("Invalid scope '" +  getRequest().getScopes() + "'")
                .buildJSONMessage();
    }

    @Override
    protected OAuthResponse invalidClientSecretResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                .setErrorDescription("Invalid client_secret by client_id '" + getRequest().getClientId() + "'")
                .buildJSONMessage();
    }

    public abstract OAuthAuthzRequest getRequest();

}
