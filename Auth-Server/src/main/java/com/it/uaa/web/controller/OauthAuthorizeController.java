package com.it.uaa.web.controller;

import com.it.uaa.oauth.OAuthAuthxRequest;
import com.it.uaa.oauth.authorize.CodeAuthorizeHandler;
import com.it.uaa.oauth.authorize.TokenAuthorizeHandler;
import com.it.uaa.web.WebUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  OauthAuthorizeController 类封装
 */
@Controller
@RequestMapping("com/it/uaa/oauth/")
public class OauthAuthorizeController {

    private static final Logger LOG = LoggerFactory.getLogger(OauthAuthorizeController.class);


    /**
     * Must handle the grant_type as follow:
     * grant_type="authorization_code" -> response_type="code"
     * ?response_type=code&scope=read,write&client_id=[client_id]&redirect_uri=[redirect_uri]&state=[state]
     * <p/>
     * grant_type="implicit"   -> response_type="token"
     * ?response_type=token&scope=read,write&client_id=[client_id]&client_secret=[client_secret]&redirect_uri=[redirect_uri]
     * <p/>
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("authorize")
    public void authorize(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            OAuthAuthxRequest oauthRequest = new OAuthAuthxRequest(request);
            if (oauthRequest.isCode()) {
                CodeAuthorizeHandler codeAuthorizeHandler = new CodeAuthorizeHandler(oauthRequest, response);
                LOG.debug("Go to  response_type = 'code' handler: {}", codeAuthorizeHandler);
                codeAuthorizeHandler.handle();
            } else if (oauthRequest.isToken()) {
                TokenAuthorizeHandler tokenAuthorizeHandler = new TokenAuthorizeHandler(oauthRequest, response);
                LOG.debug("Go to response_type = 'token' handler: {}", tokenAuthorizeHandler);
                tokenAuthorizeHandler.handle();
            } else {
                unsupportResponseType(oauthRequest, response);
            }
        } catch (OAuthProblemException e) {
            OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_FOUND)
                    .location(e.getRedirectUri())
                    .error(e)
                    .buildJSONMessage();
            WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
        }


    }


    private void unsupportResponseType(OAuthAuthxRequest oauthRequest, HttpServletResponse response) throws OAuthSystemException {
        final String responseType = oauthRequest.getResponseType();
        LOG.debug("Unsupport response_type '{}' by client_id '{}'", responseType, oauthRequest.getClientId());

        OAuthResponse oAuthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE)
                .setErrorDescription("Unsupport response_type '" + responseType + "'")
                .buildJSONMessage();
        WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
    }


    @RequestMapping(value = "oauth_login")
    public String oauthLogin(HttpServletRequest httpServletRequest, Model model) {
        putAttr(httpServletRequest,model);
        return "oauth_login";
    }
    @RequestMapping(value = "oauth_approval")
    public String oauthApproval(HttpServletRequest httpServletRequest, Model model) {
        putAttr(httpServletRequest,model);
        return "oauth_approval";
    }

    private void putAttr(HttpServletRequest httpServletRequest, Model model){
        model.addAttribute("redirect_uri ",httpServletRequest.getParameter("redirect_uri"));
        model.addAttribute("client_id",httpServletRequest.getParameter("client_id"));
        model.addAttribute("scope",httpServletRequest.getParameter("scope"));
        model.addAttribute("state",httpServletRequest.getParameter("state"));
        model.addAttribute("response_type",httpServletRequest.getParameter("response_type"));
    }


}