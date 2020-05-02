package com.thomax.letsgo.advanced.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join框架是Java7内ExecutorService接口的一种具体实现，目的是为了帮助你更好地利用多处理器带来的好处。
 * 它是为那些能够被递归地拆解成子任务的工作类型量身设计的。
 * 其目的在于能够使用所有可用的运算能力来提升你的应用的性能。
 *
 * Fork/Join使用工作窃取(work-stealing)算法：
 * 为了减少窃取任务线程和被窃取任务线程之间的竞争，通常会使用双端队列，被窃取任务线程永远从双端队列的头部拿任务执行，而窃取任务的线程永远从双端队列的尾部拿任务执行
 */
public class ForkJoinHanding {

    public static void main(String[] args) {
        long startTime;
        FibonacciTask fibonacciTask = new FibonacciTask(createFibonacci(36));
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        startTime = System.nanoTime();
        Future<Long> future = forkJoinPool.submit(fibonacciTask);
        System.out.print("fork/join task took " + (System.nanoTime() - startTime) + "ns");
        try {
            System.out.println(" result:" + future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成指定长度斐波那契数列
     */
    private static List<Long> createFibonacci(int size) {
        List<Long> list = new ArrayList<>(size);
        long total = 0;
        long startTime = System.nanoTime();
        if (size == 1) {
            list.add(0L);
        } else if (size == 2) {
            list.add(0L);
            list.add(1L);
        } else if (size > 2) {
            list.add(0L);
            list.add(1L);
            for (int i = 0; i < size - 2; i++) {
                list.add(list.get(i) + list.get(i + 1));
            }
        }
        System.out.print("create fibonacci took " + (System.nanoTime() - startTime) + "ns" + "\n[ ");
        for (Long val : list) {
            System.out.print(val + " ");
            total += val;
        }
        System.out.println("] " + "result:" + total);

        return list;
    }
}

/**
 * ForkJoinTask有两个子类：
 * RecursiveAction：用于没有返回结果的任务
 * RecursiveTask ：用于有返回结果的任务
 */
class FibonacciTask extends RecursiveTask<Long> {
    private static final long serialVersionUID = 1L;
    private List<Long> mList;

    FibonacciTask(List<Long> list) {
        mList = list;
    }

    /**
     * 将当前任务分成需要条件设定的小任务，不管是继承RecursiveAction还是RecursiveTask，都要重写compute()
     * RecursiveTask.fork()方法会触发compute()
     * RecursiveTask.join()方法返回执行以后的结果集
     */
    @Override
    protected Long compute() {
        if (mList != null) {
            if (mList.size() < 10) {
                return cal(mList);
            } else {
                List<List<Long>> lists = averageAssign(mList);
                FibonacciTask fibonacciTask1 = new FibonacciTask(lists.get(0));
                FibonacciTask fibonacciTask2 = new FibonacciTask(lists.get(1));
                fibonacciTask1.fork();
                fibonacciTask2.fork();
                return fibonacciTask1.join() + fibonacciTask2.join();
            }
        }
        return null;
    }

    private long cal(List<Long> list) {
        long total = 0;
        for (Long val : list) {
            total += val;
        }
        return total;
    }

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     */
    private static <T> List<List<T>> averageAssign(List<T> source) {
        List<List<T>> result = new ArrayList<>();
        int remaider = source.size() % 2;  //(先计算出余数)
        int number = source.size() / 2;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < 2; i++) {
            List<T> value;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }
}
