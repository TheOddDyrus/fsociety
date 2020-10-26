package com.thomax.letsgo.advanced.jvm;

import com.carrotsearch.sizeof.RamUsageEstimator;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 对象占用内存测试（单位：byte）
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

        for (int i = 0; i < 50000; i++) {
            String x = "ABCDEFGHIJK" + i;
            x = x.substring(x.length() - 12);
            cache.get(x);
        }

        System.out.println("size:" + cache.size());
        System.out.println("byte:" + RamUsageEstimator.sizeOf(cache));
    }

}
