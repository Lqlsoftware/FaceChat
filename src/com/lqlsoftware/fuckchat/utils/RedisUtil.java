package com.lqlsoftware.fuckchat.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 封装
 * @author robinlu
 */
public final class RedisUtil {

    //Redis服务器IP
    private static final String ADDR = "localhost";
    //Redis的端口号
    private static final int PORT = 6379;
    //访问密码
    //private static String AUTH = "lqlsoftware";

    /* 可用连接实例的最大数目  默认值8   -1表示不限制
       如果pool已经分配了maxActive个jedis实例 则此时pool耗尽
     */
    private static final int MAX_TOTAL = 1024;
    
    //一个pool最多有多少个空闲的的jedis实例 默认值8。
    private static final int MAX_IDLE = 200;
    
    //等待可用连接的最大时间 单位毫秒 默认值-1
    private static final int MAX_WAIT_MILLIS = 10000;
    
    private static final int TIMEOUT = 10000;
    
    //在borrow一个jedis实例时 是否提前进行validate操作
    //如果为true 则得到的jedis实例均是可用的
    private static final boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;

    /*
      初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT_MILLIS);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取Jedis实例
     * @return jedis
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}