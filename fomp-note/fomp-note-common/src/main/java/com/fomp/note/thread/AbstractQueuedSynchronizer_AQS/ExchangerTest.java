package com.fomp.note.thread.AbstractQueuedSynchronizer_AQS;

import java.util.concurrent.Exchanger;

/**
 * Exchanger用于线程之间交换数据，其使用代码很简单,主要是使用是一个exchange()方法。
 *
 * 当一个线程运行到exchange()方法时会阻塞，另一个线程运行到exchange()时，
 * 二者交换数据，然后执行后面的程序
 */
public class ExchangerTest {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(()->{
            System.out.println(" thread 1 ");
            try {
                String exchange = exchanger.exchange(" thread 1 send data ");
                System.out.println(" thread 1 revicer data : " + exchange);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            System.out.println(" thread 2 ");
            try {
                String exchange = exchanger.exchange(" thread 2 send data ");
                System.out.println(" thread 2 revicer data : " + exchange);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
