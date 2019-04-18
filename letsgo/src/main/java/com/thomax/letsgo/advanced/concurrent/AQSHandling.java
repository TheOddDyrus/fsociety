package com.thomax.letsgo.advanced.concurrent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class AQSHandling { }

/**
 *  CountDownLatch是一种灵活的闭锁实现，但是只能使用一次，基于AQS
 */
class CountDownLatchHandling {

    public long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
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
        long end = System.nanoTime();
        return end - start;
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
            // Compute the new value that goes in (x,y)
            return 0;
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
        int setNewValue(int x, int y, int value);
        void commitNewValues();
        boolean hasConverged();
        void waitForConvergence();
        Board getSubBoard(int numPartitions, int index);
    }
}









