package com.hjimi.resource.config;

import com.hjimi.resource.shiro.RedisCache;
import com.hjimi.resource.shiro.RedisCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
@EnableConfigurationProperties({RedisProperties.class})
public class ShiroManager {


    @Bean(name = "lifecycleBeanPostProcessor")
    @ConditionalOnMissingBean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "defaultAdvisorAutoProxyCreator")
    @ConditionalOnMissingBean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;

    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return new AuthorizationAttributeSourceAdvisor();
    }


    @Bean(name = "securityManager")
    @ConditionalOnMissingBean
    @ConditionalOnClass({RedisCacheManager.class})
    public DefaultWebSecurityManager securityManager(@Autowired RedisCacheManager redisCacheManagerr) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(redisCacheManagerr);
        return securityManager;
    }

    /**
     *  ------------------ cache ----------------------
     */
    @Bean(name = "shiroCacheManager")
    @ConditionalOnMissingBean
    @ConditionalOnClass(Cache.class)
    public RedisCacheManager cacheManager(@Autowired Cache cache) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(cache);
        return redisCacheManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public Cache InitCache(@Autowired RedisTemplate redisTemplate) {
        return new RedisCache(redisTemplate);
    }

}
