package com.thomax.letsgo.advanced.thread;

import sun.nio.ch.Interruptible;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.AccessControlContext;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Java线程详细方法
 */
public class ThreadHandling extends Thread {
    /**
     * 属性描述：
     * 其中ThreadLocal.ThreadLocalMap属于同包下使用，这里案例无法识别到
     */
    private volatile String name;
    private int            priority;
    private Thread         threadQ;
    private long           eetop;
    private boolean     single_step;
    private boolean     daemon = false;
    private boolean     stillborn = false;
    private Runnable target;
    private ThreadGroup group;
    private ClassLoader contextClassLoader;
    private AccessControlContext inheritedAccessControlContext;
    private static int threadInitNumber;
    //ThreadLocal.ThreadLocalMap threadLocals = null;
    //ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;
    private long stackSize;
    private long nativeParkEventPointer;
    private long tid;
    private static long threadSeqNumber;
    private volatile int threadStatus = 0;
    volatile Object parkBlocker;
    private volatile Interruptible blocker;
    private final Object blockerLock = new Object();
    public final static int MIN_PRIORITY = 1;
    public final static int NORM_PRIORITY = 5;
    public final static int MAX_PRIORITY = 10;
    /**
     * 方法描述 for Example
     */
    public synchronized void startExample() { super.start(); } //启动线程
    public void runExample() { super.run(); } //定义线程的工作内容
    public static void sleepExample() throws InterruptedException { Thread.sleep(1);} //线程进入阻塞状态，休眠1秒（不会释放持有的锁）
    public static void sleepExample2() throws InterruptedException { Thread.sleep(0, 2000);} //休眠时间2000纳秒
    public static void yieldExample() { Thread.yield(); } //暂停线程，将该线程转入到就绪状态，让CPU重新调度
    public void joinExample() throws InterruptedException { super.join(); } //
    public void joinExample2() throws InterruptedException { super.join(1); } //
}

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
        this.initExceptionHandler(); //线程启动时进行初始化
        /*business logic*/
    }
    private void initExceptionHandler() {
        this.setUncaughtExceptionHandler((t, e) -> {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "Thread terminated with exception: " + t.getName(), e);
        });
    }
}
