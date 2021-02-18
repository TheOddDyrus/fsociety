package com.thomax.letsgo.advanced.arithmetic;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * 布隆过滤器主要用于判断一个元素是否在一个集合中，它可以使用一个位数组简洁的表示一个数组。
 * 它的空间效率和查询时间远远超过一般的算法，但是它存在一定的误判的概率，适用于容忍误判的场景。
 * 如果布隆过滤器判断元素存在于一个集合中，那么大概率是存在在集合中，如果它判断元素不存在一个集合中，那么一定不存在于集合中。
 * 所以常常被用于大数据去重。
 */
public class BloomFilter {

    private static final int DEFAULT_SIZE = 2 << 24; //33554432

    private static final int[] seeds = new int[]{5, 7, 11, 13, 31, 37, 61};

    private final BitSet bitSet = new BitSet(DEFAULT_SIZE);

    private final SimpleHash[] simpleHashes = new SimpleHash[seeds.length];

    /*简易布隆过滤器*/
    public BloomFilter() {
        for (int i = 0; i < seeds.length; i++) {
            simpleHashes[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }

    public void add(String value) {
        for (SimpleHash simpleHash : simpleHashes) {
            bitSet.set(simpleHash.hash(value), true);
        }
    }

    public boolean contains(String value) {
        if (value == null) {
            return false;
        }

        boolean ret = true;
        for (SimpleHash simpleHash : simpleHashes) {
            ret = bitSet.get(simpleHash.hash(value));
            if (!ret) {
                return false;
            }
        }

        return ret;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("123456");
        list.add("hello");
        list.add("##&");
        list.add("123456");
        list.add("##&");
        list.add("hello");
        list.add("test");

        BloomFilter bloomFilter = new BloomFilter();
        for (int i = 0; i < list.size(); i++) {
            String value = list.get(i);
            boolean exist = bloomFilter.contains(value);
            if (exist) {
                System.out.println("list中下标为:" + i + "的值发生重复 " + value);
            } else {
                bloomFilter.add(value);
            }
        }
    }

    private static class SimpleHash {
        private final int cap;
        private final int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
    }

}
