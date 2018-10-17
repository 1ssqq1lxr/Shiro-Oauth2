package com.hjimi.resource;

import com.hjimi.resource.domain.Oauth2AccessToken;
import com.hjimi.resource.domain.Oauth2ClientDetails;
import com.hjimi.resource.oauth2.ImiOauthToken;
import com.hjimi.resource.oauth2.TokenDatail;
import com.hjimi.resource.service.Oauth2RsService;
import com.it.uaa.common.domain.Oauth2Type;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Auther: lxr
 * @Date: 2018/10/8 14:47
 * @Description:
 */
@Slf4j
public class ResourceOauth2Filter extends AuthenticatingFilter {
    private final String resourceId;


    private final Oauth2RsService oauth2RsService;

    public ResourceOauth2Filter(String resourceId, Oauth2RsService oauth2RsService) {
        this.resourceId = resourceId;
        this.oauth2RsService = oauth2RsService;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 设置body方式
        OAuthAccessResourceRequest resourceRequest = new OAuthAccessResourceRequest(httpRequest, ParameterStyle.BODY);
        String token = resourceRequest.getAccessToken();
        Oauth2AccessToken oauth2AccessToken = oauth2RsService.loadAccessTokenByTokenId(token)
                .orElseThrow(() -> OAuthProblemException.error(OAuthError.ResourceResponse.INVALID_TOKEN)
                        .description("access_token 不存在 "));
        if(oauth2AccessToken.tokenExpired()){
            throw OAuthProblemException.error(OAuthError.ResourceResponse.INVALID_TOKEN)
                    .description("access_token 已超时 " );
        }
        Oauth2ClientDetails oauth2ClientDetails = oauth2RsService.loadClientDetails(oauth2AccessToken.getClientId())
                .orElseThrow(() -> OAuthProblemException.error(OAuthError.ResourceResponse.INSUFFICIENT_SCOPE)
                        .description("clientId 不存在 "));
        if(StringUtils.isBlank(oauth2ClientDetails.getResourceIds()) || !oauth2ClientDetails.getResourceIds().contains(resourceId)){
            throw OAuthProblemException.error(OAuthError.ResourceResponse.INSUFFICIENT_SCOPE)
                    .description("没有此资源权限 " );
        }
        TokenDatail build = TokenDatail.builder()
                .resourceId(resourceId)
                .token(token)
                .oauth2Type(Oauth2Type.valueOf(oauth2AccessToken.getTokenType())).build();
        ImiOauthToken imiOauthToken = new ImiOauthToken();
        imiOauthToken.setTokenDatail(build);
        return imiOauthToken;

    }


    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        try {
            return executeLogin(request, response);
        }   catch (OAuthProblemException e){
            OAuthResponse  oAuthResponse = OAuthRSResponse.errorResponse(401)
                    .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                    .setErrorDescription(e.getMessage())
                    .buildJSONMessage();
            WebUtils.writeOAuthJsonResponse((HttpServletResponse) response, oAuthResponse);
            return false;
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {
        final OAuthResponse oAuthResponse;
        try {
            oAuthResponse = OAuthRSResponse.errorResponse(401)
                    .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                    .setErrorDescription(ae.getMessage())
                    .buildJSONMessage();
            WebUtils.writeOAuthJsonResponse((HttpServletResponse) response, oAuthResponse);
        } catch (OAuthSystemException e) {
            log.error("Build JSON message error", e);
            throw new IllegalStateException(e);
        }
        return false;
    }



}
