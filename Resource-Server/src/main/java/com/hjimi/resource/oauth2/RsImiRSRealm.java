package com.hjimi.resource.oauth2;

import com.hjimi.resource.repository.UserRepository;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @Auther: lxr
 * @Date: 2018/10/8 16:49
 * @Description:
 */
public   class RsImiRSRealm extends AuthorizingRealm {

    private final UserRepository userRepository;

    public RsImiRSRealm(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        ImiOauthToken upToken = (ImiOauthToken) token;
        TokenDatail tokenDatail = (TokenDatail) upToken.getPrincipal();
        return new SimpleAuthenticationInfo(tokenDatail, token.getCredentials().toString().toCharArray(), getName());
    }

    @Override
    public Class getAuthenticationTokenClass() {
        return ImiOauthToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        TokenDatail tokenDatail = (TokenDatail) getAvailablePrincipal(principals);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        switch (tokenDatail.getOauth2Type()){
            case RESFRESH_TOKEN:
            case PASSWORD:
            case CLIENT:
            case CODE:
        }
        info.addStringPermission("test");
        return info;
    }

    @Override
    protected Object getAuthenticationCacheKey(AuthenticationToken token) {
        ImiOauthToken upToken = (ImiOauthToken) token;
        return upToken.getTokenDatail().getToken();
    }

    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        TokenDatail tokenDatail = (TokenDatail) getAvailablePrincipal(principals);
        return tokenDatail.getToken();
    }
}
