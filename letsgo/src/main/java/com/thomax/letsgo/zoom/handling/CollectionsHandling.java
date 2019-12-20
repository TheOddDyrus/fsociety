package com.thomax.letsgo.zoom.handling;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;


@SuppressWarnings("unused")
public class CollectionsHandling {
	public static void main(String[] args) throws Exception {
		/********集合<E>中的E都是包装类************/
//		ListCollection oop = new ListCollection();
//		oop.main(args);
		
	}
}


/**class1-List集合*/  //java.util.List; java.util.Arrays;
class ListCollection{
	   public static void main(String args[]) {
	      String[] name = {"Penny", "nickel", "dime", "Quarter", "dollar"};
	      List<String> list = Arrays.asList(name);  //数组转List集合 -> 返回的是Arrays的内部私有类 -> private static class ArrayList<E>
//	      List<String> list = new ArrayList<String>();
//	      for (int i = 0; i < name.length; i++) {
//	    	  list.add(name[i]);
//	      }
	      System.out.println("List集合的内容: " + list);
	      System.out.println("List集合的长度: " + list.size());  //
	      System.out.println(Collections.min(list));
	      System.out.println(Collections.min(list, String.CASE_INSENSITIVE_ORDER)); //String.CASE_INSENSITIVE_ORDER) -> 申明类型是比较器Comparator<String> -> 忽略大小写排序
	      System.out.println("-----------");
	      System.out.println(Collections.max(list));
	      System.out.println(Collections.max(list, String.CASE_INSENSITIVE_ORDER));
	      
	      String[] str = list.toArray(new String[0]);  //List集合转数组
	      System.out.print("List集合转数组:");
	      for(String st : str) {
	    	  System.out.print(" " + st);
	      }
	      System.out.println();
	      
	      Collections.reverse(list);  //反转集合内元素,Collections.reverse()只能反转List的集合, 配合sort()可以倒序排序集合
	      System.out.println("Collections.reverse(list) -> " + list);
	      
	      //list.remove("3");  // UnsupportedOperationException -> 只有通过List<String> list = new ArrayList<String>() 创建的list才能进行增删操作
	      //list.add("3");  // UnsupportedOperationException -> 只有通过List<String> list = new ArrayList<String>() 创建的list才能进行增删操作
	      
	     /*执行以下代码会抛出异常
	      list = Collections.unmodifiableList(list);   //设置为只读(只有List集合可以设置为只读)
	      try {
	          list.set(0, "new value 0");  //throws java.lang.UnsupportedOperationException
	      } catch (UnsupportedOperationException e) {
	      }
	      */
	      
	      Collections.shuffle(list);  //打乱集合内元素的顺序
	      System.out.println("第一次Collections.shuffle(list) -> " + list);
	      Collections.shuffle(list);
	      System.out.println("第二次Collections.shuffle(list) -> " + list);
	      Collections.shuffle(list);
	      System.out.println("第三次Collections.shuffle(list) -> " + list);
	      
	      System.out.println("替换与截取**********************");
	      List<String> listRS = Arrays.asList("one#Two#three#Four#five#six#one#three#Four".split("#"));
	      System.out.println("listRS :" + listRS);
	      Collections.replaceAll(listRS, "one", "Thomas");  //只能替换List集合: replaceAll(List<T> list, T oldVal, T newVal)
	      System.out.println("replaceAll(listR, \"one\", \"Thomas\") -> " + listRS);
	      List<String> sublist = Arrays.asList("three@Four".split("@"));
	      List<String> sublist2 = Arrays.asList("Xman!Xour".split("!"));
	      System.out.println("sublist :" + sublist);
	      System.out.println("sublist2 :" + sublist2);
	      System.out.println("Collections.indexOfSubList(listRS, sublist) -> " + Collections.indexOfSubList(listRS, sublist));  //只能比较List集合:indexOfSubList(List<?> source, List<?> target)
	      System.out.println("Collections.lastIndexOfSubList(listRS, sublist) -> " + Collections.lastIndexOfSubList(listRS, sublist));  //只能比较List集合:lastIndexOfSubList(List<?> source, List<?> target)
	      System.out.println("Collections.indexOfSubList(listRS, sublist2) -> " + Collections.indexOfSubList(listRS, sublist2));
	      System.out.println("Collections.lastIndexOfSubList(listRS, sublist2) -> " + Collections.lastIndexOfSubList(listRS, sublist2));
	      System.out.println("替换与截取**********************");
	      
	      System.out.println("rotate----------------------");
	      //Collections.rotate(List<E>, Index) 从List<E>的末尾数Index个元素(Index需要>=0)放到开头去，例如：
	      //Collections.rotate(List<int>, 3) -> 1, 2, 3, 4, 5, 6, 7, 8, 9 -> Index=3个元素是7, 8, 9 -> 7, 8, 9 & 1, 2, 3, 4, 5, 6
	      List<String> list2 = Arrays.asList("one two three 4 5 6 7 EIGHT nine ten".split(" ")); // "e".split(String regex) -> e中的值通过regex的内容来隔开分离出String[]
	      System.out.println("\"e\".split(String regex) -> "+list2);
	      Collections.rotate(list2, 3);  //只有List集合可以循环移动元素(自旋)
	      System.out.println("Collections.rotate(list2, 3) -> " + list2);
	      System.out.println("list2.subList(1, 6) -> " + list2.subList(1, 6));  //fromIndex正常，toIndex比下标多1
	      List<String> listTest0 = new ArrayList<String>(list2); //利用new构造一个新的集合对象，内容与list2一样
	      List<String> listTest1 = new ArrayList<String>(list2);
	      List<String> listTest2 = new ArrayList<String>(list2);
	      List<String> listTest3 = new ArrayList<String>(list2);
	      List<String> listTest4 = new ArrayList<String>(list2);
	      List<String> listTest5 = new ArrayList<String>(list2);
	      List<String> listTest6 = new ArrayList<String>(list2);
	      Collections.rotate(listTest0.subList(1, 6), 0); //循环到原始内容
	      System.out.println("Collections.rotate(listTest0.subList(1, 6), 0) -> " + listTest0);
	      Collections.rotate(listTest1.subList(1, 6), 1);
	      System.out.println("Collections.rotate(listTest0.subList(1, 6), 1) -> " + listTest1);
	      Collections.rotate(listTest2.subList(1, 6), 2);
	      System.out.println("Collections.rotate(listTest0.subList(1, 6), 2) -> " + listTest2);
	      Collections.rotate(listTest3.subList(1, 6), 3);
	      System.out.println("Collections.rotate(listTest0.subList(1, 6), 3) -> " + listTest3);
	      Collections.rotate(listTest4.subList(1, 6), 4);
	      System.out.println("Collections.rotate(listTest0.subList(1, 6), 4) -> " + listTest4);
	      Collections.rotate(listTest5.subList(1, 6), 5); //循环到原始内容
	      System.out.println("Collections.rotate(listTest0.subList(1, 6), 5) -> " + listTest5);
	      Collections.rotate(listTest6.subList(1, 6), 6);
	      System.out.println("Collections.rotate(listTest0.subList(1, 6), 6) -> " + listTest6);
	      System.out.println("rotate----------------------");
	      
	      
	      //使用iterator遍历List集合
	      System.out.print("使用iterator遍历List集合: ");
	      Iterator<String> it = list.iterator();
	      while (it.hasNext()) {
	         String value = it.next();
	         System.out.print(" " + value);
	      }
	      System.out.println();
	      //使用传统for循环进行遍历List集合
	      System.out.print("使用传统for循环进行遍历List集合: ");
	      for (int i = 0, size = list.size(); i < size; i++) {
	         String value = list.get(i);
	         System.out.print(" " + value);
	      }
	      System.out.println();
	      //使用增强for循环进行遍历List集合
	      System.out.print("使用增强for循环进行遍历List集合: ");
	      for (String value : list) {
	         System.out.print(" " + value);
	      }
	   }
}
//List集合的内容: [Penny, nickel, dime, Quarter, dollar]
//List集合的长度: 5
//Penny
//dime
//-----------
//nickel
//Quarter
//List集合转数组: Penny nickel dime Quarter dollar
//Collections.reverse(list) -> [dollar, Quarter, dime, nickel, Penny]
//第一次Collections.shuffle(list) -> [Penny, Quarter, nickel, dime, dollar]
//第二次Collections.shuffle(list) -> [nickel, dollar, Quarter, Penny, dime]
//第三次Collections.shuffle(list) -> [dime, dollar, Penny, Quarter, nickel]
//替换与截取**********************
//listRS :[one, Two, three, Four, five, six, one, three, Four]
//replaceAll(listR, "one", "Thomas") -> [Thomas, Two, three, Four, five, six, Thomas, three, Four]
//sublist :[three, Four]
//sublist2 :[Xman, Xour]
//Collections.indexOfSubList(listRS, sublist) -> 2
//Collections.lastIndexOfSubList(listRS, sublist) -> 7
//Collections.indexOfSubList(listRS, sublist2) -> -1
//Collections.lastIndexOfSubList(listRS, sublist2) -> -1
//替换与截取**********************
//rotate----------------------
//"e".split(String regex) -> [one, two, three, 4, 5, 6, 7, EIGHT, nine, ten]
//Collections.rotate(list2, 3) -> [EIGHT, nine, ten, one, two, three, 4, 5, 6, 7]
//list2.subList(1, 6) -> [nine, ten, one, two, three]
//Collections.rotate(listTest0.subList(1, 6), 0) -> [EIGHT, nine, ten, one, two, three, 4, 5, 6, 7]
//Collections.rotate(listTest0.subList(1, 6), 1) -> [EIGHT, three, nine, ten, one, two, 4, 5, 6, 7]
//Collections.rotate(listTest0.subList(1, 6), 2) -> [EIGHT, two, three, nine, ten, one, 4, 5, 6, 7]
//Collections.rotate(listTest0.subList(1, 6), 3) -> [EIGHT, one, two, three, nine, ten, 4, 5, 6, 7]
//Collections.rotate(listTest0.subList(1, 6), 4) -> [EIGHT, ten, one, two, three, nine, 4, 5, 6, 7]
//Collections.rotate(listTest0.subList(1, 6), 5) -> [EIGHT, nine, ten, one, two, three, 4, 5, 6, 7]
//Collections.rotate(listTest0.subList(1, 6), 6) -> [EIGHT, three, nine, ten, one, two, 4, 5, 6, 7]
//rotate----------------------
//使用iterator遍历List集合:  dime dollar Penny Quarter nickel
//使用传统for循环进行遍历List集合:  dime dollar Penny Quarter nickel
//使用增强for循环进行遍历List集合:  dime dollar Penny Quarter nickel


/**class2-Set集合*/  //java.util.Set; java.util.TreeSet;
class SetCollection {
    public static void main(String[] args) {
        String[] coins = { "Penny", "nickel", "dime", "Quarter", "dollar" };
        Set<String> set = new TreeSet<String>();  //字符串排序，先大写后小写
        for (int i = 0; i < coins.length; i++) {
            set.add(coins[i]);
        }
        System.out.println("Set集合内容: " + set);
        System.out.println("Set集合的长度: " + set.size());  //
        System.out.println(Collections.min(set));
        System.out.println(Collections.min(set, String.CASE_INSENSITIVE_ORDER)); //String.CASE_INSENSITIVE_ORDER) -> 申明类型是比较器Comparator<String> -> 忽略大小写排序
        System.out.println("-----------");
        System.out.println(Collections.max(set));
        System.out.println(Collections.max(set, String.CASE_INSENSITIVE_ORDER));
        
        //Collections.reverse(set);  // Error -> Collections.reverse()只能反转List的集合
        
        set.remove("dallar");
	    System.out.println("Set<E>.remove(\"dallar\") -> " + set);
        set.remove("dollar");
	    System.out.println("Set<E>.remove(\"dollar\") -> " + set);

        
        //使用iterator遍历Set集合
        System.out.print("使用iterator遍历Set集合: ");
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
           String value = it.next();
           System.out.print(" " + value);
        }
        System.out.println();
        //使用增强for循环遍历Set集合
        System.out.print("使用增强for循环遍历Set集合: ");
        for(String value: set){
           System.out.print(" " + value);
        }
    }
}
//Set集合内容: [Penny, Quarter, dime, dollar, nickel]
//Set集合的长度: 5
//Penny
//dime
//-----------
//nickel
//Quarter
//Set<E>.reverse("dallar") -> [Penny, Quarter, dime, dollar, nickel]
//Set<E>.reverse("dollar") -> [Penny, Quarter, dime, nickel]
//使用iterator遍历Set集合:  Penny Quarter dime nickel
//使用增强for循环遍历Set集合:  Penny Quarter dime nickel


/**class3-Map集合*/  //java.util.Map; java.util.HashMap; java.util.Collection; java.util.Map.Entry;
class MapCollection {
	   public static void main(String[] args) {
		  Map<String, String> map = new HashMap<String, String>();
	      map.put("x1", "1st");
	      map.put("x2", "22nd");
	      map.put("x3", "333rd");
	      map.put("x4", "4444rd");
	      map.put("x5", "55555rd");
	      
	      System.out.println( "Map集合: " + map);
	      System.out.println( "Map集合的长度: " + map.size());  //
	      
	      //Collections.reverse(map);  // Error -> Collections.reverse()只能反转List的集合
	      //System.out.println(Collections.min(map)); // Error -> Collections.min() 只能识别继承或实现于Collection<? extends T>接口的集合
	      
	      map.remove("x3");
	      System.out.println( "map.remove(\"x3\") -> " + map);
	      map.remove("x5", "55555");
	      System.out.println( "map.remove(\"x5\", \"55555\") -> " + map);
	      map.remove("x5", "55555rd");
	      System.out.println( "map.remove(\"x5\", \"55555rd\") -> " + map);
	      //System.out.println( "map.lastKey() -> " + map.lastKey());  //Error -> 只有TreeMap集合中才有firstKey()与lastkey()函数
	      
	      
	      //只遍历map集合的values()
	      System.out.println("只遍历Map集合的values()");
	      Collection<String> cl = map.values();   //实现了Map<K,V>接口的集合内的values()方法都能返回Collection<V>接口
	      Iterator<String> itr = cl.iterator();
	      while (itr.hasNext()) {
	         System.out.println(itr.next());
	      }
	      
	      //传统的遍历map集合的方法: keySet()
	      System.out.println("传统的遍历Map集合的方法: keySet()");
	      Set<String> set1 = map.keySet();    // Map<E, E>.keySet() -> 返回键key的Set<K>集合
	      Iterator<String> it1 = set1.iterator();
	      while (it1.hasNext()) {
	         String key = it1.next();
	         String value = map.get(key);
	         System.out.println(key + " : " + value);
	      }
	      //传统的遍历map集合的方法: entrySet()
	      System.out.println("传统的遍历Map集合的方法: entrySet()");
	      Set<Entry<String, String>> set2 = map.entrySet();   //Map<E, E>.entrySet() -> 返回包含键key与值value的Entry<K,V>集合
	      Iterator<Entry<String, String>> it2 = set2.iterator();
	      while (it2.hasNext()) {
	         Entry<String, String> entry = it2.next();
	         String key = entry.getKey();
	         String value = entry.getValue();
	         System.out.println(key + " : " + value);
	      }
	      //使用增强For循环来遍历map集合方法: keySet()
	      System.out.println("使用增强For循环来遍历Map集合方法: keySet()");
	      Set<String> set3 = map.keySet();  // Map<E, E>.keySet() -> 返回键key的Set<K>集合
	      for (String s : set3) {
	         String key = s;
	         String value = map.get(s);
	         System.out.println(key + " : " + value);
	      }
	      //使用增强For循环来遍历map集合方法: entrySet()
	      System.out.println("使用增强For循环来遍历Map集合方法: entrySet()");
	      Set<Entry<String, String>> set4 = map.entrySet();  //Map<E, E>.entrySet() -> 返回包含键key与值value的Entry<K,V>集合
	      for (Entry<String, String> entry : set4) {
	         String key = entry.getKey();
	         String value = entry.getValue();
	         System.out.println(key + " : " + value);
	      }
	      
	      //使用Enumeration遍历Hashtable()中的keys
	      Hashtable<String, String> ht = new Hashtable<String, String>();
	      ht.put("1", "One");
	      ht.put("2", "Two");
	      ht.put("3", "Three");
	      Enumeration<String> e = ht.keys();
	      System.out.print("使用Enumeration遍历Hashtable.keys() - >");
	      while (e.hasMoreElements()){
	         System.out.print(" " + e.nextElement());
	      }
	   }
}
//Map集合: {x1=1st, x2=22nd, x3=333rd, x4=4444rd, x5=55555rd}
//Map集合的长度: 5
//map.remove("x3") -> {x1=1st, x2=22nd, x4=4444rd, x5=55555rd}
//map.remove("x5", "55555") -> {x1=1st, x2=22nd, x4=4444rd, x5=55555rd}
//map.remove("x5", "55555rd") -> {x1=1st, x2=22nd, x4=4444rd}
//只遍历Map集合的values()
//1st
//22nd
//4444rd
//传统的遍历Map集合的方法: keySet()
//x1 : 1st
//x2 : 22nd
//x4 : 4444rd
//传统的遍历Map集合的方法: entrySet()
//x1 : 1st
//x2 : 22nd
//x4 : 4444rd
//使用增强For循环来遍历Map集合方法: keySet()
//x1 : 1st
//x2 : 22nd
//x4 : 4444rd
//使用增强For循环来遍历Map集合方法: entrySet()
//x1 : 1st
//x2 : 22nd
//x4 : 4444rd
//使用Enumeration遍历Hashtable.keys() - > 3 2 1


/**class4-集合中添加不同类型元素*/
class ManyDiffCollection {
	   public static void main(String[] args) {
		   
	      List<String> lnkLst = new LinkedList<String>();
	      lnkLst.add("element1");
	      lnkLst.add("element2");
	      lnkLst.add("element3");
	      lnkLst.add("element4");
	      displayAll(lnkLst);
	      
	      List<String> aryLst = new ArrayList<String>();
	      aryLst.add("x");
	      aryLst.add("y");
	      aryLst.add("z");
	      aryLst.add("w");
	      displayAll(aryLst);
	      
	      Set<String> hashSet = new HashSet<String>();
	      hashSet.add("set1");
	      hashSet.add("set2");
	      hashSet.add("set3");
	      hashSet.add("set4");
	      displayAll(hashSet);
	      
	      SortedSet<String> treeSet = new TreeSet<String>();
	      treeSet.add("1");
	      treeSet.add("2");
	      treeSet.add("3");
	      treeSet.add("4");
	      displayAll(treeSet);
	      
	      LinkedHashSet<String> lnkHashset = new LinkedHashSet<String>();
	      lnkHashset.add("one");
	      lnkHashset.add("two");
	      lnkHashset.add("three");
	      lnkHashset.add("four");
	      displayAll(lnkHashset);
	      
	      Map<String, String> map1 = new HashMap<String, String>();
	      map1.put("key1", "J");
	      map1.put("key2", "K");
	      map1.put("key3", "L");
	      map1.put("key4", "M");
	      displayAll(map1.keySet());
	      displayAll(map1.values());
	      
	      SortedMap<String, String> map2 = new TreeMap<String, String>();
	      map2.put("key1", "JJ");
	      map2.put("key2", "KK");
	      map2.put("key3", "LL");
	      map2.put("key4", "MM");
	      displayAll(map2.keySet());
	      displayAll(map2.values());
	      
	      LinkedHashMap<String, String> map3 = new LinkedHashMap<String, String>();
	      map3.put("key1", "JJJ");
	      map3.put("key2", "KKK");
	      map3.put("key3", "LLL");
	      map3.put("key4", "MMM");
	      displayAll(map3.keySet());
	      displayAll(map3.values());
	   }
	   static void displayAll(Collection<String> col) {
	      Iterator<String> itr = col.iterator();
	      while (itr.hasNext()) {
	         String str = (String)itr.next();
	         System.out.print(str + " ");
	      }
	      System.out.println();
	   }
}
//element1 element2 element3 element4 
//x y z w 
//set3 set2 set4 set1 
//1 2 3 4 
//one two three four 
//key1 key2 key3 key4 
//J K L M 
//key1 key2 key3 key4 
//JJ KK LL MM 
//key1 key2 key3 key4 
//JJJ KKK LLL MMM 
























