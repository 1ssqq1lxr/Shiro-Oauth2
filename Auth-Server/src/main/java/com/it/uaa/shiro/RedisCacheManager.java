package com.it.uaa.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * @Auther: lxr
 * @Date: 2018/9/30 10:18
 * @Description:
 */
public class RedisCacheManager implements CacheManager {

    private final  Cache Cache;

    public RedisCacheManager(org.apache.shiro.cache.Cache cache) {
        Cache = cache;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return Cache;
    }

}
