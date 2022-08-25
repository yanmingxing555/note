package com.fomp.note.thread.AbstractQueuedSynchronizer_AQS;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier（栅栏屏障）
 * 1、让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，
 *    屏障才会开门，所有被屏障拦截的线程才会继续运行
 * 2、CyclicBarrier默认的构造方法是CyclicBarrier（int parties），
 *   其参数表示屏障拦截的线程数量，每个线程调用await方法告CyclicBarrier我已经到达了屏障，然后当前线程被阻塞；
 * 3、可重复：一组任务执行完毕后，立刻进入初始化，可以重复使用
 */
public class CyclicBarrierTest implements Runnable {
    private CyclicBarrier cyclicBarrier;
    private int index ;
    public CyclicBarrierTest(CyclicBarrier cyclicBarrier, int index) {
        this.cyclicBarrier = cyclicBarrier;
        this.index = index;
    }
    public void run() {
        try {
            System.out.println("index: " + index);
            index--;
            cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(11, new Runnable() {
            public void run() {
                System.out.println("所有特工到达屏障，准备开始执行秘密任务");
            }
        });
        for (int i = 0; i < 10; i++) {
            new Thread(new CyclicBarrierTest(cyclicBarrier, i)).start();
        }
        cyclicBarrier.await();
        System.out.println("全部到达屏障....1");
        //重复使用栅栏屏障
        for (int i = 0; i < 10; i++) {
            new Thread(new CyclicBarrierTest(cyclicBarrier, i)).start();
        }
        cyclicBarrier.await();
        System.out.println("全部到达屏障....2");
    }
}

