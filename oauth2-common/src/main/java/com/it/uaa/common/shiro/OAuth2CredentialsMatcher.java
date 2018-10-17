/*
 * Copyright (c) 2015 MONKEYK Information Technology Co. Ltd
 * www.monkeyk.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * MONKEYK Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with MONKEYK Information Technology Co. Ltd.
 */
package com.it.uaa.common.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 18:29
 * @Description:
 */
public class OAuth2CredentialsMatcher implements CredentialsMatcher {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2CredentialsMatcher.class);

    // authz module
    private CredentialsMatcher authzCredentialsMatcher;
    //resources module
    private CredentialsMatcher resourcesCredentialsMatcher;

    public OAuth2CredentialsMatcher() {
    }


    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        LOG.debug("Do credentials match, token: {}, info: {}", token, info);

        if (token instanceof OAuth2Token) {
            LOG.debug("Call [resources] CredentialsMatcher: {}", resourcesCredentialsMatcher);
            return resourcesCredentialsMatcher.doCredentialsMatch(token, info);
        } else {
            LOG.debug("Call [authz] CredentialsMatcher: {}", authzCredentialsMatcher);
            return authzCredentialsMatcher.doCredentialsMatch(token, info);
        }

    }


    public void setAuthzCredentialsMatcher(CredentialsMatcher authzCredentialsMatcher) {
        this.authzCredentialsMatcher = authzCredentialsMatcher;
    }

    public void setResourcesCredentialsMatcher(CredentialsMatcher resourcesCredentialsMatcher) {
        this.resourcesCredentialsMatcher = resourcesCredentialsMatcher;
    }
}
