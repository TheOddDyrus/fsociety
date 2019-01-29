package com.thomax.letsgo.handling;

/**
 * 10大计算机经典算法
 * @author thomax
 */
public class ArithmeticTop10 {}

/**
 * 快速排序
 */
class FastSort {

    /**
     * @param a 需要排序的数组
     * @param low 排序范围：开始时的下标
     * @param high 排序范围：结束时的下标
     */
    public static void sort(int[] a, int low, int high) {
        int start = low;
        int end = high;
        int key = a[low];
        int temp;

        while (end > start) {
            //从后往前比较：如果没有比关键值小的，比较下一个，直到有比关键值小的交换位置，然后又从前往后比较
            while (end > start && a[end] >= key) {
                end--;
                if (a[end] <= key) {
                    temp = a[end];
                    a[end] = a[start];
                    a[start] = temp;
                }
            }
            //从前往后比较：如果没有比关键值大的，比较下一个，直到有比关键值大的交换位置
            while (end > start && a[start] <= key) {
                start++;
                if (a[start] >= key) {
                    temp = a[start];
                    a[start] = a[end];
                    a[end] = temp;
                }
            }
        }

        if (start > low) sort(a, low, start - 1); //左边序列。第一个索引位置到关键值索引-1
        if (end < high) sort(a, end + 1, high); //右边序列。从关键值索引+1到最后一个
    }

    public static void main(String[] args) {
        int[] a = {12, 20, 5, 16, 15, 1, 30, 45, 23, 9};
        sort(a, 0, a.length - 1);
        for (int i : a) {
            System.out.println(i);
        }
    }

}

/**
 * 堆排序
 */
class HeapSort {
}

/**
 * 归并排序
 */
class MergeSort {
}

/**
 * 二分查找
 */
class BinarySearch {
}

/**
 * 线性查找（BFPRT）
 */
class BlumFloydPrattRivestTarjan {
}

/**
 * 深度优先搜索（DFS）
 */
class DepthFirstSearch {
}

/**
 * 广度优先搜索（BFS）
 */
class BreadthFirstSearch {
}

/**
 * 戴克斯特拉算法
 */
class Dijkstra {
}

/**
 * 动态规划算法
 */
class DynamicProgramming {
}

/**
 * 朴素贝叶斯分类算法
 */
class NaiveBayes {
}