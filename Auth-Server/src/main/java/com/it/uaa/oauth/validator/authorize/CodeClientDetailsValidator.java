package com.it.uaa.oauth.validator.authorize;

import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.oauth.validator.AbstractOauthCodeValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  code 校验器
 */
public class CodeClientDetailsValidator extends AbstractOauthCodeValidator {

    private static final Logger LOG = LoggerFactory.getLogger(CodeClientDetailsValidator.class);

    private final  OAuthAuthzRequest OAuthAuthzRequest;

    public CodeClientDetailsValidator(OAuthAuthzRequest oAuthAuthzRequest) {
        OAuthAuthzRequest = oAuthAuthzRequest;
    }

    /*
    *  grant_type="authorization_code"
    *  ?response_type=code&scope=read,write&client_id=[client_id]&redirect_uri=[redirect_uri]&state=[state]
    * */
    @Override
    public OAuthResponse validateSelf(Oauth2ClientDetails clientDetails) throws OAuthSystemException {
        //validate redirect_uri
        final String redirectURI = OAuthAuthzRequest.getRedirectURI();
        if (redirectURI == null || !redirectURI.equals(clientDetails.getRedirectUri())) {
            LOG.debug("Invalid redirect_uri '{}' by response_type = 'code', client_id = '{}'", redirectURI, clientDetails.getClientId());
            return invalidRedirectUriResponse();
        }

        //validate scope
        final Set<String> scopes = OAuthAuthzRequest.getScopes();
        if (scopes.isEmpty() || excludeScopes(scopes, clientDetails)) {
            return invalidScopeResponse();
        }

        //validate state
        final String state = getState();
        if (StringUtils.isEmpty(state)) {
            LOG.debug("Invalid 'state', it is required, but it is empty");
            return invalidStateResponse();
        }

        return null;
    }

    private String getState() {
        return OAuthAuthzRequest.getState();
    }

    private OAuthResponse invalidStateResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.INVALID_REQUEST)
                .setErrorDescription("Parameter 'state'  is required")
                .buildJSONMessage();
    }

    @Override
    public org.apache.oltu.oauth2.as.request.OAuthAuthzRequest getRequest() {
        return OAuthAuthzRequest;
    }


}
