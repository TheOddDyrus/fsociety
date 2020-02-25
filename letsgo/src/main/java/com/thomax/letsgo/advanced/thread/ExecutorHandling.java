package com.thomax.letsgo.advanced.thread;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
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
    public void shutdown() { }  //平缓地关闭过程（非阻塞）：队列中不再接受新的任务，等待队列中还未执行完成的任务，包括队列中还未开始执行的任务
    public List<Runnable> shutdownNow() { return null; } //粗暴的关闭过程：尝试取消队列中所有运行中的任务，并且不再启动队列中尚未开始执行的任务
    public boolean isShutdown() { return false; }
    public boolean isTerminated() { return false; }
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException { return false; } /*优雅的关闭过程：使当前线程阻塞，并且期间任务可以继续提交，
                                                                                        会调用shutdown()，在超时时间内所有已提交的任务已执行完返回值是true，否则是false */
    /**
     * 提交任务
     */
    public <T> Future<T> submit(Callable<T> task) { return null; }
    public <T> Future<T> submit(Runnable task, T result) { return null; }
    public Future<?> submit(Runnable task) { return null; }
    /**
     * 执行顺序无关性任务：
     * invokeAll()执行所有，返回所有的执行结果；invokeAny()只会返回第一个执行成功的任务的结果，并终止其他未完成的任务（类似多个线程搜索多个盘内的文件，第一个获取到即可）
     */
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException { return null; }
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException { return null; }
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException { return null; }
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException { return null; }

    public void execute(Runnable command) { }
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
            //异步任务执行过程中自己抛出的异常会被封装在ExecutionException中
        } catch (TimeoutException e) {
            this.failover(img); //超时后进行任务降级
            future.cancel(true); //取消超时任务
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












