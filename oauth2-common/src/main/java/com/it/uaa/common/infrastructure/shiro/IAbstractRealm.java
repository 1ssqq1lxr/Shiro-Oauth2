package com.it.uaa.common.infrastructure.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Optional;


public abstract class IAbstractRealm extends AuthorizingRealm implements IRealm{

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return  getUsername(token)
                .map(user->getUser(user)).orElseThrow(()->new UnknownAccountException());
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) getAvailablePrincipal(principals);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(getPermissions(username));
        info.addRoles(getRoles(username));
        return info;
    }
    private Optional<String> getUsername(AuthenticationToken token) {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        return Optional.ofNullable(upToken.getUsername());
    }



}
