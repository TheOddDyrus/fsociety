package com.thomax.letsgo.advanced.thread;

import sun.nio.ch.Interruptible;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.AccessControlContext;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Java线程详述
 */
public class ThreadHandling extends Thread {
    /**
     * 属性描述：
     * 其中ThreadLocal.ThreadLocalMap属于同包下使用，这里案例无法识别到
     */
    private volatile String name; //线程名
    private int            priority; //线程优先级（1~10）,越小越容易被先调度执行
    private Thread         threadQ; //保留属性，暂时没被使用
    private long           eetop; //保留属性，暂时没被使用
    private boolean     single_step; //保留属性，暂时没被使用
    private boolean     daemon = false; //是否是守护线程
    private boolean     stillborn = false; //保留属性，JVM的状态
    private Runnable target; //初始化的任务
    private ThreadGroup group; //线程所属的分组
    private ClassLoader contextClassLoader; //此线程的上下文类加载器
    private AccessControlContext inheritedAccessControlContext; //此线程的访问控制上下文
    private static int threadInitNumber; //匿名线程的编号id
    //ThreadLocal.ThreadLocalMap threadLocals = null; //线程持有的ThreadLocal<>的Map
    //ThreadLocal.ThreadLocalMap inheritableThreadLocals = null; //可以被继承的ThreadLocal<>的Map
    private long stackSize; //初始化线程以后，分配的栈内存大小
    private long nativeParkEventPointer; //本机线程终止后还会保存到JVM的私有状态
    private long tid; //线程id
    private static long threadSeqNumber; //用于递增共享的线程id
    private volatile int threadStatus = 0; //线程的生命周期的6种状态之一，初始值0为新建状态
    volatile Object parkBlocker; //此属性可以提供给java.util.concurrent.locks.LockSupport进行操作(set and get)
    private volatile Interruptible blocker; //设置阻止字段，在可中断的I/O操作中阻塞；通过java.nio代码中的sun.misc.SharedSecrets调用
    private final Object blockerLock = new Object(); //内置对象锁
    public final static int MIN_PRIORITY = 1; //线程优先级最小值，最容易被CPU先调度执行
    public final static int NORM_PRIORITY = 5; //线程优先级默认值
    public final static int MAX_PRIORITY = 10; //线程优先级最大值
    /**
     * 方法描述 for Example
     */
    public synchronized void start() { super.start(); } //启动线程
    public void run() { super.run(); } //运行线程的工作内容
    public static Thread getCurrentThread() { return currentThread(); } //获得当前正在执行的线程
    public static void sleep1() throws InterruptedException { sleep(1);} //线程进入阻塞状态，休眠1秒（不会释放持有的锁）
    public static void sleep2() throws InterruptedException { sleep(0, 2000);} //休眠时间2000纳秒
    public static void yield() { yield(); } //暂停线程，将该线程转入到就绪状态，让CPU重新调度
    public void wait1() throws InterruptedException { wait(); } //wait(0)，直接让线程进入等待状态
    public void wait2() throws InterruptedException { wait(1); } //持续1秒为等待状态，超过1秒则自己唤醒
    public void wait3() throws InterruptedException { wait(0, 2000); } //持续2000纳秒为等待状态，超过1秒则自己唤醒
    public void join1() throws InterruptedException { join(); } //运行线程中调用此线程的join()以后会阻塞，等待此线程执行完，再执行运行线程的代码
    public void join2() throws InterruptedException { join(1); } //wait()1毫秒以后，再继续join()
    public void join3() throws InterruptedException { join(0, 2000); } //wait()2000纳秒以后，再继续join()
    public void notify1() { notify(); } //唤醒一个等待中的线程并使该线程开始执行，由CPU调度，随机一个
    public void notifyAll1() { notifyAll(); } //唤醒所有等待中的线程，由CPU调度
    /*线程的中断标志在Java源码里面是看不到的，由计算机系统实现*/
    public void interrupt() { interrupt(); } //使线程中断，并且不会中断一个正在运行的线程；如果此线程是阻塞状态，则会抛出一个异常（可以中断阻塞状态）
    public boolean isInterrupted() { return isInterrupted(); } //调用Thread类中的native方法isInterrupted(false)，返回线程的中断状态
    public static boolean interruptedExample() { return interrupted(); } //调用Thread类中的native方法isInterrupted(true)，返回线程的中断状态并且清空线程的中断状态
    //public void stop() { stop(); } //此方法会直接终止run()方法的调用，并且会抛出一个ThreadDeath错误，如果线程持有某个对象锁的话，会完全释放锁，导致对象状态不一致（已废弃）
    //public void destory() { destroy(); } //已废弃，直接会出异常
    public long getId() { return super.getId(); } //获得线程id
    public void modifyName() {
        super.setName("new name:" + super.getName()); //get set name
    }
    public void modifyPriority() {
        super.setPriority(1 + super.getPriority()); //get set 线程优先级（1～10，默认为5，值越小优先级越大）
    }
    public void modifyDaemon() { //守护线程依赖于创建它的线程，而用户线程则不依赖。当创建守护线程的线程运行完毕以后守护线程也会随着消亡，而用户线程则不会，用户线程会一直运行直到其运行完毕
        boolean on = super.isDaemon();//判断是否是守护线程
        super.setDaemon(on); //设置此线程是否为守护线程
    }
    /**
     * 内部枚举：线程的生命周期
     */
    public enum State {
        NEW, //新建
        RUNNABLE, //就绪
        BLOCKED, //阻塞，并且正在等待监视器锁
        WAITING, //等待，可能被这些操作所触发：不带超时值的Object.wait、不带超时值的Thread.join
        TIMED_WAITING, //定时等待，超时以后自己唤醒；可能被这些操作所触发：Thread.sleep、带有超时值的Object.wait、带有超时值的Thread.join、LockSupport.parkNanos、LockSupport.parkUntil
        TERMINATED //终结
    }
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
