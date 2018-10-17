package com.it.uaa.web.controller;

import com.it.uaa.oauth.OAuthTokenxRequest;
import com.it.uaa.oauth.token.OAuthTokenHandleDispatcher;
import com.it.uaa.web.WebUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  OauthTokenController 类封装
 */
@Controller
@RequestMapping("com/it/uaa/oauth/")
public class OauthTokenController {


    private final OAuthTokenHandleDispatcher tokenHandleDispatcher;

    public OauthTokenController(OAuthTokenHandleDispatcher tokenHandleDispatcher) {
        this.tokenHandleDispatcher = tokenHandleDispatcher;
    }

    @RequestMapping("token")
    public void authorize(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            OAuthTokenxRequest tokenRequest = new OAuthTokenxRequest(request);
            tokenHandleDispatcher.dispatch(tokenRequest,response);
        } catch (OAuthProblemException e) {
            //com.it.uaa.exception
            OAuthResponse oAuthResponse = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_FOUND)
                    .location(e.getRedirectUri())
                    .error(e)
                    .buildJSONMessage();
            WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
        }

    }
}
