package com.thomax.letsgo.advanced.concurrent;

import org.apache.commons.lang3.concurrent.Computable;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * FutureTask中也是用AQS的同步状态来保存任务的状态，例如：正在运行、已完成、已取消
 */
public class FutureHandling {

    public CookUtil buy() throws InterruptedException, ExecutionException {
        //网购厨具
        Callable<CookUtil> onlineShopping = new Callable<CookUtil>() {
            @Override
            public CookUtil call() throws Exception {
                System.out.println("第一步：下单");
                System.out.println("第一步：等待送货");
                Thread.sleep(5000);  // 模拟送货时间
                System.out.println("第一步：快递送到");
                return new CookUtil();
            }
        };

        FutureTask<CookUtil> futureTask = new FutureTask<>(onlineShopping);
        new Thread(futureTask).start(); //开始网购厨具

        Thread.sleep(2000);  //模拟网购厨具时间
        if (!futureTask.isDone()) {
            System.out.println("厨具还没到，心情好就等着（心情不好就通过futureTask.isCancelled()取消订单）");
        }

        return futureTask.get();
    }

    private class CookUtil {}
}

/**
 * 利用异步构建高效可伸缩性缓存（适合计算量较大的执行任务）
 * FutureTask实现了Future接口，所以异步结果可以通过FutureTask获得
 */
class Memoizer<I, O> implements Computable<I, O> {
    private final ConcurrentMap<I, Future<O>> cache = new ConcurrentHashMap<>();
    private final Computable<I, O> c;

    public Memoizer(Computable<I, O> c) {
        this.c = c;
    }

    public O compute(final I in){
        while (true) {
            Future<O> future = cache.get(in);
            if (future == null) {
                FutureTask<O> futureTask = new FutureTask<>(() -> c.compute(in));
                future = cache.putIfAbsent(in, futureTask); //任务唯一性，利用原子操作：缺少则插入（插入成功返回null，插入失败返回已存在的value）
                if (future == null) {
                    future = futureTask;
                    futureTask.run(); //说明插入成功，然后开始执行任务
                }
            }

            try {
                return future.get();
            } catch (CancellationException e) {
                cache.remove(in, future); //当异步任务执行过程中被取消，则会抛出此异常，需要清除缓存中的任务（是非受检异常，原则上可以不处理，但是业务需要还是可以处理一下）
            } catch (ExecutionException e) {
                throw LaunderThrowable.handle(e.getCause()); //异步任务执行过程中自己抛出的异常会被封装在ExecutionException中
            } catch (InterruptedException e) {
                //任务执行还未出结果时会抛出此异常；在这里的逻辑不用处理，线程会再次进入while{}
            }
        }
    }
}