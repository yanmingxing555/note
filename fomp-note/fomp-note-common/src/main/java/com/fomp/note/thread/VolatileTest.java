package com.fomp.note.thread;

import java.util.concurrent.TimeUnit;

/**
 * Volatile是Java虚拟机提供的轻量级的同步机制（三大特性）
 *     保证可见性
 *     不保证原子性
 *     禁止指令重排
 * JMM是Java内存模型：定义了一个线程对另外一个线程的可见性
 * JMM的三大特性：volatile只保证了两个，即可见性和有序性，不满足原子性
 *     可见性
 *     原子性
 *     有序性
 * 也就是Java Memory Model，简称JMM，本身是一种抽象的概念，实际上并不存在，
 * 它描述的是一组规则或规范，通过这组规范定义了程序中各个变量（包括实例字段，静态字段和构成数组对象的元素）的访问方式。
 * JMM关于同步的规定：
 *     线程解锁前，必须把共享变量的值刷新回主内存
 *     线程加锁前，必须读取主内存的最新值，到自己的工作内存
 *     加锁和解锁是同一把锁
 * JMM内存模型的可见性：
 *      指的是当主内存区域中的值被某个线程写入更改后，其它线程会马上知晓更改后的值，并重新得到更改后的值。
 * 缓存一致性协议：
 *      各个处理器访问缓存时都遵循的一些协议，保证内存模型的可见性
 * 总线嗅探：
 *      就是每个处理器通过嗅探在总线上传播的数据来检查自己缓存值是否过期了，当处理器发现自己的缓存行对应的内存地址被修改，
 *      就会将当前处理器的缓存行设置为无效状态，当处理器对这个数据进行修改操作的时候，会重新从内存中把数据读取到处理器缓存中。
 * 总线风暴：当程序中有大量的volatile修饰的变量和CAS锁时，可能引发总线风暴
 *      由于volatile的MESI缓存一致性协议，需要不断的从主内存嗅探和CAS循环，无效的交互会导致总线带宽达到峰值。
 *      解决办法：部分volatile和 cas使用synchronize
 * Volatile禁止指令重排：Volatile实现禁止指令重排优化，从而避免了多线程环境下程序出现乱序执行的现象。
 *    源代码 -> 编译器优化的重排 -> 指令并行的重排 -> 内存系统的重排 -> 最终执行指令
 *   计算机在执行程序时，为了提高性能，编译器和处理器常常会对指令重排，一般分为以下三种：
 *      1.单线程环境里面确保最终执行结果和代码顺序的结果一致；
 *      2.处理器在进行重排序时，必须要考虑指令之间的数据依赖性；
 *      3.多线程环境中线程交替执行，由于编译器优化重排的存在，两个线程中使用的变量能否保证一致性是无法确定的，结果无法预测。
 * 内存屏障（Memory Barrier）又称内存栅栏，是一个CPU指令，它的作用有两个：
 *     1.保证特定操作的顺序
 *     2.保证某些变量的内存可见性（利用该特性实现volatile的内存可见性）
 */
public class VolatileTest {
    public static void main(String[] args) {

    }
    //验证volatile的可见性
    public static void test1(){
        MyData myData=new MyData();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.add();
            System.out.println(Thread.currentThread().getName()+"\t update number value:"+myData.number);
        },"AAA").start();
        while (myData.number==0){

        }
        System.out.println(Thread.currentThread().getName()+"\t mission is over");
    }
    //Volatile不保证原子性
    public static void test2(){
        MyData02 myData=new MyData02();
        for(int i=0;i<20;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++){
                    myData.addPlus();
                }
            },String.valueOf(i)).start();
        }
        while (Thread.activeCount()>2){
            Thread.yield();
        }
        //输出结果没有20000，说明没有保证原子性
        /**
         * 解决方法：
         *         synchronized
         *         AtomicInteger
         */
        System.out.println(Thread.currentThread().getName()+"\t finally number value: "+myData.number);
    }
}

//假设是物理内存
class MyData{
    volatile int number=0;
    public void add(){
        this.number=60;
    }
}
class MyData02{
    volatile int number =0;
    public void addPlus(){
        number++;
    }
}
