package com.thomax.letsgo.handling;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class DataStructure {
	public static void main(String[] args) throws Exception {
//		VectorHandling itp = new VectorHandling();
//		itp.main(args);
	}
}


/**class1-中缀表达式转后缀表达式*/
/*
中缀表达式转后缀表达式的方法：
	1.遇到操作数：直接输出（添加到后缀表达式中）
	2.栈为空时，遇到运算符，直接入栈
	3.遇到左括号：将其入栈
	4.遇到右括号：执行出栈操作，并将出栈的元素输出，直到弹出栈的是左括号，左括号不输出。
	5.遇到其他运算符：加减乘除：弹出所有优先级大于或者等于该运算符的栈顶元素【栈内的栈顶运算符>=遇到的运算符，就弹出】，然后将该运算符入栈
	6.最终将栈中的元素依次出栈，输出。
	
后缀表达式的计算机求值,与前缀表达式类似，只是顺序是从左至右：
   从左至右扫描表达式，遇到数字时，将数字压入堆栈，遇到运算符时，弹出栈顶的两个数，用运算符对它们做相应的计算（次顶元素 op 栈顶元素），
   并将结果入栈；重复上述过程直到表达式最右端，最后运算得出的值即为表达式的结果。
例如后缀表达式“3 4 + 5 × 6 -”：
	1.从左至右扫描，将3和4压入堆栈；
	2.遇到+运算符，因此弹出4和3（4为栈顶元素，3为次顶元素，注意与前缀表达式做比较），计算出3+4的值，得7，再将7入栈；
	3.将5入栈；
	4.接下来是×运算符，因此弹出5和7，计算出7×5=35，将35入栈；
	5.将6入栈；
	6.最后是-运算符，计算出35-6的值，即29，由此得出最终结果。
*/
class InToPost{

	   private Stack theStack;
	   private String input;
	   private String output = "";
	   
	   public InToPost() {
		   //便于不用传递参数来new此类
	   }
	   public InToPost(String in) {
	      input = in;
	      int stackSize = input.length();
	      theStack = new Stack(stackSize);
	   }
	   
	   public static void main(String[] args) throws IOException {
		      String input = "1+2*4/5-7+3/6";
		      String output;
		      InToPost theTrans = new InToPost(input);
		      output = theTrans.doTrans(); 
		      System.out.println("Infix: " + input + '\n');
		      System.out.println("Postfix: " + output + '\n');
	   }
	   
	   public String doTrans() {
	      for (int j = 0; j < input.length(); j++) {
	         char ch = input.charAt(j);
	         switch (ch) {
	            case '+': 
	            case '-':
	            gotOper(ch, 1); 
	            break; 
	            case '*': 
	            case '/':
	            gotOper(ch, 2); 
	            break; 
	            case '(': 
	            theStack.push(ch);
	            break;
	            case ')': 
	            gotParen(ch); 
	            break;
	            default: 
	            output = output + ch; 
	            break;
	         }
	      }
	      while (!theStack.isEmpty()) {
	         output = output + theStack.pop();
	      }
	      //System.out.println(output);
	      return output; 
	   }
	   
	   public void gotOper(char opThis, int prec1) {
	      while (!theStack.isEmpty()) {
	         char opTop = theStack.pop();
	         if (opTop == '(') {
	            theStack.push(opTop);
	            break;
	         }
	         else {
	            int prec2;
	            if (opTop == '+' || opTop == '-')
	            prec2 = 1;
	            else
	            prec2 = 2;
	            if (prec2 < prec1) { 
	               theStack.push(opTop);
	               break;
	            }
	            else
	            output = output + opTop;
	         }
	      }
	      theStack.push(opThis);
	   }
	   
	   public void gotParen(char ch){ 
	      while (!theStack.isEmpty()) {
	         char chx = theStack.pop();
	         if (chx == '(') 
	         break; 
	         else
	         output = output + chx; 
	      }
	   }
	   
	   class Stack {
	      private int maxSize;
	      private char[] stackArray;
	      private int top;
	      public Stack(int max) {
	         maxSize = max;
	         stackArray = new char[maxSize];
	         top = -1;
	      }
	      public void push(char j) {
	         stackArray[++top] = j;
	      }
	      public char pop() {
	         return stackArray[top--];
	      }
	      public char peek() {
	         return stackArray[top];
	      }
	      public boolean isEmpty() {
	         return (top == -1);
	     }
	   }
}
/*Infix: 1+2*4/5-7+3/6 = -3.9
 *Postfix: 124*5/+7-36/+
   Index:   -0-  -1-  -2-  -3-
  Stack1:    1
  Stack2:    1    2
  Stack3:    1    2    4
  Stack4:    1    2    4    *
  Stack5:    1    8
  Stack6:    1    8    5
  Stack7:    1    8    5    /
  Stack8:    1   1.6
  Stack9:    1   1.6   +
  Stack10:  2.6
  Stack11:  2.6   7
  Stack12:  2.6   7    -
  Stack13: -4.4
  Stack14: -4.4   3
  Stack15: -4.4   3    6
  Stack16: -4.4   3    6    /
  Stack17: -4.4  0.5
  Stack18: -4.4  0.5   +
  Stack19: -3.9
 */


/**class2-链表（LinkedList）*/  //java.util.LinkedList;
class LinkedListHandling {
    public static void main(String[] args) {
        LinkedList<String> lList = new LinkedList<String>();
        lList.add("1");
        lList.add("2");
        lList.add("3");
        lList.add("4");
        lList.add("5");
        lList.add("6");
        System.out.println(lList);
        lList.set(4, "2");
        System.out.println("lList.set(4, \"2\") -> " + lList);
        System.out.println("链表的第一个元素是：" + lList.getFirst());   //LinkedList<E>.getFirst()
        System.out.println("链表的最后一个元素是：" + lList.getLast());  //LinkedList<E>.getLast()
        System.out.println("元素\"2\"第一次出现的位置：" + lList.indexOf("2"));   //LinkedList<E>.indexOf(E e)
        System.out.println("元素\"2\"最后一次出现的位置："+ lList.lastIndexOf("2"));   //LinkedList<E>.lastIndexOf(E e)
        lList.addFirst("0");  //LinkedList<E>.addFirst(E e)
        System.out.println("addFirst(\"0\") -> " + lList);
        lList.addLast("7");   //LinkedList<E>.addLast(E e)
        System.out.println("addLast(\"7\") -> " + lList);
        System.out.println("lList.subList(2, 4) -> " + lList.subList(2, 4));  //比如下标0123456中的范围23，fromIndex=2，toIndex=4
        lList.subList(2, 4).clear();  //LinkedList<E>.subList(fromIndex, toIndex).clear()
        System.out.println("clear() -> " + lList);
        lList.removeFirst();  //LinkedList<E>.removeFirst()
        System.out.println("removeFirst() -> " + lList);
        lList.removeLast();   //LinkedList<E>.removeLast()
        System.out.println("removeLast() -> " + lList);
    }
}
//[1, 2, 3, 4, 5, 6]
//lList.set(4, "2") -> [1, 2, 3, 4, 2, 6]
//链表的第一个元素是：1
//链表的最后一个元素是：6
//元素"2"第一次出现的位置：1
//元素"2"最后一次出现的位置：4
//addFirst("0") -> [0, 1, 2, 3, 4, 2, 6]
//addLast("7") -> [0, 1, 2, 3, 4, 2, 6, 7]
//lList.subList(2, 4) -> [2, 3]
//clear() -> [0, 1, 4, 2, 6, 7]
//removeFirst() -> [1, 4, 2, 6, 7]
//removeLast() -> [1, 4, 2, 6]


/**class3-向量（Vector）*/  //java.util.Vector; java.util.Collections;
class VectorHandling {
	   public static void main(String[] args) {
	      Vector<String> v = new Vector<String>();
	      v.add("X");  //Vector<E>.add(E e)
	      v.add("M");
	      v.add("D");
	      v.add("A");
	      v.add("O");
	      System.out.println(v);
	      Collections.sort(v);  //Collections.sort(Vector<E>)
	      System.out.println("Collections.sort(Vector<E>) -> " + v);
	      
	      int index2 = Collections.binarySearch(v, "D");  //Collections.binarySearch(Vector<E>, e)
	      System.out.println("元素\"D\"的索引值为 : " + index2);
	      
	      Collections.swap(v, 1, 3);  //Collections.swap(Vector<E>, indexFrom, indexTo) 这里的index是下标值，indexTo不用加1
	      System.out.println("swap(v, 1, 3) -> " + v);
	      
	      int index = Collections.binarySearch(v, "D");  //Collections.binarySearch(Vector<E>, e)
	      System.out.println("元素\"D\"的索引值为 : " + index + " ，因为二分查找需要使用正向排序的向量");
	      
	      Object obj = Collections.max(v);   //Collections.max(Vector<E>)
	      Object obj2 = Collections.min(v);  //Collections.min(Vector<E>)
	      System.out.println("此向量中的最大元素为 : " + obj);
	      System.out.println("此向量中的最小元素为 : " + obj2);
	   }
}
//[X, M, D, A, O]
//Collections.sort(Vector<E>) -> [A, D, M, O, X]
//元素"D"的索引值为 : 1
//swap(v, 1, 3) -> [A, O, M, D, X]
//元素"D"的索引值为 : -2 ，因为二分查找需要使用正向排序的向量
//此向量中的最大元素为 : X
//此向量中的最小元素为 : A


/**class3-栈原理的实现*/
//利用long[]数组实现一个栈的原理，pop()返回最大下标的数组的值，实现后进先出
class RealizeStack {
	   private int maxSize;
	   private long[] stackArray;
	   private char[] stackArrayChar;
	   private int top;
	   
	   public RealizeStack() {
		 //便于不用传递参数来new此类   
	   }
	   public RealizeStack(int s, String type) {
	      maxSize = s;
	      top = -1;
	      if(type.equals("long")) {
	    	  stackArray = new long[maxSize];
	      }else if(type.equals("char")) {
	    	  stackArrayChar = new char[maxSize];
	      }
	   }
	   
	   public static void main(String[] args) {
		  RealizeStack theStack = new RealizeStack(10, "long"); 
	      theStack.push(10);
	      theStack.push(20);
	      theStack.push(30);
	      theStack.push(40);
	      theStack.push(50);
	      while (!theStack.isEmpty()) {
	         long value = theStack.pop();
	         System.out.print(value);
	         System.out.print(" ");
	      }
	   }
	   
	   public void push(long j) {
	      stackArray[++top] = j;
	   }
	   public void push(char j) {
	      stackArrayChar[++top] = j;
	   }
	   public long pop() {
	      return stackArray[top--]; //栈的核心部分: 后进先出
	   }
	   public char popChar() {
	      return stackArrayChar[top--];
	   }
	   public long peek() {
	      return stackArray[top];
	   }
	   public boolean isEmpty() {
	      return (top == -1);
	   }
	   public boolean isFull() {
	      return (top == maxSize - 1);
	   }
}
//50 40 30 20 10 


/**class4-压栈出栈的方法(栈原理)实现字符串反转*/
class StringReverserThroughStack {
	   private String input;
	   private String output;
	   
	   public StringReverserThroughStack() {
		 //便于不用传递参数来new此类
	   }
	   public StringReverserThroughStack(String in) {
	      input = in;
	   }
	   
	   public static void main(String[] args) throws IOException {
	      String input = "Let's go to be a winner";
	      String output;
	      StringReverserThroughStack theReverser = new StringReverserThroughStack(input);
	      output = theReverser.doRev();
	      System.out.println("反转前： " + input);
	      System.out.println("反转后： " + output);
	   }
	   
	   public String doRev() {
	      int stackSize = input.length(); 
	      RealizeStack theStack = new RealizeStack(stackSize, "char"); 
	      for (int i = 0; i < input.length(); i++) {
	         char ch = input.charAt(i); 
	         theStack.push(ch); 
	      }
	      output = "";
	      while (!theStack.isEmpty()) {
	         char ch = theStack.popChar(); 
	         output = output + ch; 
	      }
	      return output;
	   }
}
//反转前： Let's go to be a winner
//反转后： renniw a eb ot og s'teL


/**class5- 队列（Queue）用法*/  //java.util.Queue;
//队列是一种特殊的线性表，它只允许在表的前端进行删除操作，而在表的后端进行插入操作。
//LinkedList类实现了Queue接口，因此我们可以把LinkedList当成Queue来用。
class QueueHandling {
    public static void main(String[] args) {
        //add()和remove()方法在失败的时候会抛出异常(不推荐)
        Queue<String> queue = new LinkedList<String>();
        //添加元素
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        queue.offer("d");
        queue.offer("e");
        for(String q : queue){
            System.out.println(q);
        }
        System.out.println("===");
        System.out.println("poll="+queue.poll()); //Queue<E>.poll()返回第一个元素，并在队列中删除
        for(String q : queue){
            System.out.println(q);
        }
        System.out.println("===");
        System.out.println("element="+queue.element()); //Queue<E>.element()返回第一个元素
        for(String q : queue){
            System.out.println(q);
        }
        System.out.println("===");
        System.out.println("peek="+queue.peek()); //Queue<E>.peek()返回第一个元素
        for(String q : queue){
            System.out.println(q);
        }
    }
}
//a
//b
//c
//d
//e
//===
//poll=a
//b
//c
//d
//e
//===
//element=b
//b
//c
//d
//e
//===
//peek=b
//b
//c
//d
//e



















