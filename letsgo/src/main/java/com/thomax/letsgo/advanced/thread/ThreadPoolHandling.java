package com.thomax.letsgo.advanced.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程池详述
 */
public class ThreadPoolHandling extends ThreadPoolExecutor {
    /**
     * 从构造方法来看一个线程池的结构
     */
    public ThreadPoolHandling(int corePoolSize, //核心线程数
                              int maximumPoolSize, //最大线程数
                              long keepAliveTime, //当池内线程数高于corePoolSize时，经过多少时间多余的空闲线程才会被回收。回收前处于wait状态
                              TimeUnit unit, //时间的单位
                              BlockingQueue<Runnable> workQueue, //线程池队列
                              ThreadFactory threadFactory, //线程工厂
                              RejectedExecutionHandler handler /*拒绝策略（Policy）：
                                                                   1.中止（Abort）：该策略是线程池的默认策略。如果队列满了丢掉这个任务并且抛出RejectedExecution异常
                                                                   2.调用者运行（CallerRuns）：如果添加任务到队列失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行
                                                                   3.抛弃（Discard）：如果队列满了，会直接丢掉这个任务并且不会有任何异常
                                                                   4.抛弃最旧的（Discard-Oldest）：如果队列满了，会将最早进入队列的任务删掉腾出空间，再尝试加入队列
                                                                */
                             ) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }
    /**
     * 获得线程池的4种方式，当创建完成以后仍然可以通过setter改变参数
     */
    public ExecutorService getExecutorService1() {
        return Executors.newFixedThreadPool(20); //固定容量的线程池，当其中某个线程发生未预期异常的时候，线程池会补充一个新的线程
    }
    public ExecutorService getExecutorService2() {
        return Executors.newCachedThreadPool(); //可缓存的线程池，不会限制容量，当处理需求数小于容量时会回收空闲线程，当处理需求数大于容量时会创建新的线程
    }
    public ExecutorService getExecutorService3() {
        return Executors.newSingleThreadExecutor(); //创建一个单线程的Executor，线程异常结束会重新创建新的线程来代替
    }
    public ExecutorService getExecutorService4() {
        return Executors.newScheduledThreadPool(20); //固定容量的线程池，以延迟或定时的方式来执行线程，类似Timer（但是Timer只会创建一个线程，会出现任务计划频率被任务执行时间覆盖的问题）
    }
    public ExecutorService getExecutorService5() { //JDK1.8中新增的，它不是ThreadPoolExecutor的扩展，它是新的线程池类ForkJoinPool的扩展，工作窃取算法由于能并行操作，所以适合使用在很耗时的任务中
        return Executors.newWorkStealingPool(20); //不传参默认使用Runtime.getRuntime().availableProcessors()获得操作系统线程数，传参适合IO密集型一般设置为操作系统线程数的2N~3N
    }
    /**
     * 一个可以记录创建了多少次线程的线程工厂
     */
    class RecordThreadFactory implements ThreadFactory {
        private AtomicLong aLong = new AtomicLong(0);
        private ThreadFactory threadFactory = Executors.defaultThreadFactory(); //创建默认的线程工厂

        @Override
        public Thread newThread(Runnable r) {
            aLong.incrementAndGet();
            return threadFactory.newThread(r);
        }

        public int getTotal() {
            return aLong.intValue();
        }
    }
    /**
     * 如果不想让使用者在对线程池进行配置，可以如下操作：
     */
    public void unConfigurable() {
        ExecutorService executorService1 = Executors.unconfigurableExecutorService(getExecutorService1());
        ExecutorService executorService2 = Executors.unconfigurableExecutorService(getExecutorService2());
        ExecutorService executorService3 = Executors.unconfigurableExecutorService(getExecutorService3());
        ExecutorService executorService4 = Executors.unconfigurableScheduledExecutorService((ScheduledExecutorService) getExecutorService4());
    }
    /**
     * 线程池的类似AOP操作
     */
    public void aopExecute() {
        super.beforeExecute(new Thread(), () -> System.out.println(123));
        super.afterExecute(() -> System.out.println(456), new RuntimeException("456"));
    }
}
