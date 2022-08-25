package com.fomp.note.redis.redisson;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁工具类
 */
@Component
public class RedissonLockUtil {
    private static final String PRE_LOCK = "REDISSON-LOCK-";

    private final RedissonClient redissonClient;

    public RedissonLockUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private RLock getRLock(String lockKey) {
        return redissonClient.getLock(PRE_LOCK + lockKey);
    }

    /**
     * 获取分布式锁
     * <strong>该方法必须手动释放锁</strong>
     * <strong>该方法如果获取不到锁会一直阻塞</strong>
     *
     * @param lockKey 锁名称
     */
    public void lock(String lockKey) {
        getRLock(lockKey).lock();
    }

    /**
     * 获取分布式锁
     * <strong>该方法如果获取不到锁会一直阻塞</strong>
     * <strong>该方法在没有手动释放锁的情况下会在设定时间内自动释放锁</strong>
     *
     * @param lockKey   锁名称
     * @param timeUnit  自动释放锁的时间单位
     * @param leaseTime 自动释放锁的时间
     */
    public void lock(String lockKey, TimeUnit timeUnit, long leaseTime) {
        getRLock(lockKey).lock(leaseTime, timeUnit);
    }

    /**
     * 尝试获取分布式锁
     * <strong>该方法不会自动释放锁</strong>
     * <strong>该方法如果获取不到锁会直接返回false</strong>
     *
     * @param lockKey 锁名称
     * @return 获取到锁返回true，否则返回false
     */
    public boolean tryLock(String lockKey) {
        return getRLock(lockKey).tryLock();
    }

    /**
     * 尝试获取分布式锁
     * <strong>该方法不会自动释放锁</strong>
     * <strong>该方法如果获取不到锁会会阻塞到设定的时间</strong>
     *
     * @param lockKey  锁名称
     * @param timeUnit 阻塞时间单位
     * @param waitTime 阻塞时间
     * @return 获取到锁返回true，否则返回false
     */
    public boolean tryLock(String lockKey, TimeUnit timeUnit, long waitTime) {
        try {
            return getRLock(lockKey).tryLock(waitTime, timeUnit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 尝试获取分布式锁
     * <strong>该方法在没有手动释放锁的情况下会在设定时间内自动释放锁</strong>
     * <strong>该方法如果获取不到锁会会阻塞到设定的时间</strong>
     *
     * @param lockKey   锁名称
     * @param timeUnit  时间单位
     * @param leaseTime 自动释放时间
     * @param waitTime  阻塞时间
     * @return 获取到锁返回true，否则返回false
     */
    public boolean tryLock(String lockKey, TimeUnit timeUnit, int leaseTime, int waitTime) {
        try {
            return getRLock(lockKey).tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 解锁
     *
     * @param lockKey 锁名称
     */
    public void unLock(String lockKey) {
        getRLock(lockKey).unlock();
    }

    /**
     * 是否已经有锁
     *
     * @param lockKey 锁名称
     * @return 已经存在锁则返回true，否则返回false
     */
    public boolean isLocked(String lockKey) {
        return getRLock(lockKey).isLocked();
    }

}