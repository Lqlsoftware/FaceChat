package com.lqlsoftware.XPM.utils;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

/**
 * 通过 Redis 存储和验证 token 的实现类
 * @author ScienJus
 * @date 2015/7/31.
 */
@Component
public class TokenManager implements ITokenManager {
	
	private final Long sessionExpireTime = (long) 2592000 * 1000;
	
    private Jedis jedis;

    private static Logger log = Logger.getLogger(TokenManager.class);

    private void setRedis() {
        this.jedis = RedisUtil.getJedis();
    }
    public TokenModel createToken (String userId) {
        // 使用 UUID 作为源 token
        String token = UUID.randomUUID ().toString ().replace ("-", "");
        TokenModel model = new TokenModel (userId, token);
        // 存储到 redis 并设置过期时间
        setRedis();
		jedis.set(userId, token);
		jedis.expireAt(userId,sessionExpireTime);
		jedis.close();
        log.info("<-- create new Token id:" + userId + ";token:" + model.getAuthentication() + " -->");
        return model;
    }
    public TokenModel getToken (String authentication) {
        if (authentication == null || authentication.length () == 0) {
            return null;
        }
        String [] param = authentication.split ("_");
        if (param.length != 2) {
            return null;
        }
        // 使用 userId 和源 token 简单拼接成的 token，可以增加加密措施
        String userId = param [0];
        String token = param [1];

        return new TokenModel (userId, token);
    }
    public boolean checkToken (TokenModel model) {
        if (model == null) {
            return false;
        }
        setRedis();
        String token = jedis.get(model.getUserId());
        if (token == null || !token.equals (model.getToken ())) {
            return false;
        }
        // 如果验证成功，说明此用户进行了一次有效操作，延长 token 的过期时间
		jedis.expireAt(model.getUserId (),sessionExpireTime);
		jedis.close();
        log.info("<-- ckeck Token id:" + model.getUserId() + ";token:" + model.getAuthentication() + " -->");
        return true;
    }
    public void deleteToken (String userId) {
    	setRedis();
        jedis.del (userId);
        jedis.close();
        log.info("<-- delete Token id:" + userId + " -->");
    }
}