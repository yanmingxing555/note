package com.fomp.note.thread.AbstractQueuedSynchronizer_AQS;

/**
 * synchronized显示锁：一种对象锁，作用粒度是对象，是可重入的
 *  加锁的方式：
 *  1.同步普通方法，锁当前（this）实例对象
 *  2.同步静态方法，锁是当前类对象
 *  3.同步代码块，锁是括号里面的对象
 */
public class SynchronizedTest {
    public static void main(String[] args) {
        SynchronizedTest test = new SynchronizedTest();
    }

    /**
     * 1.同步普通方法，锁当前（this）实例对象
     */
    public synchronized void doSomething1() {
        System.out.println(Thread.currentThread().getName() + "进入方法");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "对当前对象加锁,不同的线程同时访问该对象时，将同步执行");
    }
    /**
     * 2.同步静态方法，锁是当前类对象
     */
    public static synchronized void doSomething3() {
        System.out.println(Thread.currentThread().getName() + "进入方法");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "对当前类对象加锁，其他线程访问该类对象时将同步执行");
    }

    /**
     * 3.同步代码块，锁是括号里面的对象
     *  ①如果括号里面是类对象，则对这个类所有new的对象加锁
     */
    public void doSomething4() {
        synchronized (SynchronizedTest.class) {
            System.out.println(Thread.currentThread().getName() + "进入代码块");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "对该类对象加锁,不同的线程同时访问该类new的所有对象时，将同步执行");
        }
    }
    /**
     * 3.同步代码块，锁是括号里面的对象
     *  ②如果括号里面是对象，则对括号里的对象加锁
     */
    public void doSomething5() {
        String object = new String();
        synchronized (object) {
            System.out.println(Thread.currentThread().getName() + "进入代码块");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "不同线程获取的是不同对象，因此不同步执行");
        }
    }
    private String object = new String();
    public void doSomething6() {
        synchronized (object) {
            System.out.println(Thread.currentThread().getName() + "进入代码块");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "不同线程获取的是同一个对象，因此同步执行");
        }
    }
    /**
     * 3.同步代码块，锁是括号里面的对象
     *  ③如果括号里是this，则对当前对象加锁
     */
    public void doSomething2() {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + "进入代码块");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "对当前对象加锁,不同的线程同时访问该对象时，将同步执行");
        }
    }
}
