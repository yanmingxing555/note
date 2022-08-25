package com.fomp.note.redis.jedis;

import redis.clients.jedis.Jedis;

/**
 * Jedis操作Redis
 */
public class JedisTest {
    public static void main(String[] args) {
        //连接redis
        //设置为主节点，可读写；设置为从节点，只可读
        Jedis jedis = new Jedis("10.10.10.157",6379);
        //jedis.auth("123456");
        String pong = jedis.ping();
        System.out.println(pong);

        key(jedis);
        jedis.close();
    }
    /**
     * Jedis-API：Key
     */
    public static void key(Jedis jedis){
        /*jedis.flushDB();
        jedis.set("k1","v11");
        jedis.set("k2","v22");
        jedis.set("k3","v33");*/
        System.out.println(jedis.keys("*"));
        System.out.println(jedis.exists("k1"));
        System.out.println(jedis.get("k1"));
        System.out.println(jedis.ttl("k1"));
        System.out.println(jedis.type("k1"));
        //System.out.println(jedis.del("k1"));
        System.out.println(jedis.keys("*"));
    }
}
