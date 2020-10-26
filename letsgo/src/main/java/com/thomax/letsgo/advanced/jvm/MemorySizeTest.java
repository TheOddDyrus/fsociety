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
 * Object object = new Object(): object占八个字节，object的引用占四个字节 ==> 一个空对象占用：8byte + 4byte = 12byte
 */
public class MemorySizeTest {

    public static void main(String[] args) throws ExecutionException {
        LoadingCache<String, AtomicLong> cache = CacheBuilder.newBuilder()
                .maximumSize(50000)
                .build(new CacheLoader<String, AtomicLong>() {
                    @Override
                    public AtomicLong load(String key) {
                        return new AtomicLong(System.currentTimeMillis());
                    }
                });

        for (int i = 0; i < 50400; i++) {
            String x = "ABCDEFGHIJK" + i;
            x = x.substring(x.length() - 12);
            cache.get(x);
        }

        System.out.println("size:" + cache.size());
        System.out.println("all byte:" + RamUsageEstimator.sizeOf(cache)); //计算指定对象及其引用树上的所有对象的综合大小，单位字节
        System.out.println("all memory:" + RamUsageEstimator.humanSizeOf(cache)); //计算指定对象及其引用树上的所有对象的综合大小，并返回可读的结果
        System.out.println("root byte:" + RamUsageEstimator.shallowSizeOf(cache)); //计算指定对象本身在堆空间的大小，单位字节
    }

}
