package com.fomp.note.thread.AbstractQueuedSynchronizer_AQS;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch（计数器）：CountDownLatch允许一个或者多个线程去等待其他线程完成操作。
 * 1、通过一个计数器来实现的，计数器的初始值为线程的数量。
 *    每当一个线程完成了自己的任务后，计数器的值就会减1。
 *    当计数器值到达0时，它表示所有的线程已经完成了任务，然后在闭锁上等待的线程就可以恢复执行任务
 * 2、不可重复：一组任务执行完毕就结束
 * 方法	                                    说明
 * await()	                            使当前线程进入同步队列进行等待，直到latch的值被减到0或者当前线程被中断，当前线程就会被唤醒。
 * await(long timeout, TimeUnit unit)	带超时时间的await()。
 * countDown()	                        使latch的值减1，如果减到了0，则会唤醒所有等待在这个latch上的线程。
 * getCount()	                        获得latch的数值。
 */
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        //CountDownLatch接收一个int型参数，表示要等待的工作线程的个数。
        // 让2个线程去等待3个三个工作线程执行完成
        CountDownLatch c = new CountDownLatch(3);

        // 2 个等待线程
        WaitThread waitThread1 = new WaitThread("wait-thread-1", c);
        WaitThread waitThread2 = new WaitThread("wait-thread-2", c);

        // 3个工作线程
        Worker worker1 = new Worker("worker-thread-1", c);
        Worker worker2 = new Worker("worker-thread-2", c);
        Worker worker3 = new Worker("worker-thread-3", c);

        // 启动所有线程
        waitThread1.start();
        waitThread2.start();
        Thread.sleep(1000);
        worker1.start();
        worker2.start();
        worker3.start();
    }
}

/**
 * 等待线程
 */
class WaitThread extends Thread {

    private String name;
    private CountDownLatch c;

    public WaitThread(String name, CountDownLatch c) {
        this.name = name;
        this.c = c;
    }

    @Override
    public void run() {
        try {
            // 等待
            System.out.println(this.name + " wait...");
            c.await();
            System.out.println(this.name + " continue running...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 工作线程
 */
class Worker extends Thread {

    private String name;
    private CountDownLatch c;

    public Worker(String name, CountDownLatch c) {
        this.name = name;
        this.c = c;
    }

    @Override
    public void run() {
        System.out.println(this.name + " is running...");
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name + " is end.");
        c.countDown();
    }
}
