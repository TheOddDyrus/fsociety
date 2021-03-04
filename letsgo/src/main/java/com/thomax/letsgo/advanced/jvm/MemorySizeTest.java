package com.thomax.letsgo.advanced.jvm;

import com.carrotsearch.sizeof.RamUsageEstimator;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 对象占用内存测试（单位：byte）
 *
 * Object object = new Object(): object占八个字节，object的引用占四个字节 ==> 一个空对象占用：8byte + 4byte = 12byte （最后java会补全slot，一个Object=12+4=16个字节）
 */
public class MemorySizeTest {

    public static void main(String[] args) throws ExecutionException {
        Object o = new Object();
        System.out.println("all byte:" + RamUsageEstimator.sizeOf(o)); //计算指定对象及其引用树上的所有对象的综合大小，单位字节
        System.out.println("all memory:" + RamUsageEstimator.humanSizeOf(o)); //计算指定对象及其引用树上的所有对象的综合大小，并返回可读的结果
        System.out.println("root byte:" + RamUsageEstimator.shallowSizeOf(o)); //计算指定对象本身在堆空间的大小，单位字节
        System.out.println("--------------------");

        AA aa = new AA();
        System.out.println("all byte:" + RamUsageEstimator.sizeOf(aa));
        System.out.println("all memory:" + RamUsageEstimator.humanSizeOf(aa));
        System.out.println("root byte:" + RamUsageEstimator.shallowSizeOf(aa));
        System.out.println("--------------------");

        BB bb = new BB();
        System.out.println("all byte:" + RamUsageEstimator.sizeOf(bb));
        System.out.println("all memory:" + RamUsageEstimator.humanSizeOf(bb));
        System.out.println("root byte:" + RamUsageEstimator.shallowSizeOf(bb));
    }

    private static class AA {
        private int num = 123456;
        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

    private static class BB {
        private long num = 123456L;
        public long getNum() {
            return num;
        }

        public void setNum(long num) {
            this.num = num;
        }
    }

}
