package com.it.uaa.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 18:29
 * @Description:
 */
public class OAuth2SubjectFactory extends DefaultWebSubjectFactory {


    @Override
    public Subject createSubject(SubjectContext context) {


        boolean authenticated = context.isAuthenticated();

        if (authenticated) {

            AuthenticationToken token = context.getAuthenticationToken();

            if (token != null && token instanceof OAuth2Token) {
                OAuth2Token oAuth2Token = (OAuth2Token) token;
                if (oAuth2Token.isRememberMe()) {
                    context.setAuthenticated(false);
                }
            }
        }

        return super.createSubject(context);
    }
}
