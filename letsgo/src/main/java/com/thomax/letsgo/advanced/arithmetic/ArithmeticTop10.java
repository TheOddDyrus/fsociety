package com.thomax.letsgo.advanced.arithmetic;

/**
 * 10大计算机经典算法
 */
public class ArithmeticTop10 {}

/**
 * 快速排序
 */
class FastSort {

    /**
     * @param arr 需要排序的数组
     * @param left 排序范围：开始时的下标
     * @param right 排序范围：结束时的下标
     */
    static void quickSort(int[] arr, int left, int right) {
        int dp;
        if (left < right) {
            dp = partition(arr, left, right);
            quickSort(arr, left, dp - 1);
            quickSort(arr, dp + 1, right);
        }
    }

    static int partition(int[] arr, int left, int right) {
        int pivot = arr[left];
        while (left < right) {
            while (left < right && arr[right] >= pivot) {
                right--;
            }
            if (left < right) {
                arr[left++] = arr[right];
            }
            while (left < right && arr[left] <= pivot) {
                left++;
            }
            if (left < right) {
                arr[right--] = arr[left];
            }
        }
        arr[left] = pivot;
        return left;
    }

    public static void main(String[] args) {
        int[] arr = {12, 20, 5, 16, 15, 1, 30, 1, 45, 23, 9};
        quickSort(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.print(i + " ");
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