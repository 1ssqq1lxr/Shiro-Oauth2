package com.it.uaa.common.infrastructure.shiro;


import org.apache.shiro.authc.AuthenticationInfo;

import java.util.Set;


public interface IRealm {

    AuthenticationInfo getUser(String username);

    Set<String> getRoles(String username);

    Set<String> getPermissions(String username);
}
