package com.thomax.letsgo.advanced.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池的操作
 */
public class ThreadPoolHandling {
    /**
     * 获得线程池的4种方式
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
}
