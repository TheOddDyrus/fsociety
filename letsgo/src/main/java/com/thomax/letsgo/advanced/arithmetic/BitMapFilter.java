package com.thomax.letsgo.advanced.arithmetic;

import java.util.BitSet;

/**
 * 位图过滤
 *
 * >>相较于使用一个Set来存储N个数值，BitSet的内存占用是Set的1/32
 */
public class BitMapFilter {

    public static void main(String[] args) {
        int[] values = {0, 1, 2, 4, 5, 6, 8, 9};
        BitSet bitSet = new BitSet(values.length);
        for (int value : values) {
            bitSet.set(value);
        }
        System.out.println("bitSet: " + bitSet);
        System.out.println("bitSet contains 1?: " + bitSet.get(1));
        System.out.println("bitSet contains 3?: " + bitSet.get(3));
    }

}
