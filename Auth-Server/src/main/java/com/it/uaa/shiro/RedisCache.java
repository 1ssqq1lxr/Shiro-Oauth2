package com.it.uaa.shiro;

import lombok.Data;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.Collection;
import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/9/30 10:17
 * @Description:
 */

@Data
public class RedisCache<K, V> implements Cache<K, V> {

    private String keyPrefix = "shiro_cache:";

    private final org.springframework.data.redis.core.RedisTemplate<K,V> redisTemplate;

    public RedisCache(org.springframework.data.redis.core.RedisTemplate<K,V> redisTemplate) {
       this.redisTemplate = redisTemplate;
    }

    @Override
    public V get(K key) throws CacheException {
       return redisTemplate.boundValueOps(key).get();
    }

    @Override
    public V put(K key, V value) throws CacheException {
        return   redisTemplate.opsForValue().getAndSet(key,value);
    }

    @Override
    public V remove(K key) throws CacheException {
        V v = get(key);
        redisTemplate.delete(key);
        return v ;
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.delete(redisTemplate.keys((K)keyPrefix));
    }

    @Override
    public int size() {
        try {
            Long longSize = new Long(redisTemplate.keys((K)keyPrefix).size());
            return longSize.intValue();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        return redisTemplate.keys((K)keyPrefix);
    }

    @Override
    public Collection<V> values() {
       return redisTemplate.opsForValue().multiGet(keys());
    }
}