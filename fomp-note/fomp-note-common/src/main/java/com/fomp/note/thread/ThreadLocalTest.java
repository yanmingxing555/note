package com.fomp.note.thread;

/**
 * ThreadLocal叫做线程变量：
 * ThreadLocal中填充的变量属于当前线程，该变量对其他线程而言是隔离的。
 * ThreadLocal为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。
 * 使用场景：
 *      1.线程间数据隔离
 *      2.进行事务操作，用于存储线程事务信息。
 *      3.数据库连接，Session会话管理。
 * 每个运行的线程都会有一个类型为ThreadLocal.ThreadLocalMap的map,
 * 这个map就是用来存储与这个线程绑定的变量,map的key就是ThreadLocal对象,
 * value就是线程正在执行的任务中的某个变量的包装类Entry.
 */
public class ThreadLocalTest {
    //可以通过set(T)方法来设置一个值，在当前线程下再通过get()方法获取原先设置的值；

    // 第一次get()方法调用时会进行初始化（如果set方法没有调用），每个线程会调用一次
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>() {
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };
    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }
    public static final long end() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }
}

