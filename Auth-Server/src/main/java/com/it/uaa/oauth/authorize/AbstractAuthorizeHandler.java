package com.it.uaa.oauth.authorize;

import com.it.uaa.common.domain.oauth.Constants;
import com.it.uaa.oauth.OAuthAuthxRequest;
import com.it.uaa.oauth.OAuthHandler;
import com.it.uaa.oauth.validator.AbstractClientDetailsValidator;
import com.it.uaa.web.WebUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  /com.it.uaa.oauth/authorize 站点抽象类 处理授权handler
 */
public abstract class AbstractAuthorizeHandler extends OAuthHandler {

    protected OAuthAuthxRequest oauthRequest;
    protected HttpServletResponse response;

    protected boolean userFirstLogged = false;
    protected boolean userFirstApproved = false;

    public AbstractAuthorizeHandler(OAuthAuthxRequest oauthRequest, HttpServletResponse response) {
        this.oauthRequest = oauthRequest;
        this.response = response;
    }


    protected boolean validateFailed() throws OAuthSystemException {
        AbstractClientDetailsValidator validator = getValidator();

        final OAuthResponse oAuthResponse = validator.validate();
        return checkAndResponseValidateFailed(oAuthResponse);
    }

    protected abstract AbstractClientDetailsValidator getValidator();

    protected boolean checkAndResponseValidateFailed(OAuthResponse oAuthResponse) {
        if (oAuthResponse != null) {
            WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
            return true;
        }
        return false;
    }

    protected String clientId() {
        return oauthRequest.getClientId();
    }

    protected boolean isUserAuthenticated() {
        final Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }

    protected boolean isNeedUserLogin() {
        return !isUserAuthenticated() && !isPost();
    }


    protected boolean goApproval() throws ServletException, IOException {
        if (userFirstLogged && !clientDetails().isTrusted()) {
            //go to approval
            final HttpServletRequest request = oauthRequest.request();
            request.getRequestDispatcher(Constants.OAUTH_APPROVAL_VIEW)
                    .forward(request, response);
            return true;
        }
        return false;
    }

    protected boolean submitApproval() throws IOException, OAuthSystemException {
        if (isPost() && !clientDetails().isTrusted()) {
            //submit approval
            final HttpServletRequest request = oauthRequest.request();
            final String oauthApproval = request.getParameter(Constants.REQUEST_USER_OAUTH_APPROVAL);
            if (!"true".equalsIgnoreCase(oauthApproval)) {
                //Deny action
                responseApprovalDeny();
                return true;
            } else {
                userFirstApproved = true;
                return false;
            }
        }
        return false;
    }

    protected void responseApprovalDeny() throws  OAuthSystemException {
        final OAuthResponse oAuthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                .setError(OAuthError.CodeResponse.ACCESS_DENIED)
                .setErrorDescription("User denied access")
                .location(clientDetails().getRedirectUri())
                .setState(oauthRequest.getState())
                .buildQueryMessage();

        WebUtils.writeOAuthQueryResponse(response, oAuthResponse);

        //user logout when deny
        final Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }


    protected boolean goLogin() throws ServletException, IOException {
        if (isNeedUserLogin()) {
            final HttpServletRequest request = oauthRequest.request();
            request.getRequestDispatcher(Constants.OAUTH_LOGIN_VIEW)
                    .forward(request, response);
            return true;
        }
        return false;
    }


    //true is login failed, false is successful
    protected boolean submitLogin() throws ServletException, IOException {
        if (isSubmitLogin()) {
            //login flow
            try {
                UsernamePasswordToken token = createUsernamePasswordToken();
                SecurityUtils.getSubject().login(token);

                this.userFirstLogged = true;
                return false;
            } catch (Exception ex) {
                //login failed
                final HttpServletRequest request = oauthRequest.request();
                request.setAttribute("oauth_login_error", true);
                request.getRequestDispatcher(Constants.OAUTH_LOGIN_VIEW)
                        .forward(request, response);
                return true;
            }
        }
        return false;
    }

    private UsernamePasswordToken createUsernamePasswordToken() {
        final HttpServletRequest request = oauthRequest.request();
        final String username = request.getParameter(Constants.REQUEST_USERNAME);
        final String password = request.getParameter(Constants.REQUEST_PASSWORD);
        return new UsernamePasswordToken(username, password);
    }

    private boolean isSubmitLogin() {
        return !isUserAuthenticated() && isPost();
    }

    protected boolean isPost() {
        return RequestMethod.POST.name().equalsIgnoreCase(oauthRequest.request().getMethod());
    }

    public void handle() throws OAuthSystemException, ServletException, IOException {
        //validate
        if (validateFailed()) {
            return;
        }

        //Check need usr login
        if (goLogin()) {
            return;
        }

        //submit login
        if (submitLogin()) {
            return;
        }

        // Check approval
        if (goApproval()) {
            return;
        }

        //Submit approval
        if (submitApproval()) {
            return;
        }

        //handle response
        handleResponse();
    }

    //Handle custom response content
    protected abstract void handleResponse() throws OAuthSystemException, IOException;
}
