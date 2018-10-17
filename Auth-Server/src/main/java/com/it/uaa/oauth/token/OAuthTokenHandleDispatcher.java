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
import com.it.uaa.common.OauthHttpOptions;
import com.it.uaa.oauth.OAuthTokenxRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:   /com.it.uaa.oauth/token 核心分发类
 */
@Component
@Slf4j
public class OAuthTokenHandleDispatcher   implements InitializingBean {


    private List<OAuthTokenHandler> handlers = new ArrayList<>();

    private void initialHandlers() {
        handlers.add(new AuthorizationCodeTokenHandler());
        handlers.add(new PasswordTokenHandler());
        handlers.add(new RefreshTokenHandler());
        handlers.add(new ClientCredentialsTokenHandler());
    }

    public void dispatch(OAuthTokenxRequest tokenRequest, HttpServletResponse response) throws OAuthProblemException, OAuthSystemException {
        OauthHttpOptions build = OauthHttpOptions.builder().httpServletResponse(response).oAuthRequest(tokenRequest).build();
        try{
            Oauth2RequestHolder.saveHttpOptions(build);
            for (OAuthTokenHandler handler : handlers) {
                if (handler.support(tokenRequest)) {
                    log.debug("Found '{}' handle OAuthTokenxRequest: {}", handler, tokenRequest);
                    handler.handle();
                    return;
                }
            }
            throw new IllegalStateException("Not found 'OAuthTokenHandler' to handle OAuthTokenxRequest: " + tokenRequest);
        }
        finally {
            Oauth2RequestHolder.removeHttpOptions();
        }
    }

    @Override
    public void afterPropertiesSet()  {
        initialHandlers();
    }
}
