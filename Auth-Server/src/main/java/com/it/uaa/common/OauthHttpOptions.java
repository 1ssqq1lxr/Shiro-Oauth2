package com.it.uaa.common;

import com.it.uaa.oauth.OAuthTokenxRequest;
import lombok.Builder;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:53
 * @Description:
 */
@Data
@Builder
public class OauthHttpOptions {

    private OAuthTokenxRequest oAuthRequest;

    private HttpServletResponse httpServletResponse;

}
