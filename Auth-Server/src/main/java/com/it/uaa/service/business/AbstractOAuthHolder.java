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
package com.it.uaa.service.business;

import com.it.uaa.common.domain.shared.BeanProvider;
import com.it.uaa.oauth.OauthCacheRepository;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.shiro.SecurityUtils;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  AbstractOAuthHolder 类封装
 */
public abstract class AbstractOAuthHolder {

    protected OauthCacheRepository oauthRepository = BeanProvider.getBean(OauthCacheRepository.class);

    protected  OAuthIssuer oAuthIssuer = BeanProvider.getBean(OAuthIssuer.class);

    protected String currentUsername() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }

}
