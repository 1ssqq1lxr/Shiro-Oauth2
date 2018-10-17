package com.it.uaa.config;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: lxr
 * @Date: 2018/9/30 11:29
 * @Description: oauth2 id生成
 */
@Configuration
public class Oauth2Config {

    @Bean
    public OAuthIssuer initOAuthIssuer(){
        return  new OAuthIssuerImpl(new MD5Generator());
    }

}
