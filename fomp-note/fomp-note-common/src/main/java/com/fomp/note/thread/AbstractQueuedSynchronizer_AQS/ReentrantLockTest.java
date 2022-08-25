package com.fomp.note.thread.AbstractQueuedSynchronizer_AQS;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 默认采用非公平锁，除非在构造方法中传入参数 true
 *     getHoldCount()：当前线程调用 lock() 方法的次数
 *     getQueueLength()：当前正在等待获取 Lock 锁的线程的估计数
 *     getWaitQueueLength(Condition condition)：当前正在等待状态的线程的估计数，需要传入 Condition 对象
 *     hasWaiters(Condition condition)：查询是否有线程正在等待与 Lock 锁有关的 Condition 条件
 *     hasQueuedThread(Thread thread)：查询指定的线程是否正在等待获取 Lock 锁
 *     hasQueuedThreads()：查询是否有线程正在等待获取此锁定
 *     isFair()：判断当前 Lock 锁是不是公平锁
 *     isHeldByCurrentThread()：查询当前线程是否保持此锁定
 *     isLocked()：查询此锁定是否由任意线程保持
 *     tryLock()：线程尝试获取锁，如果获取成功，则返回 true，如果获取失败（即锁已被其他线程获取），则返回 false
 *     tryLock(long timeout，TimeUnit unit)：线程如果在指定等待时间内获得了锁，就返回true，否则返回 false
 *     lockInterruptibly()：如果当前线程未被中断，则获取该锁定，如果已经被中断则出现异常
 */
public class ReentrantLockTest {

    private ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) {
        ReentrantLockTest test = new ReentrantLockTest();
        test.getHoldCountTest1();
    }
    public  void getHoldCountTest1(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " method1 getHoldCount："
                    + lock.getHoldCount());
            getHoldCountTest2();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public  void getHoldCountTest2(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " method1 getHoldCount："
                    + lock.getHoldCount());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
