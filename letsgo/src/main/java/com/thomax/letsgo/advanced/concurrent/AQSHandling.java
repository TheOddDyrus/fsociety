package com.thomax.letsgo.advanced.concurrent;

import com.thomax.letsgo.advanced.concurrent.CellularAutomata.BoardImpl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * AQS方法详解：
 * 1.它定义两种资源共享方式：Exclusive（独占，只有一个线程能执行，如ReentrantLock）和Share（共享，多个线程可同时执行，如Semaphore、CountDownLatch）
 * 2.以ReentrantLock为例，state初始化为0，表示未锁定状态。A线程lock()时，会调用tryAcquire()独占该锁并将state+1。此后，其他线程再tryAcquire()时就会失败，
 *   直到A线程unlock()到state=0（即释放锁）为止，其它线程才有机会获取该锁。当然，释放锁之前，A线程自己是可以重复获取此锁的（state会累加），这就是可重入的概念。
 *   但要注意，获取多少次就要释放多么次，这样才能保证state是能回到零态的。
 * 3.再以CountDownLatch以例，任务分为N个子线程去执行，state也初始化为N（注意N要与线程个数一致）。这N个子线程是并行执行的，每个子线程执行完后countDown()一次，
 *   state会CAS减1。等到所有子线程都执行完后(即state=0)，会unpark()主调用线程，然后主调用线程就会从await()函数返回，继续后余动作。
 *
 * 一般来说，自定义同步器要么是独占方法，要么是共享方式，他们也只需实现tryAcquire-tryRelease、tryAcquireShared-tryReleaseShared中的一种即可。
 * 但AQS也支持自定义同步器同时实现独占和共享两种方式，如ReentrantReadWriteLock
 */
public class AQSHandling extends AbstractQueuedSynchronizer {

    /*AQS中获取操作和释放操作的标准形式：
    public final void acquire(int arg) {
        while (当前状态不允许获取操作) {
            if (需要阻塞) {
                如果当前线程不在队列中，将其插入队列
                阻塞当前队列
            } else {
                返回失败
            }
        }
        根据独占方式/共享方式来更新同步器的状态
        如果线程位于队列中，则将其移出队列
        返回成功
    }
    public final boolean release(int arg) {
        更新同步器的状态
        if (新的状态允许被某个被阻塞的线程获取成功) {
            解除队列中一个或多个线程的阻塞状态
        }
    }
    */

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchHandling countDownLatchHandling = new CountDownLatchHandling();
        countDownLatchHandling.timeTasks(10, System.out::println); //闭锁实现10个线程同时打印

        SemaphoreHandling<Integer> objectSemaphoreHandling = new SemaphoreHandling<>(10);
        for (int i = 0; i <= 10; i++) {
            objectSemaphoreHandling.add(i); //信号量实现遍历过程的第11个值的添加将阻塞

            if (i == 10) {
                objectSemaphoreHandling.remove(0);
                objectSemaphoreHandling.add(i); //第11个值添加成功
            }
        }

        CellularAutomata cellularAutomata = new CellularAutomata(new BoardImpl());
        cellularAutomata.start(); //栅栏实现反复计算
    }

}

/**
 *  CountDownLatch是一种灵活的闭锁实现，但是只能使用一次，基于AQS
 */
class CountDownLatchHandling {

    public void timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread(() -> {
                try {
                    startGate.await(); //每个线程持有这个闭锁
                    try {
                        task.run();
                    } finally {
                        endGate.countDown(); //每个线程结束时让闭锁的计数减1
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); //清除线程内置的中断状态，让线程继续执行
                }
            });
            t.start();
        }

        long start = System.nanoTime();
        startGate.countDown(); //放开nThreads个线程的阻塞
        endGate.await(); //直到nThreads个线程的finally执行完，闭锁的计数清0以后才会停止阻塞
        System.out.println("耗时：" + (System.nanoTime() - start) + "纳秒！");
    }
}

/**
 * 信号量Semaphore，可以用来控制访问某个特定资源的操作数量，或者对容器施加边界
 */
class SemaphoreHandling<T> {

    private final Set<T> set;

    private final Semaphore semaphore;

    public SemaphoreHandling(int bound) {
        set = Collections.synchronizedSet(new HashSet<>());
        semaphore = new Semaphore(bound); //最大许可数为bound个，当并发访问时没有获得许可的线程会阻塞
    }

    public boolean add(T o) throws InterruptedException {
        semaphore.acquire(1); //获得许可，信号量加1
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded) {
                semaphore.release(); //新增失败时，返回许可，信号量减1
            }
        }
    }

    public boolean remove(T o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved) {
            semaphore.release(); //删除成功时，返回许可，信号量减1
        }
        return wasRemoved;
    }
}

/**
 * 栅栏CyclicBarrier可以使一定数量的参与方反复地在栅栏位置汇集
 */
class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public CellularAutomata(Board board) {
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors(); //返回Java虚拟机的可用的处理器数量
        this.barrier = new CyclicBarrier(count, mainBoard::commitNewValues); //形参：(栅栏的最终信号量, 传入线程执行的内容)
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++) {
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
        }
    }

    private class Worker implements Runnable {
        private final Board board;

        public Worker(Board board) {
            this.board = board;
        }

        public void run() {
            while (!board.hasConverged()) {
                for (int x = 0; x < board.getMaxX(); x++) {
                    for (int y = 0; y < board.getMaxY(); y++) {
                        board.setNewValue(x, y, computeValue(x, y));
                    }
                }
                try {
                    barrier.await();  //栅栏信号量++(信号量起始值为0)，并阻塞当前线程（当栅栏的信号量等于count时停止所有阻塞，并将信号量清0）
                } catch (InterruptedException | BrokenBarrierException ex) {
                    return;
                }
            }
        }

        private int computeValue(int x, int y) {
            return x + y; // Compute the new value that goes in (x,y)
        }
    }

    public void start() {
        for (Worker worker : workers) {
            new Thread(worker).start();
        }
        mainBoard.waitForConvergence();
    }

    interface Board {
        int getMaxX();
        int getMaxY();
        void setNewValue(int x, int y, int value);
        void commitNewValues();
        boolean hasConverged();
        void waitForConvergence();
        Board getSubBoard(int numPartitions, int index);
    }

    public static class BoardImpl implements Board{
        @Override
        public int getMaxX() {
            return 0;
        }
        @Override
        public int getMaxY() {
            return 0;
        }
        @Override
        public void setNewValue(int x, int y, int value) {
        }
        @Override
        public void commitNewValues() {
        }
        @Override
        public boolean hasConverged() {
            return false;
        }
        @Override
        public void waitForConvergence() {
        }
        @Override
        public Board getSubBoard(int numPartitions, int index) {
            return null;
        }
    }

}









