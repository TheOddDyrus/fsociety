package com.thomax.letsgo.advanced.arithmetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

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
 * 原理：不断将列表拆分为一半，如果列表为空或有一个项，则按定义进行排序。如果列表有多个项，我们分割列表，并递归调用两个半部分的合并排序。
 *       一旦对两半排序完成，获取两个较小的排序列表并将它们组合成单个排序的新列表的过程
 * 适用场景：①数据量大，并且对稳定性有要求的情况
 */
class MergeSort {

    public static void sort(int[] arr, int left, int right) {
        int mid = (left + right) / 2;
        if (left < right) {
            sort(arr, left, mid);
            sort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    public static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;
        // 把较小的数先移到新数组中
        while (i <= mid && j <= right) {
            if (arr[i] < arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        // 把左边剩余的数移入数组
        while (i <= mid) {
            temp[k++] = arr[i++];
        }
        // 把右边边剩余的数移入数组
        while (j <= right) {
            temp[k++] = arr[j++];
        }
        // 把新数组中的所有值复制到原数组中
        if (temp.length >= 0) {
            System.arraycopy(temp, 0, arr, left, temp.length);
        }
    }

}

/**
 * 快速排序
 *
 * 平均时间复杂度=O(n²)，最好的情况=O(n)，最坏的情况=O(n²)，空间复杂度=O(1)，不需要额外空间，不稳定的排序方式
 * 原理：①先从数列中取出一个数作为基准数。
 *       ②分区过程，将比这个数大的数全放到它的右边，小于或等于它的数全放到它的左边。
 *       ③再对左右区间重复第二步，直到各区间只有一个数
 * 适用场景：①若初始状态基本有序(指正序)，可使用快速排序
 */
class FastSort {

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
 * 原理：①堆的概念：堆排序里的堆指的是二叉堆（binary heap），是一种完全二叉树，二叉堆有两种：最大堆和最小堆，特点是父节点的值大于（小于）两个小节点的值
 *       ②基础知识：完全二叉树有一个性质是，除了最底层，每一层都是满的，这使得堆可以利用数组来表示，每个结点对应数组中的一个元素
 *       ③堆的基本操作：
 *          1）最大堆调整：该操作主要用于维持堆的基本性质。假设数组A和下标i，假定以Left(i)和Right(i)为根结点的左右两棵子树都已经是最大堆，
 *          节点i的值可能小于其子节点。调整节点i的位置，使得子节点永远小于父节点
 *          2）创建最大堆：是将一个数组改造成一个最大堆，接受数组和堆大小两个参数，创建最大堆将自下而上的调用最大堆调整来改造数组，建立最大堆。
 *          因为最大堆调整能够保证下标 i 的结点之后结点都满足最大堆的性质，所以自下而上的调用最大堆调整能够在改造过程中保持这一性质。
 *          如果最大堆的数量元素是 n，那么创建最大堆从 Parent(n) 开始，往上依次调用最大堆调整
 *          3）堆排序：先调用创建最大堆将数组改造为最大堆，然后将堆顶和堆底元素交换，之后将底部上升，最后重新调用最大堆调整保持最大堆性质。
 *          由于堆顶元素必然是堆中最大的元素，所以一次操作之后，堆中存在的最大元素被分离出堆，重复n-1次之后，数组排列完毕。
 *          如果是从小到大排序，用大顶堆；从大到小排序，用小顶堆
 * 适用场景：①若n较小(如n≤50)，可采用直接插入或直接选择排序。当记录规模较小时，直接插入排序较好；否则因为直接选择移动的记录数少于直接插人，应选直接选择排序为宜。
 *           ②若文件初始状态基本有序(指正序)，则应选用直接插人、冒泡或随机的快速排序为宜；
 *           ③若n较大，则应采用时间复杂度为O(nlgn)的排序方法：快速排序、堆排序或归并排序。
 *     　        1）快速排序是目前基于比较的内部排序中被认为是最好的方法，当待排序的关键字是随机分布时，快速排序的平均时间最短；
 *     　        2）堆排序所需的辅助空间少于快速排序，并且不会出现快速排序可能出现的最坏情况。这两种排序都是不稳定的。
 *     　        3）若要求排序稳定，则可选用归并排序。但本章介绍的从单个记录起进行两两归并的  排序算法并不值得提倡，通常可以将它和直接插入排序结合在一起使用。
 *               先利用直接插入排序求得较长的有序子文件，然后再两两归并之。因为直接插入排序是稳定 的，所以改进后的归并排序仍是稳定的。
 */
class HeapSort {

    public static void sort(int[] arr) {
        //1.构建大顶堆
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            //从第一个非叶子结点从下至上，从右至左调整结构
            adjustHeap(arr, i, arr.length);
        }
        //2.调整堆结构+交换堆顶元素与末尾元素
        for (int j = arr.length - 1; j > 0; j--) {
            //将堆顶元素与末尾元素进行交换
            swap(arr, 0, j);
            //重新对堆进行调整
            adjustHeap(arr, 0, j);
        }

    }

    /**
     * 调整大顶堆（仅是调整过程，建立在大顶堆已构建的基础上）
     */
    public static void adjustHeap(int[] arr, int i, int length) {
        //先取出当前元素i
        int temp = arr[i];
        //从i结点的左子结点开始，也就是2i+1处开始
        for (int k = i * 2 + 1; k < length; k = k * 2 + 1) {
            //如果左子结点小于右子结点，k指向右子结点
            if (k + 1 < length && arr[k] < arr[k + 1]) {
                k++;
            }
            //如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
            if (arr[k] > temp) {
                arr[i] = arr[k];
                i = k;
            } else {
                break;
            }
        }
        //将temp值放到最终的位置
        arr[i] = temp;
    }

    public static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

}


/**
 * 计数排序
 *
 * 平均时间复杂度=O(n + k)，最好的情况=O(n + k)，最坏的情况=O(n + k)，空间复杂度=O(k)，需要额外空间，稳定的排序方式
 * 原理：与比较排序注重于元素之间的比较不同，计数排序专注于找准自己的位置，而不关心自己比谁小，比谁大。其核心在于对于统计出现的次数
 * 适用场景：①适合于待排序的值的范围较小。比如100万学生参加高考，我们想对这100万学生的数学成绩（假设分数为0到100）做个排序
 */
class CountSort {

    private static final HashMap<Integer, Integer> COUNT_MAP = new HashMap<>(101); //假设是学生分数0~100

    public static void sort(int[] arr) {
        for (int key : arr) {
            COUNT_MAP.merge(key, 1, Integer::sum); //JDK1.8写法
        }

        int index = 0;
        for (Entry<Integer, Integer> entry : COUNT_MAP.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                arr[index++] = entry.getKey();
            }
        }
    }

}

/**
 * 桶排序
 *
 * 平均时间复杂度=O(n + k)，最好的情况=O(n + k)，最坏的情况=O(n²)，空间复杂度=O(n + k)，需要额外空间，稳定的排序方式
 * 原理：将一组待排序的数组，按照某种映射规则分配到有限数量的桶中，桶中的记录值再按照某种排序规则排序，最后将这些有限数量的桶中的记录值合并，即得到排序后的数组
 *          ①桶数量的确定。假设待排序记录数是N，桶数是k，一般可根据k^2=N来确定桶的数量。
 *          ②映射规则。在桶数量确定的情况下，要确定映射规则，使得从待排序数组中依次取出的记录值，能够判定其分配到坐标为几的桶中。
 *            假设待排序的记录值是array[i]，那么根据映射规则，必然能够找到某个桶B[j]，记录值array[i]就是B[j]中的一个元素。
 *          ③每个桶内记录值的排序。可以使用快速排序，也可以使用插入排序，如果待排序的记录值是整数，甚至可以使用集合类自带的排序方法。
 *          ④所有不为空的桶的记录值合并，形成最终排序后的有序数组
 * 适用场景：①桶排序是计数排序的升级版，有些排序场景计数排序无法使用（数值超出范围或者不是整数）。
 *             将数据分到有限数量的桶里，每个桶再分别排序（有可能再使用别的排序算法或是以递归方式继续使用桶排序）
 */
class BucketSort {

    public static void bucketSort(float[] arr) {
        int bucketTotal = 10; //桶的数量，可以根据系统环境因素决定数量
        // 新建一个桶的集合
        ArrayList<LinkedList<Float>> buckets = new ArrayList<>();
        for (int i = 0; i < bucketTotal; i++) {
            buckets.add(new LinkedList<>());
        }
        // 将输入数据全部放入桶中并完成排序
        for (float data : arr) {
            int index = getBucketIndex(data);
            insertSort(buckets.get(index), data);
        }

        int index = 0;
        for (LinkedList<Float> bucket : buckets) {
            for (Float data : bucket) {
                arr[index++] = data;
            }
        }
    }

    /**
     * 计算得到输入元素应该放到哪个桶内
     */
    public static int getBucketIndex(float data) {
        // 这里例子写的比较简单，仅使用浮点数的整数部分作为其桶的索引值
        // 实际开发中需要根据场景具体设计
        return (int) data;
    }

    /**
     * 选择插入排序作为桶内元素排序的方法 每当有一个新元素到来时，我们都调用该方法将其插入到恰当的位置
     */
    public static void insertSort(List<Float> bucket, float data) {
        ListIterator<Float> it = bucket.listIterator();
        boolean insertFlag = true;
        while (it.hasNext()) {
            if (data <= it.next()) {
                it.previous(); // 把迭代器的位置偏移回上一个位置
                it.add(data); // 把数据插入到迭代器的当前位置
                insertFlag = false;
                break;
            }
        }
        if (insertFlag) {
            bucket.add(data); // 否则把数据插入到链表末端
        }
    }

}

/**
 * 基数排序
 *
 * 平均时间复杂度=O(n * k)，最好的情况=O(n * k)，最坏的情况=O(n * k)，空间复杂度=O(n + k)，需要额外空间，稳定的排序方式
 * 原理：①是一种非比较型整数排序算法，其原理是将整数按位数切割成不同的数字，然后按每个位数分别比较
 * 适用场景：①电话号码、英文词典
 */
class RadixSort {

    public int[] sort(int[] sourceArray) throws Exception {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        int maxDigit = getMaxDigit(arr);
        return radixSort(arr, maxDigit);
    }

    /**
     * 获取最高位数
     */
    private int getMaxDigit(int[] arr) {
        int maxValue = getMaxValue(arr);
        return getNumLength(maxValue);
    }

    private int getMaxValue(int[] arr) {
        int maxValue = arr[0];
        for (int value : arr) {
            if (maxValue < value) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    protected int getNumLength(long num) {
        if (num == 0) {
            return 1;
        }
        int lenght = 0;
        for (long temp = num; temp != 0; temp /= 10) {
            lenght++;
        }
        return lenght;
    }

    private int[] radixSort(int[] arr, int maxDigit) {
        int mod = 10;
        int dev = 1;

        for (int i = 0; i < maxDigit; i++, dev *= 10, mod *= 10) {
            // 考虑负数的情况，这里扩展一倍队列数，其中 [0-9]对应负数，[10-19]对应正数 (bucket + 10)
            int[][] counter = new int[mod * 2][0];

            for (int item : arr) {
                int bucket = ((item % mod) / dev) + mod;
                counter[bucket] = arrayAppend(counter[bucket], item);
            }

            int pos = 0;
            for (int[] bucket : counter) {
                for (int value : bucket) {
                    arr[pos++] = value;
                }
            }
        }

        return arr;
    }

    /**
     * 自动扩容，并保存数据
     */
    private int[] arrayAppend(int[] arr, int value) {
        arr = Arrays.copyOf(arr, arr.length + 1);
        arr[arr.length - 1] = value;
        return arr;
    }

}
