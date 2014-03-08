package com.events.common.nosql.redis;

import com.events.common.Utility;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 3/7/14
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class RedisDAO {
    public static int put(String sResource, HashMap<String,String> hmRecords) {
        int iRowsAffected = 0;
        if(hmRecords!=null && !hmRecords.isEmpty() ) {
            Jedis jedis = getJedis(sResource);
            if(jedis!=null){
                for(Map.Entry<String,String> mapRecords : hmRecords.entrySet() ) {
                    String response = jedis.set(mapRecords.getKey(), mapRecords.getValue() );
                    if("OK".equalsIgnoreCase(response)) {
                        iRowsAffected = 1;
                    }
                }
            }
        }
        return iRowsAffected;
    }

    public static Long getId( String sResource, String key ) {
        Long lId = 0L;
        if(!Utility.isNullOrEmpty(key) ) {
            Jedis jedis = getJedis(sResource);
            if(jedis!=null){
                lId = jedis.incr( key );
            }
        }
        return lId;
    }

    public static Long pushId( String sResource, String key, String id ) {
        Long lId = 0L;
        if(!Utility.isNullOrEmpty(key) ) {
            Jedis jedis = getJedis(sResource);
            if(jedis!=null){
                lId = jedis.lpush(key,id);
            }
        }
        return lId;
    }

    private static Jedis getJedis(String sResource){
        Jedis jedis = null;
        if(!Utility.isNullOrEmpty(sResource) ) {
            Pool pool = Pool.getInstance(  sResource );
            if(pool!=null) {
                JedisPool jedisPool = pool.getJedisPool();
                if(jedisPool!=null ) {
                    jedis = jedisPool.getResource();
                }
            }
        }
        return jedis;
    }
}
