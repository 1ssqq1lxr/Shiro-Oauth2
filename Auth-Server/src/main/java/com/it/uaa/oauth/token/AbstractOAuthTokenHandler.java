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
package com.it.uaa.oauth.token;

import com.it.uaa.common.Oauth2RequestHolder;
import com.it.uaa.oauth.OAuthHandler;
import com.it.uaa.oauth.validator.AbstractClientDetailsValidator;
import com.it.uaa.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  /com.it.uaa.oauth/token 站点抽象类
 */
@Slf4j
public abstract class AbstractOAuthTokenHandler extends OAuthHandler implements OAuthTokenHandler {



    @Override
    public final void handle() throws OAuthProblemException, OAuthSystemException {
        //validate
        if (validateFailed()) {
            return;
        }
        handleAfterValidation();
    }


    protected boolean validateFailed() throws OAuthSystemException {
        AbstractClientDetailsValidator validator = getValidator();
        log.debug("Use [{}] validate client: {}", validator, Oauth2RequestHolder.getOauthRequest().getClientId());

        final OAuthResponse oAuthResponse = validator.validate();
        return checkAndResponseValidateFailed(oAuthResponse);
    }

    protected boolean checkAndResponseValidateFailed(OAuthResponse oAuthResponse) {
        if (oAuthResponse != null) {
            log.debug("Validate OAuthAuthzRequest(client_id={}) failed", Oauth2RequestHolder.getOauthRequest().getClientId());
            WebUtils.writeOAuthJsonResponse(Oauth2RequestHolder.getHttpServletResponse(), oAuthResponse);
            return true;
        }
        return false;
    }

    protected abstract AbstractClientDetailsValidator getValidator();

    protected String clientId() {
        return Oauth2RequestHolder.getOauthRequest().getClientId();
    }

    protected abstract void handleAfterValidation() throws OAuthProblemException, OAuthSystemException;


}
