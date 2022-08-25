package com.fomp.note.thread.AbstractQueuedSynchronizer_AQS;

public class AQS {
    /**
     * AbstractQueuedSynchronizer会把请求构建一个CLH队列，
     * 当一个线程执行完毕时会激活自己的后续节点，但正在执行的线程并不在队列中，
     * 而那些等待的线程全部处于阻塞状态，线程的阻塞是通过LockSupport.part（）完成
     */
}
