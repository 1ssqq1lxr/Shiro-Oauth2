package com.it.uaa.oauth.validator;

import com.it.uaa.common.Oauth2RequestHolder;
import com.it.uaa.common.domain.shared.BeanProvider;
import com.it.uaa.domain.Oauth2ClientDetails;
import com.it.uaa.service.OauthService;
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
 * @Description:  AbstractClientDetailsValidator 校验器
 */
public abstract class AbstractClientDetailsValidator {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractClientDetailsValidator.class);


    protected OauthService oauthService = BeanProvider.getBean(OauthService.class);



    protected Oauth2ClientDetails clientDetails() {
        return oauthService.loadClientDetails(Oauth2RequestHolder.getOauthRequest().getClientId());
    }


    protected OAuthResponse invalidClientErrorResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                .setErrorDescription("Invalid client_id '" + Oauth2RequestHolder.getOauthRequest().getClientId() + "'")
                .buildJSONMessage();
    }

    protected OAuthResponse invalidRedirectUriResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.INVALID_REQUEST)
                .setErrorDescription("Invalid redirect_uri '" + Oauth2RequestHolder.getOauthRequest().getRedirectURI() + "'")
                .buildJSONMessage();
    }

    protected OAuthResponse invalidScopeResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.CodeResponse.INVALID_SCOPE)
                .setErrorDescription("Invalid scope '" + Oauth2RequestHolder.getOauthRequest().getScopes() + "'")
                .buildJSONMessage();
    }

    protected OAuthResponse invalidClientSecretResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                .setErrorDescription("Invalid client_secret by client_id '" + Oauth2RequestHolder.getOauthRequest().getClientId() + "'")
                .buildJSONMessage();
    }


    public final OAuthResponse validate() throws OAuthSystemException {
        final Oauth2ClientDetails details = clientDetails();
        if (details == null) {
            return invalidClientErrorResponse();
        }

        return validateSelf(details);
    }


    protected boolean excludeScopes(Set<String> scopes, Oauth2ClientDetails clientDetails) {
        final String clientDetailsScope = clientDetails.getScope();          //read write
        for (String scope : scopes) {
            if (!clientDetailsScope.contains(scope)) {
                LOG.debug("Invalid scope - ClientDetails scopes '{}' exclude '{}'", clientDetailsScope, scope);
                return true;
            }
        }
        return false;
    }





    protected abstract OAuthResponse validateSelf(Oauth2ClientDetails clientDetails) throws OAuthSystemException;
}
