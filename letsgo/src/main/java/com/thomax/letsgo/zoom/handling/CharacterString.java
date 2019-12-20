package com.thomax.letsgo.zoom.handling;

import java.util.Locale;
import java.util.StringTokenizer;


//Java 中 StringBuffer 和 String 是有一定的区别的:
//	首先，String 是被 final 修饰的，他的长度是不可变的，就算调用 String 的concat 方法，那也是把字符串拼接起来并重新创建一个对象，把拼接后的 String 的值赋给新创建的对象，
//	而 StringBuffer 的长度是可变的，调用StringBuffer 的 append 方法，来改变 StringBuffer 的长度，并且，相比较于 StringBuffer，String 一旦发生长度变化，是非常耗费内存的！
//
//String 长度大小不可变
//StringBuffer 和 StringBuilder 长度可变
//StringBuffer 线程安全 StringBuilder 线程不安全 (StringBuffer类多次使用synchronized来进行同步)
//StringBuilder 速度快
public class CharacterString {
	public static void main(String[] args) {
//		StringConcatenate2 example = new StringConcatenate2();
//		example.main(args);
	}
}


/**class1-字符串比较*/
class StringCompareEmp{
	   public static void main(String[] args){
	      String str = "Hello World";
	      String anotherString = "hello world";
	      Object objStr = str;
	 
	      System.out.println( str.compareTo(anotherString) );
	      System.out.println( str.compareToIgnoreCase(anotherString) );  //忽略大小写
	      System.out.println( str.compareTo(objStr.toString()));
	   }
}
//-32
//0
//0


/**class2-查找字符串最后一次出现的位置*/
//Java中第一个字符位置需要从0开始数
class SearchlastString {
	   public static void main(String[] args) {
	      String strOrig = "Hello world ,Hello Runoob";
	      int lastIndex = strOrig.lastIndexOf("Runoob");
	      if(lastIndex == - 1){
	         System.out.println("没有找到字符串 Runoob");
	      }else{
	         System.out.println("Runoob 字符串最后出现的位置： "+ lastIndex);
	      }
	   }
}
//Runoob 字符串最后出现的位置： 19


/**class3-删除字符串中的一个字符*/
class DeleteString {
	   public static void main(String args[]) {
	      String str = "this is Java";
	      System.out.println(removeCharAt(str, 3));
	   }
	   public static String removeCharAt(String s, int pos) {
		  System.out.println(s.substring(1, pos));  //String.substring(A, B) 截取的字符串，起始位置为A，长度= B-A
		  /* r  0123456789
		   *    this is Java
		   */
		  System.out.println(s.substring(pos + 1));  //String.substring(A) 截取的字符串，起始位置为A+1，长度= 字符串总长度-（A+1）
	      return s.substring(0, pos) + s.substring(pos + 1);
	   }
}
//thi
// is Java
//thi is Java


/**class4-字符串替换*/
class StringReplaceEmp{
	   public static void main(String args[]){
	      String str="Hello World He";
	      System.out.println( str.replace( 'H','W' ) );
	      System.out.println( str.replaceFirst("He", "Wa") );
	      System.out.println( str.replaceAll("He", "Ha") );
	   }
}
//Wello World We
//Wallo World He
//Hallo World Ha


/**class5-字符串反转*/
class StringReverseExample{
	   public static void main(String[] args){
	      String string="runoob";
	      String string2="";
	      String reverse = new StringBuffer(string).reverse().toString(); //StringBuffer.reverse().toString()
	      System.out.println("字符串反转前:"+string);
	      System.out.println("字符串反转后:"+reverse);
	      
	      char[] charArray = new char[string.length()];
	      for(int i=0; i<string.length(); i++) {
	    	  charArray[i] = string.charAt(i);
	      }
	      for(int i=string.length()-1; i>=0; i--) {
	    	  string2 += charArray[i];
	      }
	      System.out.println("Custom Thomas: "+string2);
	   }
}
//字符串反转前:runoob
//字符串反转后:boonur
//Custom Thomas: boonur


/**class6-字符串搜索*/
class SearchStringEmp {
	   public static void main(String[] args) {
	      String strOrig = "Google Runoob Taobao";
	      //                0123456789
	      int intIndex = strOrig.indexOf("Runoob");
	      if(intIndex == - 1){
	         System.out.println("没有找到字符串 Runoob");
	      }else{
	         System.out.println("Runoob 字符串位置 " + intIndex);
	      }
	   }
}
//Runoob 字符串位置 7


/**class7-字符串分割*/
class JavaStringSplitEmp {
	   public static void main(String args[]){
	      
	      String str = "www-runoob-com";
	      String[] temp;
	      String delimeter = "-";  // 指定分割字符
	      temp = str.split(delimeter); // 分割字符串
	      // 普通 for 循环
	      for(int i =0; i < temp.length ; i++){
	         System.out.println(temp[i]);
	      }
	 
	      System.out.println("------java for each循环输出的方法-----");
	      String str1 = "www.runoob.com";
	      String[] temp1;
	      String delimeter1 = "\\.";  // 指定分割字符， . 号需要转义
	      temp1 = str1.split(delimeter1); // 分割字符串
	      for(String x :  temp1){
	         System.out.println(x);
	      }
	   }
}
//www
//runoob
//com
//------java for each循环输出的方法-----
//www
//runoob
//com


/**class8-字符串分隔*/  //java.util.StringTokenizer
class StringTokenizerExample {
    public static void main(String[] args) {
 
        String str = "This is String , split by StringTokenizer, created by runoob";
        StringTokenizer st = new StringTokenizer(str); //只有一个参数的话默认按空格来分隔字符串
        System.out.println("----- 通过空格分隔 ------");
        while (st.hasMoreElements()) {
            System.out.println(st.nextElement());
        }
 
        System.out.println("----- 通过逗号分隔 ------");
        StringTokenizer st2 = new StringTokenizer(str, ",");
         while (st2.hasMoreElements()) {
            System.out.println(st2.nextElement());
        }
    }
}
//----- 通过空格分隔 ------
//This
//is
//String
//,
//split
//by
//StringTokenizer,
//created
//by
//runoob
//----- 通过逗号分隔 ------
//This is String 
// split by StringTokenizer
// created by runoob


/**class9-字符串转大小写*/
class StringToUpperCaseEmp {
    public static void main(String[] args) {
        String str = "sTriNg rUnoOb";
        System.out.println("原始字符串转大写: " + str.toUpperCase());
        System.out.println("原始字符串: " + str);
        System.out.println("原始字符串转小写: " + str.toLowerCase());
    }
}
//原始字符串转大写: STRING RUNOOB
//原始字符串: sTriNg rUnoOb
//原始字符串转小写: string runoob


/**class10-测试两个字符串区域是否相等*/
//first_str.regionMatches(11, second_str, 12, 9) 表示将 first_str 字符串从第11个字符"M"开始
//和 second_str 字符串的第12个字符"M"开始逐个比较，共比较 9 对字符（字符串的首字母是第0个字符）
class StringRegionMatch{
	   public static void main(String[] args){
	      String first_str = "Welcome to Microsoft";
	      String second_str = "I work with microsoft";
	      boolean match1 = first_str.
	      regionMatches(11, second_str, 12, 9);
	      boolean match2 = first_str.
	      regionMatches(true, 11, second_str, 12, 9); //第一个参数 true 表示忽略大小写区别
	      System.out.println("区分大小写返回值：" + match1);
	      System.out.println("不区分大小写返回值：" + match2);
	   }
}
//区分大小写返回值：false
//不区分大小写返回值：true


/**class11-字符串性能比较测试*/
class StringComparePerformance{
	   @SuppressWarnings("unused")
	public static void main(String[] args){
	      long startTime = System.currentTimeMillis(); //从1970-1-01 00:00:00.000开始到现在的毫秒
	      for(int i=0;i<10000000;i++){
	         String s1 = "hello";
	         String s2 = "hello";
	         String s3 = "hello";
	      }
	      long endTime = System.currentTimeMillis();
	      System.out.println("通过 String 关键词创建字符串"
	      + " : "+ endTime + " - " + startTime + " = " + (endTime - startTime) 
	      + " 毫秒" );       
	      long startTime1 = System.currentTimeMillis(); //从1970-1-01 00:00:00.000开始到现在的毫秒
	      for(int i=0;i<10000000;i++){
	         String s4 = new String("hello");
	         String s5 = new String("hello");
	         String s6 = new String("hello");
	      }
	      long endTime1 = System.currentTimeMillis();
	      System.out.println("通过 String 对象创建字符串"
	      + " : " + endTime1 + " - " + startTime1 + " = " + (endTime1 - startTime1)
	      + " 毫秒");
	   }
}
//通过 String 关键词创建字符串 : 1520841107585 - 1520841107585 = 0 毫秒
//通过 String 对象创建字符串 : 1520841107601 - 1520841107585 = 16 毫秒


/**class12-字符串优化*/
//1.通过字面量赋值创建字符串时，会优先在常量池中查找是否已经存在相同的字符串，倘若已经存在，栈中的引用直接指向该字符串；
//  倘若不存在，则在常量池中生成一个字符串，再将栈中的引用指向该字符串
//2.而通过new的方式创建字符串时，就直接在堆中生成一个字符串的对象（JDK1.7以后，HotSpot已将常量池从永久代转移到了堆中），栈中的引用指向该对象
//3.对于堆中的字符串对象，可以通过 intern() 方法来将字符串添加到常量池中，并返回指向该常量的引用
class StringOptimization {
    public static void main(String[] args){
        String variables[] = new String[50000];
        for( int i=0;i <50000;i++){
            variables[i] = "s"+i;
        }
        long startTime0 = System.currentTimeMillis();
        for(int i=0;i<50000;i++){
            variables[i] = "hello";
        }
        long endTime0 = System.currentTimeMillis();
        System.out.println("直接使用字符串： "+ (endTime0 - startTime0)  + " ms" );
        long startTime1 = System.currentTimeMillis();
            for(int i=0;i<50000;i++){
            variables[i] = new String("hello");
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("使用 new 关键字：" + (endTime1 - startTime1) + " ms");
        long startTime2 = System.currentTimeMillis();
        for(int i=0;i<50000;i++){
            variables[i] = new String("hello");
            variables[i] = variables[i].intern();  //将字符串添加到常量池中
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("使用字符串对象的 intern() 方法: "
        + (endTime2 - startTime2)
        + " ms");
    }
}
//直接使用字符串： 3 ms
//使用 new 关键字：5 ms
//使用字符串对象的 intern() 方法: 10 ms


/**class13-字符串格式化*/  //java.util.Locale
class StringFormat {
    public static void main(String[] args){
        double e = Math.E;
        System.out.format("%f%n", e);
        System.out.format(Locale.CHINA  , "%-10.4f%n%n", e);  //精确到小数点后四位
    }
}
//2.718282
//2.7183 


/**class14-连接字符串*/
// + 为每个字符串变量赋值，公用一个内值，占用一份内存空间
//StringBuffer每次新建一个新对象，内存分配新的空间
class StringConcatenate {
    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        for(int i=0;i<5000;i++){
            @SuppressWarnings("unused")
            String result = "This is"
            + "testing the"
            + "difference"+ "between"
            + "String"+ "and"+ "StringBuffer";
        }
        long endTime = System.currentTimeMillis();
        System.out.println("使用+操作符连接字符串耗时：" + (endTime - startTime)+ " ms");
        long startTime1 = System.currentTimeMillis();
        for(int i=0;i<5000;i++){
            StringBuffer result = new StringBuffer();
            result.append("This is");
            result.append("testing the");
            result.append("difference");
            result.append("between");
            result.append("String");
            result.append("and");
            result.append("StringBuffer");
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("使用StringBuffer.append(str)连接字符串耗时：" + (endTime1 - startTime1)+ " ms");
    }
}
//使用+操作符连接字符串耗时：0 ms
//使用StringBuffer.append(str)连接字符串耗时：5 ms

class StringConcatenate2 {
    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        String[] strArr = new String[500];
        for(int i=0;i<500;i++){
            String result = "This is";
            strArr[i]= String.valueOf(result.hashCode());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("使用+操作符连接字符串耗时：" + (endTime - startTime)+ " ms");
        System.out.println("strArr[0-2]三个值的hashCode：");
        System.out.println(strArr[0]+"\n"+strArr[1]+"\n"+strArr[2]);
        long startTime1 = System.currentTimeMillis();
        for(int i=0;i<500;i++){
            StringBuffer result = new StringBuffer();
            result.append("This is");
            strArr[i]= String.valueOf(result.hashCode());
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("使用StringBuffer.append(str)连接字符串耗时：" + (endTime1 - startTime1)+ " ms");
        System.out.println("strArr[0-2]三个值的hashCode：");
        System.out.println(strArr[0]+"\n"+strArr[1]+"\n"+strArr[2]);
    }
}
//使用+操作符连接字符串耗时：0 ms
//strArr[0-2]三个值的hashCode：
//318759372
//318759372
//318759372
//使用StringBuffer.append(str)连接字符串耗时：15 ms
//strArr[0-2]三个值的hashCode：
//118352462
//1550089733
//865113938





