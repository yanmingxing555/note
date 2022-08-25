package com.fomp.note.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 线程创建线程和启动
 */
public class ThreatStart {
    public static void main(String[] args) {
        //1.继承Thread类创建线程
        SomeThead oneThread = new SomeThead();
        oneThread.start();

        //2.实现Runnable接口创建线程类
        Runnable oneRunnable = new SomeRunnable();
        Thread runnableThread = new Thread(oneRunnable);
        runnableThread.start();

        //3.通过Callable和Future创建线程
        Callable oneCallable = new SomeCallable();
        //由Callable创建一个FutureTask对象：
        FutureTask oneTask = new FutureTask(oneCallable);
        //注释： FutureTask是一个包装器，它通过接受Callable来创建，它同时实现了 Future和Runnable接口。
        //由FutureTask创建一个Thread对象：
        Thread callableThread = new Thread(oneTask);
        callableThread.start();
        try {
            /**
             * futureTask.get()方法可能会阻塞
             * 因为call()中可能会存在耗时的任务，所以在获取返回值的时候，就会等待（阻塞）。
             * 一般将get()方法放在最后一行，并用通过异步操作调用。
             */
            //接收返回值
            Object result = oneTask.get();
            System.out.println(String.valueOf(result));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
/**
 * （1） 继承Thread类创建线程类,重写该类的run()方法,调用该线程对象的start()方法启动线程
 */
class SomeThead extends Thread  {
    @Override
    public void run()   {
        System.out.println("Thread do something here");
    }
}

/**
 * （2）实现Runnable接口创建线程类
 * 通过实现Runnable接口创建线程类的具体步骤和具体代码如下：
 *    • 定义Runnable接口的实现类，并重写该接口的run()方法；
 *    • 创建Runnable实现类的实例，并以此实例作为Thread的target对象，即该Thread对象才是真正的线程对象。
 */
class SomeRunnable implements Runnable   {
    @Override
    public void run()   {
        System.out.println("Runnable do something here");
    }
}
/**
 *  （3）通过Callable和Future创建线程
 * 通过Callable和Future创建线程的具体步骤和具体代码如下：
 *    • 创建Callable接口的实现类，并实现call()方法，该call()方法将作为线程执行体，并且有返回值。
 *    • 创建Callable实现类的实例，使用FutureTask类来包装Callable对象，该FutureTask对象封装了该Callable对象的call()方法的返回值。
 *    • 使用FutureTask对象作为Thread对象的target创建并启动新线程。
 *    • 调用FutureTask对象的get()方法来获得子线程执行结束后的返回值其中，Callable接口(也只有一个方法)定义如下：
 */
class SomeCallable implements Callable<Object> {
    @Override
    public Object call() throws Exception {
        System.out.println("Callable do something here");
        return "Callable do something finished";
    }
}
