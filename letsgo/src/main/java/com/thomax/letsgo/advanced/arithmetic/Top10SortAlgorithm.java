package com.thomax.letsgo.advanced.arithmetic;

/**
 * 10大排序算法
 *
 * 时间复杂度：①算法的运行时间，在这里主要计算比较和交换的成本
 *             ②常见的时间复杂度排序：常数阶O(1) < 对数阶O(log2n) < 线性阶O(n) < 线性对数阶O(nlog2n) < 平方阶O(n²) < 立方阶O(n³) < 指数阶O(2^n)
 * 空间复杂度：使用的内存，是否需要额外的存储空间，相对额外的存储空间来计算成本
 * 稳定性：相等元素排序前后相对位置保持不变，就认为是稳定的
 */
public class Top10SortAlgorithm {

    public static void main(String[] args) {

    }

}

/**
 * 冒泡排序
 *
 * 性能分析：平均时间复杂度=O(n²)，最好的情况=O(n)，最坏的情况=O(n²)，空间复杂度=O(1)，不需要额外空间，稳定的排序方式
 * 适用场景：①适用元素较少的情况下，元素太多的话，交换和比较次数都会很多，影响效率，元素多的情况适合用快速排序
 *           ②当数组基本有序的情况下适合使用冒泡排序和插入排序，它们在基本有序的情况下排序的时间复杂度接近O(n)
 */
class BubbleSort {

    /**
     * @param arr 需要排序的数组
     */
    public static void sort(int[] arr) {
        for (int x = 0; x < arr.length - 1; x++) {
            for (int y = 0; y < arr.length - x - 1; y++) {
                if (arr[y] > arr[y + 1]) {
                    int temp = arr[y];
                    arr[y] = arr[y + 1];
                    arr[y + 1] = temp;
                }
            }
        }
    }

}

/**
 * 选择排序
 *
 * 性能分析：平均时间复杂度=O(n²)，最好的情况=O(n²)，最坏的情况=O(n²)，空间复杂度=O(1)，不需要额外空间，不稳定的排序方式
 * 适用场景：
 */
class SelectionSort {

}

/**
 * 插入排序
 *
 * 性能分析：平均时间复杂度=O(n²)，最好的情况=O(n)，最坏的情况=O(n²)，空间复杂度=O(1)，不需要额外空间，稳定的排序方式
 * 适用场景：
 */
class InsertionSort {

}

/**
 * 希尔排序
 *
 * 性能分析：平均时间复杂度=O(n*log(n))，最好的情况=O(n)，最坏的情况=O(n²)，空间复杂度=O(1)，不需要额外空间，不稳定的排序方式
 * 适用场景：
 */
class ShellSort {

}

/**
 * 快速排序
 *
 * 平均时间复杂度=O(n²)，最好的情况=O(n)，最坏的情况=O(n²)，空间复杂度=O(1)，不需要额外空间，不稳定的排序方式
 */
class FastSort {

    /**
     * @param arr 需要排序的数组
     * @param left 排序范围：开始时的下标
     * @param right 排序范围：结束时的下标
     */
    public static void sort(int[] arr, int left, int right) {
        int mid;
        if (left < right) {
            mid = partition(arr, left, right);
            sort(arr, left, mid - 1);
            sort(arr, mid + 1, right);
        }
    }

    private static int partition(int[] arr, int left, int right) {
        int midVal = arr[left];
        while (left < right) {
            while (left < right && arr[right] >= midVal) {
                right--;
            }
            if (left < right) {
                arr[left++] = arr[right];
            }
            while (left < right && arr[left] <= midVal) {
                left++;
            }
            if (left < right) {
                arr[right--] = arr[left];
            }
        }
        arr[left] = midVal;

        return left;
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