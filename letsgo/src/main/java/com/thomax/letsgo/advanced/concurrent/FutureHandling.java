package com.thomax.letsgo.advanced.concurrent;

import org.apache.commons.lang3.concurrent.Computable;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

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
 * 利用异步构建高效可伸缩性缓存
 */
class Memoizer <I, O> implements Computable<I, O> {
    private final ConcurrentMap<I, Future<O>> cache = new ConcurrentHashMap<>();
    private final Computable<I, O> c;

    public Memoizer(Computable<I, O> c) {
        this.c = c;
    }

    public O compute(final I in) throws InterruptedException {
        while (true) {
            Future<O> future = cache.get(in);
            if (future == null) {
                FutureTask<O> futureTask = new FutureTask<>(() -> c.compute(in));
                future = cache.putIfAbsent(in, futureTask); //利用原子操作：缺少则插入
                if (future == null) {
                    future = futureTask;
                    futureTask.run(); //再次调用() -> c.compute(arg)
                }
            }

            try {
                return future.get();
            } catch (CancellationException e) {
                cache.remove(in, future);
            } catch (ExecutionException e) {
                throw LaunderThrowable.handle(e.getCause());
            }
        }
    }
}