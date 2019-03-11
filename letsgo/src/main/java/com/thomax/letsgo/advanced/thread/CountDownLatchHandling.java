package com.thomax.letsgo.advanced.thread;

import java.util.concurrent.CountDownLatch;

/**
 *  CountDownLatch是一种灵活的闭锁实现
 */
public class CountDownLatchHandling {

    public long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        startGate.await(); //每个线程持有这个闭锁
                        try {
                            task.run();
                        } finally {
                            endGate.countDown(); //每个线程结束时让闭锁的计数减1
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            };
            t.start();
        }

        long start = System.nanoTime();
        startGate.countDown(); //放开nThreads个线程的阻塞
        endGate.await(); //直到nThreads个线程的finally执行完，闭锁的计数清0以后才会停止阻塞
        long end = System.nanoTime();
        return end - start;
    }

}
