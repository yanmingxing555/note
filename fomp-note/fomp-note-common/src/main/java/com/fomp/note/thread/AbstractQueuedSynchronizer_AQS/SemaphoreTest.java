package com.fomp.note.thread.AbstractQueuedSynchronizer_AQS;
import java.util.concurrent.Semaphore;
/**
 * Semaphore（信号量） :可用于流量控制，限制最大的并发访问数
 * 控制访问特定资源的线程数目，资源访问，
 * 服务限流:指定特定数目的票据，当线程拿到票据后才能执行同步的代码，否则等待
 */

/**
 * Semaphore可以控制同时访问共享资源的线程个数，
 * 线程通过 acquire方法获取一个信号量，信号量减一，如果没有就等待；
 * 通过release方法释放一个信号量，信号量加一。
 * 它通过控制信号量的总数量，以及每个线程所需获取的信号量数量，
 * 进而控制多个线程对共享资源访问的并发度，以保证合理的使用共享资源。
 * 相比synchronized和独占锁一次只能允许一个线程访问共享资源，功能更加强大，有点类似于共享锁！
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        //控制并发量
        Semaphore semaphore = new Semaphore(2);
        for (int i=0;i<5;i++){
            new Thread(new Task(semaphore,"yangguo+"+i)).start();
        }
    }
    static class Task extends Thread{
        Semaphore semaphore;
        public Task(Semaphore semaphore,String tname){
            this.semaphore = semaphore;
            this.setName(tname);
        }
        public void run() {
            try {
                semaphore.acquire();//获取公共资源
                System.out.println(Thread.currentThread().getName()+":aquire() at time:"+System.currentTimeMillis());
                Thread.sleep(3000);
                semaphore.release();//释放公共资源
                System.out.println(Thread.currentThread().getName()+":aquire() at time:"+System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
