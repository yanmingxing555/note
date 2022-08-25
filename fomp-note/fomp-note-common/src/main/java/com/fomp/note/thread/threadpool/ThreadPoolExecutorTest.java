package com.fomp.note.thread.threadpool;

import java.util.List;
import java.util.concurrent.*;

/**
 * 默认线程池：ThreadPoolExecutor
 * 优点：
 *      1、降低因频繁切换线程而造成的cpu资源消耗
 *      2、提高响应速度
 *      3、对线程统一管理
 */
public class ThreadPoolExecutorTest {
    /**
     * public ThreadPoolExecutor(    int corePoolSize,
     *                               int maximumPoolSize,
     *                               long keepAliveTime,
     *                               TimeUnit unit,
     *                               BlockingQueue<Runnable> workQueue,
     *                               ThreadFactory threadFactory,
     *                               RejectedExecutionHandler handler)
     */
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            /**
             * corePoolSize：线程池中的核心线程数，当提交一个任务时，线程池创建一个新线程执行任务，直到当前线程数等于corePoolSize；
             * 如果当前线程数为corePoolSize，继续提交的任务被保存到阻塞队列中，等待被执行；
             * 如果执行了线程池的prestartAllCoreThreads()方法，线程池会提前创建并启动所有核心线程。
             * 解释：
             *     ①如果workerCount （线程池中的线程数）< corePoolSize，则创建并启动线程来执行新提交的任务；
             *     ②如果workerCount >= corePoolSize && workerCount <maximumPoolSize
             *          线程池内的阻塞队列未满，则将任务添加到该阻塞队列中，不再创建线程；
             *          线程池内的阻塞队列已满，则创建并启动线程来执行新提交的任务
             */
            10,
            /**
             * maximumPoolSize：线程池中允许的最大线程数。如果当前阻塞队列满了，且继续提交任务，则创建新的线程执行任务，前提是当前线程数小于maximumPoolSize；
             * 解释：
             *      如果workerCount >= maximumPoolSize，并且线程池内的阻塞队列已满,
             *      不在接收任务，执行拒绝策略, 默认的处理方式是直接抛异常。
             */
            10,
            /**
             * keepAliveTime：线程池维护线程所允许的空闲时间。当线程池中的线程数量大于corePoolSize的时候，
             * 如果这时没有新的任务提交，核心线程外的线程不会立即销毁，而是会等待，直到等待 的时间超过了keepAliveTime；
             */
            0l,
            /**
             * unit：keepAliveTime的单位；
             */
            TimeUnit.MILLISECONDS,
            /**
             * workQueue 用来保存等待被执行的任务的阻塞队列，且任务必须实现Runnable接口，
             *    在JDK中提供了如下阻塞队列：
             *      1、ArrayBlockingQueue：基于数组结构的有界阻塞队列，按FIFO排序任务；
             *      2、LinkedBlockingQueue：基于链表结构的阻塞队列，按FIFO排序任务，吞吐量通常要高于ArrayBlockingQueue；
             *      3、SynchronousQueue：一个不存储元素的阻塞队列，每个插入操作必须等到另一个线程调用移除操作，
             *                          否则插入操作一直处于阻塞状态，吞吐量通常要高于 LinkedBlockingQueue；
             *      4、priorityBlockingQueue：具有优先级的无界阻塞队列；
             *          ArrayBlockingQueue和LinkedBlockingQueue都是按照先进先出算法来处理任务。
             *          而PriorityBlockingQueue可根据任务自身的优先级顺序先后执行（总是确保高优先级的任务先执行）。
             *      5.DelayQueue：
             *            无界阻塞队列：由优先级堆支持的、基于时间的调度队列，内部基于无界队列PriorityQueue实现，而无界队列基于数组的扩容实现。
             *         要求：入队的对象必须要实现Delayed接口,而Delayed集成自Comparable接口
             */
            new LinkedBlockingQueue<Runnable>(),
            /**
             * threadFactory：线程工厂，它是ThreadFactory类型的变量，用来创建新线程，定义线程名字。默认使用 Executors.defaultThreadFactory()来创建线程。
             * 使用默认的ThreadFactory来创建线程时，会使新创建的线程具有相同的NORM_PRIORITY优先级并且是非守护线程，同时也设置了线程的名称。
             */
            Executors.defaultThreadFactory(),
            /**
             * handler：线程池的饱和策略/拒绝策略，当阻塞队列满了，且没有空闲的工作线程，如果继续提交任务，必须采取一种策略处理该任务，
             * 线程池提供了4种策略： 1、AbortPolicy：直接抛出异常，默认策略；
             *                   2、CallerRunsPolicy：用调用者所在的线程来执行任务；
             *                   3、DiscardOldestPolicy：丢弃阻塞队列中靠最前的任务，并执行当前任务；
             *                   4、DiscardPolicy：直接丢弃任务；
             * 上面的4种策略都是ThreadPoolExecutor的内部类。
             */
            new ThreadPoolExecutor.AbortPolicy());
    /**
     * ExecutorService的行为方法：
     * 1，execute（Runnable command）：履行Runnable类型的任务,
     * 2，submit（task）：可用来提交Callable或Runnable任务，并返回代表此任务的Future 对象
     * 3，shutdown（）：在完成已提交的任务后封闭办事，不再接管新任务,
     * 4，shutdownNow（）：停止所有正在履行的任务并封闭办事。
     * 5，isTerminated（）：测试是否所有任务都履行完毕了。
     * 6，isShutdown（）：测试是否该ExecutorService已被关闭。
     */

    /**
     * FixedThreadPool：适用于为了满足资源管理的需求，而需要限制当前线程数量的应用场景，它适用于负载比较重的服务器。
     */
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    /**
     * SingleThreadExecutor：适用于需要保证顺序地执行各个任务；并且在任意时间点，不会有多个线程是活动的应用场景。
     */
    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    /**
     * CachedThreadPool：是大小无界的线程池，适用于执行很多的短期异步任务的小程序，或者是负载较轻的服务器。
     */
    private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    /**
     * ScheduledThreadPoolExecutor：适用于需要多个后台线程执行周期任务，同时为了满足资源管理的需求而需要限制后台线程的数量的应用场景。
     */
    private static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    /**
     * 一、Callable创建
     */
    private static void creatCallable() throws Exception{
        //1.new Callable
        Callable<String> callable1 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "callable1 new Callable 创建";
            }
        };
        //2.Executors创建无返回值
        Callable callable2 =Executors.callable(new Runnable() {
            @Override
            public void run() {
                System.out.println("callable2 Executors创建无返回值");
            }
        });
        //3.Executors创建并有返回值
        User user = new User("张三",18);
        Callable<User> callable3 = Executors.callable(new Runnable() {
            @Override
            public void run() {
                System.out.println("callable3 Executors创建并有返回值");
                user.setAge(28);
            }
        },user);
        Future<User> task = fixedThreadPool.submit(callable3);
        System.out.println(task.get().toString());
        fixedThreadPool.shutdown();
    }
    /**
     * 二、线程池的使用
     */
    private static void runTreadPool() throws Exception{
        /**
         * 线程池的执行
         * <T> Future<T> submit(Callable<T> task)
         * <T> Future<T> submit(Runnable task, T result)
         * Future<> submit(Runnable task)
         */
        /**
         * 线程池执行
         */
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable run do something");
            }
        });
        //fixedThreadPool.shutdown();
        Future<String> result = fixedThreadPool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Callable call do something";
            }
        });
        System.out.println(result.get());
        fixedThreadPool.shutdown();
    }
    /**
     * 定时线程池的使用
     */
    private static void scheduledThreadPoolExe(){
        /**
         *     //这个方法的意思是在指定延迟之后运行task。这个方法有个问题，就是没有办法获知task的执行结果。
         *     //如果我们想获得task的执行结果，我们可以传入一个Callable的实例
         *  1. schedule (Runnable task, long delay, TimeUnit timeunit)
         *    //也是在指定延迟之后运行task，不过它接收的是一个Callable实例，此方法会返回一个ScheduleFuture对象，
         *    //通过ScheduleFuture我们可以取消一个未执行的task，也可以获得这个task的执行结果。
         *  2. schedule (Callable task, long delay, TimeUnit timeunit)
         *    //这个方法的作用是周期性的调度task执行。task第一次执行的延迟根据initialDelay参数确定，以后每一次执行都间隔period时长。
         *    //如果task的执行时间大于定义的period，那么下一个线程将在当前线程完成之后再执行。整个调度保证不会出现一个以上任务同时执行
         *    延迟initialDelay执行，开始时间间隔下次开始时间，等上个结束再开始下一个任务
         *  3. scheduleAtFixedRate (Runnable, long initialDelay, long period, TimeUnit timeunit)
         *    //scheduleWithFixedDelay的参数和scheduleAtFixedRate参数完全一致，它们的不同之处在于对period调度周期的解释。
         *    //在scheduleAtFixedRate中，period指的两个任务开始执行的时间间隔，也就是当前任务的开始执行时间和下个任务的开始执行时间之间的间隔。
         *    //而在scheduleWithFixedDelay中，period指的当前任务的结束执行时间到下个任务的开始执行时间。
         *    延迟initialDelay执行，结束时间间隔下次开始时间
         *  4. scheduleWithFixedDelay (Runnable, long initialDelay, long period, TimeUnit timeunit)
         */
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "callable 任务执行完成";
            }
        };
        //添加任务,延迟1s后执行任务
        scheduledThreadPool.schedule(callable, 1, TimeUnit.SECONDS);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("runnable 任务执行完成");
            }
        };
        //延迟10s后、每隔1000s执行任务
        scheduledThreadPool.scheduleAtFixedRate(runnable,10,1000,TimeUnit.SECONDS);
        scheduledThreadPool.shutdown();
    }
    /**
     * 三、线程池的关闭
     * 注意：在代码中声明临时线程池一定要shutdown,如果是结合spring定义的全局公用的线程池，还是不要随便shutdown
     */
    private static void shutDownTreadPool(){
        /**
         * shutdown()，它可以安全地关闭一个线程池，调用 shutdown() 方法之后线程池并不是立刻就被关闭，
         * 因为这时线程池中可能还有很多任务正在被执行，或是任务队列中有大量正在等待被执行的任务，
         * 调用 shutdown() 方法后线程池会在执行完正在执行的任务和队列中等待的任务后才彻底关闭。
         * 调用 shutdown() 方法后如果还有新的任务被提交，线程池则会根据拒绝策略直接拒绝后续新提交的任务。
         */
        fixedThreadPool.shutdown();
        /**
         * 表示立刻关闭的意思，不推荐使用这一种方式关闭线程池。
         *
         * 在执行 shutdownNow 方法之后，首先会给所有线程池中的线程发送 interrupt 中断信号，尝试中断这些任务的执行，
         * 然后会将任务队列中正在等待的所有任务转移到一个 List 中并返回，我们可以根据返回的任务 List 来进行一些补救的操作，例如记录在案并在后期重试。
         * shutdown 没有返回值，而 shutdownNow 会返回关闭前任务队列中未执行的任务集合（List）
         */
        List<Runnable> list = fixedThreadPool.shutdownNow();
        /**
         * 它可以返回 true 或者 false 来判断线程池是否已经开始了关闭工作，也就是是否执行了 shutdown 或者 shutdownNow 方法。
         * 注意：如果调用 isShutdown() 方法的返回的结果为 true 并不代表线程池此时已经彻底关闭了，这仅仅代表线程池开始了关闭的流程，
         * 也就是说，此时可能线程池中依然有线程在执行任务，队列里也可能有等待被执行的任务
         */
        boolean isShutdown = fixedThreadPool.isShutdown();
        /**
         * 可以检测线程池是否真正“终结”了，这不仅代表线程池已关闭，同时代表线程池中的所有任务都已经都执行完毕了。
         *
         * 比如我们上面提到的情况，如果此时已经调用了 shutdown 方法，但是还有任务没有执行完，那么此时调用 isShutdown 方法返回的是 true，而 isTerminated 方法则会返回 false。
         * 直到所有任务都执行完毕了，调用 isTerminated() 方法才会返回 true，这表示线程池已关闭并且线程池内部是空的，所有剩余的任务都执行完毕了。
         */
        boolean isTerminated = fixedThreadPool.isTerminated();
        /**
         * 主要用来判断线程池状态的。
         * 比如我们给 awaitTermination 方法传入的参数是 10 秒，那么它就会陷入 10 秒钟的等待，直到发生以下三种情况之一：
         *
         *     等待期间（包括进入等待状态之前）线程池已关闭并且所有已提交的任务（包括正在执行的和队列中等待的）都执行完毕，相当于线程池已经“终结”了，方法便会返回 true
         *     等待超时时间到后，第一种线程池“终结”的情况始终未发生，方法返回 false
         *     等待期间线程被中断，方法会抛出 InterruptedException 异常
         *
         * 调用 awaitTermination 方法后当前线程会尝试等待一段指定的时间，如果在等待时间内，线程池已关闭并且内部的任务都执行完毕了，
         * 也就是说线程池真正“终结”了，那么方法就返回 true，否则超时返回 false。
         */
        try {
            boolean awaitTermination = fixedThreadPool.awaitTermination(10,TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            //creatCallable();
            //runTreadPool();
            //shutDownTreadPool();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * ThreadPoolExecutor一些方法
     */
    private static void threadPoolSomeMethod(){
        /**
         * 线程池监控
         *      public long getTaskCount() //线程池已执行与未执行的任务总数
         *      public long getCompletedTaskCount() //已完成的任务数
         *      public int getPoolSize()//线程池当前的线程数
         *      public int getActiveCount() //线程池中正在执行任务的线程数量
         */
        threadPoolExecutor.getTaskCount();
        threadPoolExecutor.getCompletedTaskCount();
        threadPoolExecutor.getPoolSize();
        threadPoolExecutor.getActiveCount();
    }
}
class User{
    private String name;
    public volatile int age;
    public User() {
    }
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}