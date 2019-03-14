package com.thomax.letsgo.advanced.thread;

import com.thomax.letsgo.advanced.concurrent.LaunderThrowable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Executor执行的任务的生命周期有4种：创建、提交、开始、完成
 */
public class ExecutorHandling {
    public Executor getExecutor1(Runnable command1) {
        return (Runnable command) -> {
            new Thread(command1).start(); //由CPU调度
        };
    }
    public Executor getExecutor2(Runnable command2) {
        return (Runnable command) -> {
            command2.run(); //当做普通对象直接执行方法直接运行方法
        };
    }
}

class ExecutorServerHandling implements ExecutorService {
    /**
     * ExecutorService的三种运行状态：运行、关闭、终止
     */
    public void shutdown() { }  //平缓地关闭过程：队列中不再接受新的任务，等待队列中还未执行完成的任务，包括队列中还未开始执行的任务
    public List<Runnable> shutdownNow() { return null; } //粗暴的关闭过程：尝试取消队列中所有运行中的任务，并且不再启动队列中尚未开始执行的任务
    public boolean isShutdown() { return false; }
    public boolean isTerminated() { return false; }
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException { return false; } //调用后会调用shutdown()，产生同步关闭的效果

    public <T> Future<T> submit(Callable<T> task) { return null; }
    public <T> Future<T> submit(Runnable task, T result) { return null; }
    public Future<?> submit(Runnable task) { return null; }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException { return null; }
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException { return null; }
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException { return null; }
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException { return null; }

    public void execute(Runnable command) { }

    /**
     * 获得线程池的几种方式
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

class Rebderer<E> {
    private ExecutorService executorService;

    public Rebderer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void a(List<E> list) {
        CompletionService<E> completionService = new ExecutorCompletionService<>(executorService);
        for (E e : list) {
            completionService.submit(() -> in(e));
        }

        try {
            for (int i = 0; i < list.size(); i++) {
                Future<E> future = completionService.take();
                E e = future.get();
                this.out(e);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); //中断当前线程，不影响当前线程后面的执行
        } catch (ExecutionException e) {
            throw LaunderThrowable.handle(e);
        }
    }

    private E in(E e) {
        //操作e，形似下载
        return e;
    }
    private E out(E e) {
        //返回e，形似渲染
        return e;
    }
}













