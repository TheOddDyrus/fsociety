package com.thomax.letsgo.advanced.thread;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadHandling { }

/**
 * 中断案例1：Java并没有提供某种抢占式的机制来取消或终结线程，它提供了一种协作的方式来中断
 */
class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> blockingQueue;
    public PrimeProducer(BlockingQueue<BigInteger> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            BigInteger bigInteger = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted()) { //当线程中断标志为false的时候，线程可以继续执行；而isInterrupted(true)则会返回中断标志并清除中断标志
                blockingQueue.put(bigInteger = bigInteger.nextProbablePrime());
            }
        } catch (InterruptedException e) {
            //屏蔽异常，线程正常执行完毕
        }
    }
}

/**
 * 中断案例2：日志打印服务
 */
class LogServer {
    private final BlockingQueue<String> blockingQueue;
    private final LoggerThread loggerThread = new LoggerThread();
    private final PrintWriter printWriter;
    private boolean isShutdown; //打印服务取消的标志1
    private int reservations; //打印服务取消的标志2
    public LogServer(BlockingQueue<String> blockingQueue, PrintWriter printWriter) {
        this.blockingQueue = blockingQueue; //队列实现打印方式
        this.printWriter = printWriter;
    }

    public void start() {
        /*当想由系统托管关闭的时候，通过注册一个关闭钩子来由JVM结束时关闭停止日志服务（只关闭通过钩子注册但未开始的线程，关闭时不能保证钩子的调用顺序，且关闭钩子线程与正在执行的线程并发进行。
        当所有钩子执行结束时，如果Runtime.runFinalizersOnExit()为true则JVM运行终结器（JVM不会停止或中断还在执行的线程），当JVM终结时除了守护线程，其他线程强行结束。*/
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        loggerThread.start();
    }
    public void stop() {
        synchronized (this) {
            isShutdown = true;
        }
        loggerThread.interrupt();
    }
    public void log(String msg) throws InterruptedException {
        synchronized (this) {
            if (isShutdown) {
                throw new IllegalStateException(); //停止服务以后，拒绝新的打印任务，并抛出异常
            }
            reservations++;
        }
        blockingQueue.put(msg);
    }

    private class LoggerThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        synchronized (LogServer.this) {
                            if (isShutdown && reservations == 0) {
                                break; //stop()且等待剩余打印任务结束以后，跳出
                            }
                        }
                        String msg = blockingQueue.take();
                        synchronized (LogServer.this) {
                            reservations--;
                        }
                        printWriter.println(msg);
                    } catch (InterruptedException e) {
                        /*retry logic*/
                    }
                }
            } finally {
                printWriter.close();
            }
        }
    }
}

/**
 * 利用Thread内的UncaughtExceptionHandler实现将未捕获异常打印到日志
 */
class ThreadExceptionHandler extends Thread {
    @Override
    public void run() {
        this.initExceptionHandler();
        /*business logic*/
    }
    private void initExceptionHandler() {
        this.setUncaughtExceptionHandler((t, e) -> {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "Thread terminated with exception: " + t.getName(), e);
        });
    }
}
