package com.it.uaa.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Auther: lxr
 * @Date: 2018/9/30 10:19
 * @Description:
 */
public class RedisSessionDAO extends AbstractSessionDAO {


    /**
     * The Redis key prefix for the sessions
     */
    private String keyPrefix = "shiro_session:";


    private final org.springframework.data.redis.core.RedisTemplate<String,Session> redisTemplate;

    public RedisSessionDAO(RedisTemplate<String, Session> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    /**
     * save session
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException{
          redisTemplate.opsForValue().set(keyPrefix+session.getId(),session);
    }

    @Override
    public void delete(Session session) {
        redisTemplate.delete(keyPrefix+session.getId());

    }

    @Override
    public Collection<Session> getActiveSessions() {
        return redisTemplate.opsForValue().multiGet(redisTemplate.keys(keyPrefix));
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return redisTemplate.boundValueOps(keyPrefix+sessionId).get();
    }


}
