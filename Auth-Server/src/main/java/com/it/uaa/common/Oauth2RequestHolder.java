package com.it.uaa.common;


import com.it.uaa.exception.OauthDefindException;
import com.it.uaa.oauth.OAuthTokenxRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:
 */
public class Oauth2RequestHolder {
    private  static final ThreadLocal<OauthHttpOptions> request = new ThreadLocal<>();

    public static void saveHttpOptions(OauthHttpOptions OAuthRequest){
        request.set(OAuthRequest);
    }
    public static OauthHttpOptions getHttpOptions(){
        return  request.get();
    }
    public  static void removeHttpOptions(){
          request.remove();
    }

    public  static OAuthTokenxRequest getOauthRequest(){
      return  Optional.ofNullable(request.get())
                .map(oauthHttpOptions -> oauthHttpOptions.getOAuthRequest())
                .orElseThrow(()->new OauthDefindException("请求上下文不含有 oauth2Request"));
    }

    public  static HttpServletResponse getHttpServletResponse(){
        return  Optional.ofNullable(request.get())
                .map(oauthHttpOptions -> oauthHttpOptions.getHttpServletResponse())
                .orElseThrow(()->new OauthDefindException("请求上下文不含有 httpServletResponse"));
    }

}
