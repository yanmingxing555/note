package com.fomp.note.redis.jedissentinel;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: ymx
 * @date: 2022/8/16
 * @version: 1.0.0.0
 */
public class JedisFromSentinelPool {
    private static JedisSentinelPool jedisSentinelPool=null;

    public static  Jedis getJedisFromSentinel(){
        if (jedisSentinelPool==null){
            Set<String> sentinelSet=new HashSet<>();
            sentinelSet.add("10.10.10.157:26379");

            JedisPoolConfig jedisPoolConfig =new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(10); //最大可用连接数
            jedisPoolConfig.setMaxIdle(5); //最大闲置连接数
            jedisPoolConfig.setMinIdle(5); //最小闲置连接数
            jedisPoolConfig.setBlockWhenExhausted(true); //连接耗尽是否等待
            jedisPoolConfig.setMaxWaitMillis(2000); //等待时间
            jedisPoolConfig.setTestOnBorrow(true); //取连接的时候进行一下测试 ping pong

            //无密码
            jedisSentinelPool=new JedisSentinelPool("mymaster",sentinelSet,jedisPoolConfig);
            //有密码方式
            //jedisSentinelPool=new JedisSentinelPool("mymaster",sentinelSet,jedisPoolConfig,"123456");
            return jedisSentinelPool.getResource();
        }else{
            return jedisSentinelPool.getResource();
        }
    }

    public static void main(String[] args) {
        Jedis jedis = getJedisFromSentinel();
        System.out.println(jedis.keys("*"));
        jedis.set("jedis","jedisSentinelPool");
        System.out.println(jedis.get("jedis"));
        jedis.close();
        jedisSentinelPool.close();
    }
}
