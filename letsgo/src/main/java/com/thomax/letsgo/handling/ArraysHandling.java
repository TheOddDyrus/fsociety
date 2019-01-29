package com.thomax.letsgo.handling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArraysHandling{
	public static void main(String[] args){
//		UnionSet example = new UnionSet();
//		example.main(args);
	}
}


/**class1-数组中元素的排序,查找,添加*/  //import java.util.Arrays
/*Arrays.binarySearch()函数源码采用二分搜索法： 前提需要数组已经正序排序完毕，才能折半取值判断往左还是往右
	int low = fromIndex;//0
	int high = toIndex - 1;//Array.length -1
	
	while (low <= high) {
	    int mid = (low + high) >>> 1;
	    int midVal = a[mid];
	
	    if (midVal < key)
	    low = mid + 1;
	    else if (midVal > key)
	    high = mid - 1;
	    else
	    return mid; // key found
	}
	return -(low + 1);  // key not found. 
 */
class SortFindAdd {
   public static void main(String args[]) {
      int array[] = { 2, 5, -2, 6, -3, 8, 0, -7, -9, 4 };
      Integer array2[] = { 2, 5, -2, 6, -3, 8, 0, -7, -9, 4 };
      Arrays.sort(array);
      Arrays.sort(array2, Collections.reverseOrder());  //倒序排序，利用Collections.reverseOrder()
      printArray("数组排序", array);
      int index = Arrays.binarySearch(array, 2);   //Arrays.binarySearch(数组， 元素的值)   数组内元素位置从0开始
      int index2 = Arrays.binarySearch(array, 1);  //元素若在数组内不存在则返回负数，且若将此不存在的元素按顺序插入进去，则位置= -(index2 + 1)
      System.out.println("元素 2 所在位置：" + index);
      System.out.println("元素 1 所在位置：" + index2);
      int newIndex = -index2 - 1;
      System.out.println("如果将元素 1 按顺序插入，它的位置：" + newIndex);
      array = insertElement(array, 1, newIndex); 
      printArray("数组添加元素 1", array);
      List<String> ne = new ArrayList<String>();
      Collections.sort(ne, Collections.reverseOrder());
   }
   private static void printArray(String message, int array[]) {
      System.out.println(message + ": [length: " + array.length + "]");
      for (int i = 0; i < array.length; i++) {
         if (i != 0){
            System.out.print(", ");
         }
         System.out.print(array[i]);
      }
      System.out.println();
   }
   private static int[] insertElement(int original[],
   int element, int index) {
      int length = original.length;
      int destination[] = new int[length + 1];
     /*
      System.arraycopy(Object src, //源数组
              int srcPos, //源数组要复制的起始位置
              Object dest, //目的数组
              int destPos, //目的数组放置的起始位置
              int length) //复制的长度
      */
      System.arraycopy(original, 0, destination, 0, index);
      destination[index] = element;
      System.arraycopy(original, index, destination, index + 1, length - index);   //length - index = (length + 1) - (index + 1)
      return destination;
   }
}
//数组排序: [length: 10]
//-9, -7, -3, -2, 0, 2, 4, 5, 6, 8
//元素 2 所在位置：5
//元素 1 所在位置：-6
//如果将元素 1 按顺序插入，它的位置：5
//数组添加元素 1: [length: 11]
//-9, -7, -3, -2, 0, 1, 2, 4, 5, 6, 8


/**class2-获取数组长度*/
class GetLength {
	   public static void main(String args[]) {
	      String[][] data = new String[2][5];
	      //String[][] data2 = new String[3][];
	      System.out.println("String[2][5]的第一维数组长度: " + data.length);
	      System.out.println("String[2][5]的第二维数组长度: " + data[0].length);
	      //System.out.println("String[3][]的第二维数组长度: " + data2[0].length); -> java.lang.NullPointerException
	   }
}
//String[2][5]的第一维数组长度: 2
//String[2][5]的第二维数组长度: 5


/**class3-数组反转*/  //java.util.ArrayList;  java.util.Collections
class ArrayReverse{
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("C");
        arrayList.add("D");
        arrayList.add("E");
        System.out.println("反转前排序: " + arrayList);
        Collections.reverse(arrayList);
        System.out.println("反转后排序: " + arrayList);
    }
}
//反转前排序: [A, B, C, D, E]
//反转后排序: [E, D, C, B, A]


/**class4-数组获取最大和最小值*/
class MaxMin {
    public static void main(String[] args) {
        Integer[] numbers = { 8, 2, 7, 1, 4, 9, 5};
        int min = (int) Collections.min(Arrays.asList(numbers)); //Arrays.asList()将数组转化成List<T>，再用Collections的min()方法从List<T>遍历出最小值
        int max = (int) Collections.max(Arrays.asList(numbers));
        System.out.println("最小值: " + min);
        System.out.println("最大值: " + max);
        
        int temp = numbers[0];
        for(int x : numbers) {
        	if(temp>x)  temp =x;
        }
        System.out.println("最小值: " + temp);
        temp = numbers[0];
        for(int x : numbers) {
        	if(temp<x)  temp =x;
        }
        System.out.println("最大值: " + temp);
    }
}
//最小值: 1
//最大值: 9
//最小值: 1
//最大值: 9


/**class5-数组合并*/  //import java.util.List
class Merge{
    public static void main(String args[]) {
        String a[] = { "A", "E", "I" };
        String b[] = { "O", "U" };
        List<String> list = new ArrayList<String>(Arrays.asList(a));  //List list = new ArrayList(List<T>) 与 ArrayList list = new ArrayList(List<T>) 都可以
        list.addAll(Arrays.asList(b));
        Object[] c = list.toArray();         //list.toArray()将List转化为数组
        System.out.println(Arrays.toString(c));
    }
}
//[A, E, I, O, U]


/**class6-数组填充*/
class FillArrays {
    public static void main(String args[]) {
        int array[] = new int[6];
        Arrays.fill(array, 100);   //Arrays.fill(Object[] a, Object val)
        for (int i=0, n=array.length; i < n; i++) {
            System.out.println(array[i]);
        }
        System.out.println();
        Arrays.fill(array, 3, 6, 50);   //Arrays.fill(int[] a, int fromIndex, int toIndex, int val)   数组下标要从0开始计
        for (int i=0, n=array.length; i< n; i++) {
            System.out.println(array[i]);
        }
    }
}
//100
//100
//100
//100
//100
//100
//
//100
//100
//100
//50
//50
//50


/**class7-数组扩容*/
class Dilatation {
	    public static void main(String args[]) {
	            long startTime1 = System.currentTimeMillis();
	        for(int i=0; i<100000; i++)
	        {
	                String[] names = new String[] {"A", "B", "C"};
	                String[] extended = new String[5];
	                extended[3] = "D";
	                extended[4] = "E";
	                System.arraycopy(names, 0, extended, 0, names.length);
	        }
	        long endTime1 = System.currentTimeMillis();
	        System.out.println("arraycopy花费时间"+(endTime1-startTime1)+"毫秒");
	        
	        long startTime2 = System.currentTimeMillis();
	        for(int i=0; i<100000; i++)
	        {
	                String[] names = new String[] {"A", "B", "C"};
	                String[] extended = new String[5];
	                ArrayList<String> list = new ArrayList<String>(Arrays.asList(names));
	                list.add("D");
	                list.add("E");
	                list.toArray(extended);
	        }
	        long endTime2 = System.currentTimeMillis();
	        System.out.println("list花费时间"+(endTime2-startTime2)+"毫秒");
	    }
}
//arraycopy花费时间26毫秒
//list花费时间28毫秒


/**class8-查找数组中的重复元素*/
class RepetitionElement {
    public static void main(String[] args)
    {
        int[] my_array = {1, 2, 5, 5, 5, 6, 6, 7, 2};
        Arrays.sort(my_array);  //{1, 2, 2, 5, 5, 5, 6, 6, 7};
        List<Integer> list=new ArrayList<Integer>();
        int i;
        for( i=0; i<my_array.length; i++) {
        	if(i == 0) {
        		if(Arrays.binarySearch(my_array, (i+1), (my_array.length-1), my_array[i]) > 0) {
            		list.add(my_array[0]);
            	}
        	}else if(i == (my_array.length -1)) {
        		if(Arrays.binarySearch(my_array, 0, (i-1), my_array[i]) > 0) {
            		list.add(my_array[i]);
            	}
        	}else {
        		if(Arrays.binarySearch(my_array, (i+1), (my_array.length-1), my_array[i]) > 0 || Arrays.binarySearch(my_array, 0, i, my_array[i]) > 0) {
            		list.add(my_array[i]);
            	}
        	}
        }
        System.out.println("重复元素 : "+ list.toString());
        System.out.println("XXXXXXXX : "+ Arrays.binarySearch(my_array, 0, 1, 2));
        System.out.println("XXXXXXXX : "+ Arrays.binarySearch(my_array, 0, 2, 2)); //比如范围123456789中的4567，Java里面fromIndex是元素4下标，toIndex是元素8下标
        System.out.println("XXXXXXXX : "+ Arrays.binarySearch(my_array, 0, 2, 5));
        System.out.println("XXXXXXXX : "+ Arrays.binarySearch(my_array, 0, 3, 5)); //比如范围123456789中的4567，Java里面fromIndex是元素4下标，toIndex是元素8下标
        	
    }
}
//重复元素 : [2, 2, 5, 5, 5, 6, 6]
//XXXXXXXX : -2
//XXXXXXXX : 1
//XXXXXXXX : -3
//XXXXXXXX : -4


/**class9-删除数组元素*/
class DeleteArray {
    public static void main(String[] args)  {
        ArrayList<String> objArray = new ArrayList<String>();
        objArray.add(0,"第 0 个元素");
        objArray.add(1,"第 1 个元素");
        objArray.add(2,"第 2 个元素");
        System.out.println("数组删除元素前："+objArray);
        
        objArray.remove(1);
        System.out.println("数组删除元素前："+objArray);
        
        objArray.remove("第 0 个元素");  //ArrayList<>.remove(T t)
        System.out.println("数组删除元素后："+objArray);
        
        objArray.clear();  //ArrayList<>.clear()
        System.out.println("数组clear()后："+objArray);
    }
}
//数组删除元素前：[第 0 个元素, 第 1 个元素, 第 2 个元素]
//数组删除元素前：[第 0 个元素, 第 2 个元素]
//数组删除元素后：[第 2 个元素]
//数组clear()后：[]


/**class10-数组差集与交集*/
class DiffInter {
	
	public ArrayList<String> objArray;
	private ArrayList<String> objArray2;
	
    @SuppressWarnings("unlikely-arg-type")
	public void runDiff(String[] args) {
    	int[] ary = {1,2,3,4,5,6};
        int[] ary1 = {1,2,3,4,5,6};
        int[] ary2 = {1,2,3,4};
        objArray = new ArrayList<String>();
        objArray2 = new ArrayList<String>();
        ArrayList<String> objArray3,objArray4;
        objArray.add(0,"common1");
        objArray.add(1,"common2");
        objArray.add(2,"notcommon2");
        objArray3 = new ArrayList<String>(objArray);//直接用objArray3 = objArray的话objArray3的引用与objArray一致，objArray中数据修改会导致objArray3数据也随着修改
        System.out.println("array1 的元素" +objArray);
        
        objArray2.add(0,"common1");
        objArray2.add(1,"common2");
        objArray2.add(2,"notcommon");
        objArray2.add(3,"notcommon1");
        objArray4 = new ArrayList<String>(objArray2);//直接用objArray4 = objArray2的话objArray4的引用与objArray2一致，objArray2中数据修改会导致objArray4数据也随着修改
        System.out.println("array2 的元素" +objArray2);
        
        System.out.println("array1 是否包含字符串 \"common2\" ： " + objArray.contains("common1")); //ArrayList<>.contains(T t)
        System.out.println("array2 是否包含array1 ：" + objArray2.contains(objArray) ); //ArrayList<>.contains(ArrayList<>)
        System.out.println("array1 是否与array2相等 ：" + objArray.equals(objArray2));  //ArrayList<>.equals(ArrayList<>)
        
        objArray.removeAll(objArray2);   //ArrayList<>.removeAll(ArrayList<>)
        System.out.println("array1 与 array2 数组差集为："+objArray);
        
        objArray3.retainAll(objArray4);  //ArrayList<>.retainAll(ArrayList<>)
        System.out.println("array2 与 array1 数组交集为："+objArray3);
        
        System.out.println("数组 ary 是否与数组 ary1相等 ：" + Arrays.equals(ary, ary1)); //Arrays.equals(array, array)
        System.out.println("数组 ary 是否与数组 ary2相等 ：" + Arrays.equals(ary, ary2)); //Arrays.equals(array, array)
    }
}
//array1 的元素[common1, common2, notcommon2]
//array2 的元素[common1, common2, notcommon, notcommon1]
//array1 是否包含字符串 "common2" ： true
//array2 是否包含array1 ：false
//array1 是否与array2相等 ：false
//array1 与 array2 数组差集为：[notcommon2]
//array2 与 array1 数组交集为：[common1, common2]
//数组 ary 是否与数组 ary1相等 ：true
//数组 ary 是否与数组 ary2相等 ：false


/**class11-数组并集*/  //java.util.Set; java.util.HashSet
class UnionSet {
    public static void main(String[] args) {
        String[] arr1 = { "1", "2", "3" };
        String[] arr2 = { "4", "5", "6" };
        Set<String> set = new HashSet<String>();
        
        for (String str : arr1) {
            set.add(str);
        }
 
        for (String str : arr2) {
            set.add(str);
        }
        System.out.println("set集合中的元素: " + set);
        
        String[] result = new String[set.size()];  //Set<>.size()
        set.toArray(result);  //利用set的元素唯一性 set.toArray(array)
        
        for (String str : result) {
            System.out.println(str);
        }
    }
}
//set集合中的元素: [1, 2, 3, 4, 5, 6]
//1
//2
//3
//4
//5
//6



















