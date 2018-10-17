package com.hjimi.resource.config;

import com.hjimi.resource.ResourceOauth2Filter;
import com.hjimi.resource.oauth2.RsImiRSRealm;
import com.hjimi.resource.repository.UserRepository;
import com.hjimi.resource.service.Oauth2RsService;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Import(ShiroManager.class)
public class ShiroConfig {

	private final String resourceId ="test-resources";


	@Bean(name = "shiroFilter")
	@DependsOn("securityManager")
	@ConditionalOnMissingBean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager, Realm realm,@Autowired AuthenticatingFilter authenticatingFilter) {
		securityManager.setRealm(realm);

		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.getFilters().put("oauth2",authenticatingFilter);
		shiroFilter.setSecurityManager(securityManager);
		shiroFilter.setUnauthorizedUrl("/403");
		Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
        filterChainDefinitionMap.put("/**", "oauth2"); // 放行所有请求
		shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilter;
	}

	@Bean
	public Realm initRelam(@Autowired UserRepository userRepository){
		return  new RsImiRSRealm(userRepository);
	}

	@Bean
	public AuthenticatingFilter initFilter(@Autowired Oauth2RsService oauth2RsService){
		return  new ResourceOauth2Filter(resourceId,oauth2RsService);
	}
}
