package com.it.uaa.config;

import com.it.uaa.shiro.RedisCache;
import com.it.uaa.shiro.RedisCacheManager;
import com.it.uaa.shiro.RedisSessionDAO;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
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

/**
 * @Auther: lxr
 * @Date: 2018/9/29 13:47
 * @Description:  shiro核心配置
 */
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
    @ConditionalOnClass({RedisCacheManager.class, DefaultWebSessionManager.class})
    public DefaultWebSecurityManager securityManager(@Autowired RedisCacheManager redisCacheManager, @Autowired DefaultWebSessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(redisCacheManager);
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager);
        sessionManager.setDeleteInvalidSessions(true);//删除过期的session
        sessionManager.setSessionIdCookieEnabled(true);
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
    /**
     *  ------------------ session +cache----------------------
     */
    @Bean
    @ConditionalOnClass(RedisTemplate.class)
    public RedisSessionDAO redisSessionDAO(@Autowired RedisTemplate redisTemplate) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO(redisTemplate);
        return redisSessionDAO;
    }
    /**
     *  ------------------ com.it.uaa.shiro session manager----------------------
     */
    @Bean(name = "sessionManager")
    @ConditionalOnMissingBean
    @ConditionalOnClass(RedisSessionDAO.class)
    public DefaultWebSessionManager SessionManager(@Autowired RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
//        sessionManager.getSessionIdCookie().setDomain();
        sessionManager.setGlobalSessionTimeout(2*60*60*1000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationInterval(2*60*1000);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        return sessionManager;
    }
}
