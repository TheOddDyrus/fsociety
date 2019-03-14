package com.thomax.letsgo.advanced.thread;

import com.thomax.letsgo.advanced.concurrent.LaunderThrowable;

import java.util.ArrayList;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * ExecutorService的3种运行状态：运行、关闭、终止
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

/**
 * 利用ExecutorCompletionService实现html渲染：
 * 基于LinkedBlockingQueue的FIFO操作，实现多个IO队列的最佳性能
 */
class DrawHtml {
    private ExecutorService executorService;

    public DrawHtml(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void draw(String url) {
        CompletionService<Img> completionService = new ExecutorCompletionService<>(executorService);
        Matcher matcher = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(url); //形似从html中解析img分组出来
        int count = matcher.groupCount();
        for (int i = 0; i < count; i++) {
            String imgUrl = matcher.group(i);
            completionService.submit(() -> this.download(imgUrl));
        }

        this.drawLine(url); //CPU密集型操作（先响应给用户大概的界面）

        Future<Img> future = null;
        Img img = null;
        try { //IO密集型操作（后进行比较慢的渲染）
            for (int i = 0; i < count; i++) {
                future = completionService.take();
                img = future.get(2, TimeUnit.SECONDS); //设置任务超时时间
                this.drawImg(img);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); //future还没有值时抛出此异常，中断当前线程（即执行这个draw()方法的线程，不影响此线程后面的继续运行）
        } catch (ExecutionException e) {
            throw LaunderThrowable.handle(e);
        } catch (TimeoutException e) {
            this.failover(img); //超时后进行任务降级
            future.cancel(true); //取消任务了
        }
    }

    private Img download(String imgUrl) {
        return new Img(); //形似从imgUrl路径下载图片
    }
    private void drawLine(String url) {
        //解析html先渲染线条与文字
    }
    private void drawImg(Img img) {
        //渲染图片
    }
    private void failover(Img img) {
        //降级操作，比如显示一个广告，没加载到就使用默认的
    }

    private class Img {}
}













