package com.hjimi.resource.oauth2;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Auther: lxr
 * @Date: 2018/10/9 10:18
 * @Description:
 */
@Data
public class ImiOauthToken implements AuthenticationToken {

    private TokenDatail  tokenDatail;

    @Override
    public Object getPrincipal() {
        return tokenDatail;
    }

    @Override
    public Object getCredentials() {
        return tokenDatail.getToken();
    }
}
