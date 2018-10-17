package com.it.uaa.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
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



	@Bean(name = "shiroFilter")
	@DependsOn("securityManager")
	@ConditionalOnMissingBean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager, Realm realm) {
		securityManager.setRealm(realm);

		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
//        shiroFilter.getFilters().put("ew",n)
		shiroFilter.setSecurityManager(securityManager);

//
//		shiroFilter.setLoginUrl("/admin/login");
//		shiroFilter.setSuccessUrl("/admin/index");
//		shiroFilter.setUnauthorizedUrl("/previlige/no");
		Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
        filterChainDefinitionMap.put("/**", "anon"); // 放行所有请求
//		filterChainDefinitionMap.put("/assets/**", "anon");
//        filterChainDefinitionMap.put("/redis/**", "anon");
//		filterChainDefinitionMap.put("/admin/login", "anon");
//
//		filterChainDefinitionMap.put("/admin/user/index", "perms[system:user:index]");
//		filterChainDefinitionMap.put("/admin/user/add", "perms[system:user:add]");
//		filterChainDefinitionMap.put("/admin/user/edit*", "perms[system:user:edit]");
//		filterChainDefinitionMap.put("/admin/user/deleteBatch", "perms[system:user:deleteBatch]");
//		filterChainDefinitionMap.put("/admin/user/grant/**", "perms[system:user:grant]");
//
//		filterChainDefinitionMap.put("/admin/role/index", "perms[system:role:index]");
//		filterChainDefinitionMap.put("/admin/role/add", "perms[system:role:add]");
//		filterChainDefinitionMap.put("/admin/role/edit*", "perms[system:role:edit]");
//		filterChainDefinitionMap.put("/admin/role/deleteBatch", "perms[system:role:deleteBatch]");
//		filterChainDefinitionMap.put("/admin/role/grant/**", "perms[system:role:grant]");
//
//		filterChainDefinitionMap.put("/admin/resource/index", "perms[system:resource:index]");
//		filterChainDefinitionMap.put("/admin/resource/add", "perms[system:resource:add]");
//		filterChainDefinitionMap.put("/admin/resource/edit*", "perms[system:resource:edit]");
//		filterChainDefinitionMap.put("/admin/resource/deleteBatch", "perms[system:resource:deleteBatch]");
//
//		filterChainDefinitionMap.put("/admin/**", "authc");
		shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilter;
	}
}
