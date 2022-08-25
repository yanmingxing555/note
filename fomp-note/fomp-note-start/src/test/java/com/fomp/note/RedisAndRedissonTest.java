package com.fomp.note;

import com.fomp.note.redis.redisson.RedissonLockUtil;
import com.fomp.note.redis.redistemplate.RedisCacheUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author: ymx
 * @date: 2022/8/15
 * @version: 1.0.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisAndRedissonTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Resource
    private RedisCacheUtil redisCacheUtil;
    @Resource
    private RedisTemplate myRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedissonLockUtil redissonLockUtil;

    @Test
    public void testRedissonClient() {
        testRedissonClient1();
        String lockKey = "lockTest";
        redissonLockUtil.lock(lockKey);
        System.out.println("获取到lockTest-001");
        redissonLockUtil.unLock(lockKey);
        if (redissonLockUtil.tryLock(lockKey)){
            System.out.println("获取到lockTest-002");
            redissonLockUtil.unLock(lockKey);
        }
    }
    public void testRedissonClient1(){
        RLock lock = redissonClient.getLock("lockTest");
        lock.lock();
        System.out.println("获取到lockTest-001");
        lock.unlock();
        if (lock.tryLock()){
            System.out.println("获取到lockTest-002");
            lock.unlock();
        }
    }

    @Test
    public void testRedis() throws Exception{
        //Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach( name->System.out.println(name));
    }
    // ============================String=============================
    @Test
    public void testRedisString(){
        myRedisTemplate.opsForValue().set("k1","v1");
        myRedisTemplate.opsForValue().set("k2","v2");
        myRedisTemplate.opsForValue().set("k3","v3");
        myRedisTemplate.keys("*").stream().forEach(key-> System.out.println(key));
        Object value = myRedisTemplate.opsForValue().get("k1");
        System.out.println(String.valueOf(value));
        myRedisTemplate.opsForValue().set("k4","v4",10, TimeUnit.SECONDS);
        myRedisTemplate.expire("k3",10,TimeUnit.SECONDS);
    }


}
