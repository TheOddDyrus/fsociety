package com.thomax.letsgo.advanced.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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

}

class CookUtil {}