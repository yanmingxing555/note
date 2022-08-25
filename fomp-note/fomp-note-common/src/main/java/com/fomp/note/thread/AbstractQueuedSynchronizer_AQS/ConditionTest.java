package com.fomp.note.thread.AbstractQueuedSynchronizer_AQS;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition是对象监视器的替代品，拓展了监视器的语义。
 * 相同点：
 * 1.都有一组类似的方法：
 *  对象监视器:       Object.wait()、Object.wait(long timeout)、Object.notify()、Object.notifyAll()。
 *  Condition对象: Condition.await()、Condition.awaitNanos(long nanosTimeout)、Condition.signal()、Condition.signalAll()。
 * 2.都需要和锁进行关联：
 *  对象监视器: 需要进入synchronized语句块（进入对象监视器）才能调用对象监视器的方法。
 *  Condition对象:需要和一个Lock绑定。
 *
 * 不同点：Condition拓展的语义方法：
 *      awaitUninterruptibly()：等待时忽略中断
 *      awaitUntil(Date deadline) throws InterruptedException：等待到特定日期
 * 使用方法：
 *      对象监视器: 进入synchronized语句块（进入对象监视器）后调用Object.wait()。
 *      Condition对象: 需要和一个Lock绑定，并显示的调用lock()获取锁，然后调用 Condition.await()。
 * 等待队列数量：
 *      对象监视器: 1个。
 *      Condition对象: 多个。通过多次调用lock.newCondition()返回多个等待队列。
 *
 *
 */
public class ConditionTest {

    private ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) {
        /**
         * 总结：
         * 在Condition中，用await()替换wait()，用signal()替换notify()，用signalAll()替换notifyAll()，
         * 传统线程的通信方式，Condition都可以实现，这里注意，Condition是被绑定到Lock上的，
         * 要创建一个Lock的Condition必须用newCondition()方法。
         * Condition的强大之处在于，对于一个锁，我们可以为多个线程间建立不同的Condition。
         * 如果采用Object类中的wait(), notify(), notifyAll()实现的话，当写入数据之后需要唤醒读线程时，
         * 不可能通过notify()或notifyAll()明确的指定唤醒读线程，而只能通过notifyAll唤醒所有线程，
         * 但是notifyAll无法区分唤醒的线程是读线程，还是写线程。
         * 所以，通过Condition能够更加精细的控制多线程的休眠与唤醒。
         */
    }
    public void conditionTest(){
        /**
         * 获取一个Condition的实例
         */
        Condition condition = lock.newCondition();
        try {
            lock.lock();
            condition.await();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}

/**
 * Condition的lock方法来替代synchronized方法
 * Condition用来替代锁的监视的功能，比锁更加灵活
 * 一个Condition示例需要和一个lock进行绑定
 */
interface MyCondition extends Condition{
    /**************************等待方法*****************************/
    /**
     * 调用此方法的线程将加入等待队列，阻塞直到被通知或者线程发生中断
     */
    void await() throws InterruptedException;
    /**
     * 调用此方法的线程将加入等待队列，阻塞直到被通知(忽略中断)
     */
    void awaitUninterruptibly();
    /**
     * 调用此方法的线程将加入等待队列，阻塞直到被通知或者线程发生中断或者超出等待的时间
     */
    long awaitNanos(long nanosTimeout) throws InterruptedException;
    /**
     * 调用此方法的线程将加入等待队列，阻塞直到被通知或者线程发生中断或者超出等待的时间(需要时间单位)
     */
    boolean await(long time, TimeUnit unit) throws InterruptedException;
    /**
     * 调用此方法的线程将加入等待队列，阻塞直到被通知或者线程发生中断或者超出特定的日期时间
     */
    boolean awaitUntil(Date deadline) throws InterruptedException;

    /**************************通知方法*****************************/
    /**
     * 唤醒一个等待中的线程
     */
    void signal();
    /**
     * 唤醒所有等待中的线程
     */
    void signalAll();

}
