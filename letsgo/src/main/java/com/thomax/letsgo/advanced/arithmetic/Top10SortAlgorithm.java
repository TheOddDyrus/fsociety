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
        int[] arr = {11, 3, 6, 1, 7, 9, 3, 2, 7, 9, 12, 13, 3};

        ShellSort.sort(arr);

        for (int i : arr) {
            System.out.print(i + " ");
        }
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
 * 原理：遍历元素找到一个最小（或最大）的元素，把它放在第一个位置，然后再在剩余元素中找到最小（或最大）的元素，把它放在第二个位置，依次下去，完成排序
 * 适用场景：①当n <= 50时，适合适用简单选择排序和直接插入排序，如果元素包含的内容过大，简单选择排序更合适
 *           ②简单选择排序适合用于n较小时
 */
class SelectionSort {

    public static void sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (i != minIndex) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
    }

}

/**
 * 插入排序
 *
 * 性能分析：平均时间复杂度=O(n²)，最好的情况=O(n)，最坏的情况=O(n²)，空间复杂度=O(1)，不需要额外空间，稳定的排序方式
 * 原理：通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入
 * 适用场景：①数据量不大，并且对稳定性有要求并且数据局部或者整体有序的情况，类似扑克牌的整理方式
 */
class InsertionSort {

    public static void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int j = i;
            int temp = arr[i];
            if (arr[j - 1] > temp) {
                while (j >= 1 && arr[j - 1] > temp) {
                    arr[j] = arr[j - 1];
                    j--;
                }
            }
            arr[j] = temp;
        }
    }

}

/**
 * 希尔排序
 *
 * 性能分析：最好的情况=O(n^(3/2))，最坏的情况=O(n²)，空间复杂度=O(1)，不需要额外空间，不稳定的排序方式
 * 原理：设待排序元素序列有n个元素，首先取一个整数increment（小于n）作为间隔将全部元素分为increment个子序列，所有距离为increment的元素放在同一个子序列中，
 *       在每一个子序列中分别实行直接插入排序。然后缩小间隔increment，重复上述子序列划分和排序工作。直到最后取increment=1，将所有元素放在同一个子序列中排序为止
 * 适用场景：①希尔排序是对直接插入排序的一种优化，可以用于大型的数组，希尔排序比插入排序和选择排序要快的多，并且数组越大，优势越大
 *           ②Shell排序比冒泡排序快5倍，比插入排序大致快2倍。Shell排序比起QuickSort，MergeSort，HeapSort慢很多。但是它相对比较简单，
 *             它适合于数据量在5000以下并且速度并不是特别重要的场合。它对于数据量较小的数列重复排序是非常好的
 */
class ShellSort {

    public static void sort(int[] arr) {
        int current;
        int increment = arr.length / 2;

        while(increment > 0) {
            for(int i = increment; i < arr.length; i++) {
                current = arr[i];
                int preIndex = i - increment;

                while(preIndex >= 0 && current < arr[preIndex]) {
                    arr[preIndex + increment] = arr[preIndex];
                    preIndex -= increment;
                }
                arr[preIndex + increment] = current;
            }
            increment /= 2;
        }
    }

}

/**
 * 归并排序
 *
 * 性能分析：平均时间复杂度=O(n*log(n))，最好的情况=O(n*log(n))，最坏的情况=O(n*log(n))，空间复杂度=O(n)，需要额外空间，稳定的排序方式
 * 原理：
 * 适用场景：①
 */
class MergeSort {

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
 *
 * 性能分析：平均时间复杂度=O(n*log(n))，最好的情况=O(n*log(n))，最坏的情况=O(n*log(n))，空间复杂度=O(1)，不需要额外空间，不稳定的排序方式
 * 原理：
 * 适用场景：①
 */
class HeapSort {

}


/**
 * 计数排序
 */
class CountSort {

}

/**
 * 桶排序
 */
class BucketSort {

}

/**
 * 基数排序
 */
class RadixSort {

}
