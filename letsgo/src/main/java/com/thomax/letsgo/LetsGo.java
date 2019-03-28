package com.thomax.letsgo;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//import static com.atguigu.aaa.StaticClass.*; // 静态导入：导入一个类中所有的静态内容


/**
 * <i>Lets go</i>
 * 涵盖JavaSE所有功能
 * @author _thomas
 * @version 2.0
 */
@SuppressWarnings("unused")
public class LetsGo {
	
    /*-------------------------------------------
    |              V A R I A B L E S            |
    ============================================*/
	
	private static final Exception Hello = null;
	private static final Animal Cat = null;
	private static int num1 = 2;
	private int num2 = 2;
	static {num1 = 3;}
	Object obj1,obj2;
	int hello;  //类变量会有一个初始值，而方法局部变量定义的基础类型没有初始值
	
    /*-------------------------------------------
    |           C O N S T R U C T O R           |
    ============================================*/
	
	public LetsGo() {
		num1++;
		num2++;
		if(num1 == 4) System.out.println("Lets");
		if(num2 == 3) {
			System.out.println("Go");
		}	
	}
	
	/*-------------------------------------------
    |                 M E T H O D               |
    ============================================*/
	
	/**
	 * <i>Examples类的reverse方法</i>                           
	 * 开始执行！
	 * @param arr from input int array
	 * @return result               
	 * @throws Exception
	 * @see {@link Supplier#print2()}
	 * @since 1.0                             
	 */
	public static int[] reverse(int[] arr){
        int[] result = new int[arr.length];
        for (int i = 0,j=result.length-1; i < arr.length; i++,j--) {
            result[j] = arr[i];
        }
        return result;  
	}
	
	//冒泡排序需要遍历的总次数= (arr.length-1) + (arr.length-2) + ... + 1
	//原理就是将最左侧的数值换到最右侧去(最右侧一定是最大值)，依次再将最左侧值换到右侧倒数第二个...
	public void calcSort() {
		int arr[] = { 26, 15, 29, 66, 99, 88, 36, 77, 111, 1, 6, 8, 8 };
		for (int x = 0; x < arr.length - 1; x++) {
			for (int y = 0; y < arr.length - x - 1; y++) {
				if (arr[y] > arr[y + 1]) {
					int tempArr = arr[y];
					arr[y] = arr[y + 1];
					arr[y + 1] = tempArr;
				}
			}
			for (int k = 0; k < arr.length; k++) {
				System.out.print(arr[k] + " ");
				System.out.println();
			}
		}
	}
	
	void calcSort(int[] arrIn) {
		if (arrIn.length == 0) {
			int[] arr = { 26, 15, 29, 66, 99, 88, 36, 77, 111, 1, 6, 8, 8 };
			for (int x = 0; x < arr.length - 1; x++) {
				for (int y = 0; y < arr.length - x - 1; y++) {
					if (arr[y] > arr[y + 1]) {
						int tempArr = arr[y];
						arr[y] = arr[y + 1];
						arr[y + 1] = tempArr;
					}
				}
			}
			for (int x = 0; x < arr.length; x++) {
				System.out.print(arr[x] + " ");
			}
		} else {
			for (int x = 0; x < arrIn.length - 1; x++) {
				for (int y = 0; y < arrIn.length - x - 1; y++) {
					if (arrIn[y] > arrIn[y + 1]) {
						int tempArr = arrIn[y];
						arrIn[y] = arrIn[y + 1];
						arrIn[y + 1] = tempArr;
					}
				}
			}
			for (int x = 0; x < arrIn.length; x++) {
				System.out.print(arrIn[x] + " ");
			}
		}
	}
	
    public int factorial(int num){
		if (num == 1) return 1;
		return num * factorial(num - 1);
	}
	
    /*------------------------------------------------------------------
    |  内部类分为四种：成员内部类、局部内部类、匿名内部类、静态内部类  |
    ===================================================================*/
    
	/**成员内部类*/
	class hello{
		//int hello = num2； -> Error  -> 内部类不能访问外部类的成员变量num2
	}
	/**静态内部类*/
	static class Inner{}
	
	/**局部内部类*/
	public Comparator<Object> getComparator(){
        class MyComparator implements Comparator<Object>{
        	public int compare(Object o1, Object o2){
                return 10;
            }
        }
        //return一个实现了Comparator接口中compare()方法的MyComparator类对象
        return new MyComparator();
    }
	
	/**匿名内部类:
	 * java匿名内部类一定是在new的后面，用其隐含实现一个接口或实现一个类，没有类名，根据多态，我们使用其父类名。
	 * 因他是局部内部类，那么局部内部类的所有限制都对其生效。匿名内部类是唯一一种无构造方法类。大部分匿名内部类是用于接口回调用的。
	 * 匿名内部类在编译的时候由系统自动起名Out$1.class。如果一个对象编译时的类型是接口，那么其运行的类型为实现这个接口的类。
	 * 因匿名内部类无构造方法，所以其使用范围非常的有限。当需要多个对象时使用局部内部类，因此局部内部类的应用相对比较多。匿名内部类中不能定义构造方法。
	 * 如果一个对象编译时的类型是接口，那么其运行的类型为实现这个接口的类	
	 */
    public Comparator<Object> getComparotor2(){
        Comparator<Object> com = new Comparator<Object>(){
        	public int compare(Object o1, Object o2){
                return 20;
            }
        };
        //return一个重写了compare()的Comparator接口对象
        return com;  
    }
    public Comparator<Object> getComparotor3(){
    	//return一个实现了compare()的Comparator接口对象 ->第二种写法
        return new Comparator<Object>(){
        	public int compare(Object o1, Object o2){
                return 20;
            }
        };
    }
    
}


/**class0 - variables*/
class VariablesClass{
	//byte —> short,char —> int,float —> long,double
	//一个字节（0000 0000）
	byte byte1=127, byte2=-128; //1个字节 = 8位
	Byte byteValue = new Byte((byte)123); //Byte default value = 0
	
	short short1=32767, short2=-32768; //2个字节 = 16位
	Short shortValue = new Short((short)1234); //Short default value = 0
	
	char char1='A', char2='B'; //2个字节 = 16位
	Character charValue = new Character('h'); //Character default value = '\u0000'
	
	int int1=2147483647, int2=-2147483648, int_default=0; //4个字节 = 32位
	Integer integerValue = new Integer(12345); //Integer default value = 0

	float float1=3.402823e+38f, float2=1.401298e-45f; //4个字节 = 32位 ,float精度为6~7位，能保证6位为绝对精确，7位一般也是正确的，8位就不一定了(但不是说8位就绝对不对了)
	Float floatValue = new Float(123.456f); //Float default value = 0.0f
	
	long long1=9223372036854774807L, long2=-9223372036854774808L; //8个字节 = 64位
	Long longValue = new Long(123456L); //Long default value = 0L

	double double1=1.797693e+308d, double2=4.9000000e-324d; //8个字节 = 64位 , double精度是15~16，能保证15，一般16
	double boubleJava = 123.456; //Java里面的默认浮点类型为double，所以double有时候可以不用在结尾加一个d
	Double doubleValue = new Double(123.456d); //Double default value = 0.0d
	
	boolean boolean1=true, boolean2=false;
	Boolean booleanValue = new Boolean(false); //Boolean default value = false ,占多少个字符由编译环境而定
	
	String strOne="Lets", strTwo="Go";
	String stringValue = new String("stringValue");
	StringBuffer strBuffer = new StringBuffer("bufferValue");
	StringBuilder strBuilder = new StringBuilder("builderValue");
	
	final String STR_FINAL = "endless summer";
	String[] names = {"David","Robot","Thomas"};
	char[] helloArray = {'n', 'o', 'o', 'b'};
	int[] numbers = {10, 40, 30, 20};
}

/**class0 - enum*/
//Java 创建枚举类型要使用 enum 关键字，隐含了所创建的类型都是 java.lang.Enum 类的子类
enum EnumClass {
    lamborghini(900),tata(2),Audi(50),fiat(15),honda(12);
    private int price;
	EnumClass(int p) {
        price = p;
    }
    int getPrice() {
        return price;
    } 
}

class UseEnum {
	public void runEnum() {
		System.out.println("所有汽车的价格：");
        for (EnumClass ec : EnumClass.values()) {
        	System.out.println(ec + " 需要 " + ec.getPrice() + " 千美元。");
        }
	}
}

enum Color {  
    RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);  

    private String name;  
    private int index;  
 
    private Color(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
 
    public static String getName(int index) {  
        for (Color c : Color.values()) {  
            if (c.getIndex() == index) {  
                return c.name;  
            }  
        }  
        return null;  
    }  
    // get set 方法  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public int getIndex() {  
        return index;  
    }  
    public void setIndex(int index) {  
        this.index = index;  
    }   
    @Override  
    public String toString() {  
        return this.index+"_"+this.name;  
    }
}

//用枚举实现接口，所有的枚举都继承自java.lang.Enum类。由于Java 不支持多继承，所以枚举对象不能再继承其他类
interface Behaviour {  
    void print();  
    String getInfo();  
}

enum Color2 implements Behaviour{  
    RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);  

    private String name;  
    private int index;  
 
    private Color2(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }   
    @Override  
    public String getInfo() {  
        return this.name;  
    }    
    @Override  
    public void print() {  
        System.out.println(this.index+":"+this.name);  
    }  
}

//使用接口组织枚举
interface Food {  
	enum Color3 implements Behaviour{  
	    RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);  
 
	    private String name;  
	    private int index;  
 
	    private Color3(String name, int index) {  
	        this.name = name;  
	        this.index = index;  
	    }   
	    
	    @Override  
	    public String getInfo() {  
	        return this.name;  
	    }    
	    @Override  
	    public void print() {  
	        System.out.println(this.index+":"+this.name);  
	    }  
	}   
   enum Dessert implements Food{  
       FRUIT, CAKE, GELATO  
   }  
}


/**class1*/
class GetClass{
    public char[] num2;
	@SuppressWarnings("unused")
	private volatile static GetClass instance;
    final  int int4;
    long num1;
    String str1;
    
    public GetClass(long long4) {
    	int4 = (int)long4;
    }
    public GetClass() {
    	int4 = 2;
    }
    public GetClass(String str3) {
    	int4 = 6;
    }
}

/**class2*/
class CalcArray{
	
	//Array
	int[][] intArray;
	String[][][] strArray= new String[2][][];
	
	public CalcArray() {
		intArray = new int[][]{{1,2},{4,5,6}}; //new的时候不能在数组[]内直接写数字，不然会被识别为具体的那个组
		//intArray.length == 2;
				
	    strArray[0] = new String[2][];
	    strArray[1] = new String[1][];
	    strArray[0][0] = new String[3];
	    strArray[0][1] = new String[2];
	    strArray[1][0] = new String[3];
	    strArray[0][0][0] = "000";
	    strArray[0][0][1] = "001";
	    strArray[0][0][2] = "002";
	    strArray[0][1][0] = "010";
	    strArray[0][1][1] = "011";
	    strArray[1][0][0] = "100";
	    strArray[1][0][1] = "101";
	    strArray[1][0][2] = "102";
	}
	
	public void getArray() {
	    //foreach
	    for(String[][] strStrStr:strArray) {
	    	for(String[] strStr:strStrStr) {
	    		for(String str:strStr) {
	    			System.out.println(str);
	    		}
	    	}
	    }
	    
	    //for loop
	    for(int x = 0; x < strArray.length; x++) {
	    	String[][] strStrStr = strArray[x];
	    	for(int y = 0; y < strStrStr.length; y++) {
	    		String[] strStr = strStrStr[y];
	    		for(int z = 0; z < strStr.length; z++) {
	    			String str = strStr[z];
	    		    System.out.println(str);
	    		}
	    	}
	    }
	}
    
}

/**class3*/
//abstract 和 static 不能同时使用
//abstract 和 final 不能同时使用
//abstract 和 private 不能同时使用
//当子类继承父类后，若重写了父类中所有的抽象方法，该类为具体类，可以创建实例
//当子类继承父类后，若没有重写父类中所有的抽象方法，该类必须是抽象类，不可以创建实例
abstract class SuperClass {
	abstract void superFun();
}

/**class4*/
class ExtClass extends SuperClass{
	@Override
	void superFun() {
		System.out.println("");
	} 
}

/**class5*/
class SecClass extends GetClass{
	//////
}


/**class6*/
// Object.finalize() -> 清除回收对象前的操作
class Cake extends Object {  
	  private int id;  
	  public Cake(int id) {  
	    this.id = id;  
	    System.out.println("Cake Object " + id + "is created");  
	  }  
	    
	  protected void finalize() throws Throwable {
	    super.finalize();  
	    System.out.println("Cake Object " + id + "is disposed");  
	  }  
}

/**class7*/
class Buffered {
	
	public void reader() throws IOException {
		char c;
		// use System.in create BufferedReader 
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("输入字符, 按下 'q' 键退出。");
		
		// read string
		do {
		   c = (char) br.read();
		   System.out.println(c);
		} while(c != 'q');
		
	}
	
}

/**class8*/
class ReadLine{
	public void read() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String st;
		do {
			st = br.readLine();
			System.out.println(st);
		}while(!(st.equals("end")));
	}
}

/**class9*/
class ExceptionTest{
	public int returnTest() {
		try {
			@SuppressWarnings("unused")
			int x = 10 / 0;
		} catch (Exception e) {
			System.out.println("catch is begin");
			System.out.println(e.toString());
			return 1;
		} finally {
			System.out.println("finally is begin");
			//return 2;
		}
		return 2;
	}
	
	public void FindFileTest() {
		//检查异常1.打开文件
        FileReader fr=null;
        try {
            fr=new FileReader("d:\\aa.text");
            // 在出现异常的地方，下面的代码的就不执行
            System.out.println("aaa");
        } catch (Exception e) {
            System.out.println("进入catch");
            // 文档读取异常
            // System.exit(-1);
            System.out.println("message="+e.getLocalizedMessage());  //没有报哪一行出错
            e.printStackTrace();   // 打印出错异常还出现可以报出错先异常的行
        }
        // 这个语句块不管发生没有发生异常，都会执行
        // 一般来说，把需要关闭的资源，文件，连接，内存等
        finally
        {
            System.out.println("进入finally");
            if(fr!=null);
            {
                try {
                    fr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("OK");
	}

}

/**class10*/
class OverTest{
	
	public String hello(String st) {
		st = "hello";
		return st;
	}
	
	//overload error
//	public int hello(String st) {
//		return 12;
//	}
	
}


/**class11*/
class Animal {  
	public int intV;
	private String hello1 = "hello";
    public Animal() {
    	System.out.println("Animal");
	}
    public Animal(String str) {
    	System.out.println(str);
	}
    
    public void move() {
    	intV = 10;
    	System.out.println("Animal.move: " + intV);
    }
    public String getHello1() {
    	return this.hello1;
    }
}

class Cat extends Animal{
	public long intV;
	public String hello2;
	public Cat() {
		super(); //调用父类构造器，必须写在构造器中可执行代码的首行
		System.out.println("Cat");
	}
	public Cat(String str){
		super(str); //若父类中没有提供无参构造器，子类所有构造器中必须显示调用父类有参构造器（无论如何必须保证创建子类对象前，先初始化父类）
		System.out.println("Cat");
	}
	public void setHello2(){
		//hello1 = super.hello1;   //-> Error
		hello2 = super.getHello1();
	}
	public void eat() {
		System.out.println("Cat.eat");
	}
	public void work() {
	    @SuppressWarnings("unused")
		int intC = 2;
		System.out.println("Cat.work");
	}
//    public void move() {
//    	intV = 20;
//    	System.out.println("Cat.move: " + intV);
//    }
}

class LittleCat extends Cat{
	public LittleCat() {
		super();
	}
	public void move() {
		System.out.println("LittleCat.work" + intV);
	}
	public void work() {
	    @SuppressWarnings("unused")
		int intC = 4;
		System.out.println("LittleCat.work");
	}
}

class extendsArray{
	public void classArray() {
		Animal[] animal = new Animal[3];//多态数组
		animal[0] = new Animal();
		animal[1] = new Cat();
		animal[2] = new LittleCat();
		for(int i = 0; i < animal.length; i++){
			animal[i].move();//虚拟方法调用
			//animal[i].work(); -> Error -> 用父类申明的Animal中没有work()
	    }
	}
}


/**class12*/
// 文件名 : Employee.java
class Employee{
   public String name;
   private String address;
   private int number;
   public Employee(String name, String address, int number) {
      System.out.println("Employee 构造函数");
      this.name = name;
      this.address = address;
      this.number = number;
   }
   public void mailCheck() {
	  System.out.println("ThomasTest");
      System.out.println("邮寄支票给： " + this.name
       + " " + this.address);
   }
   public String toString() {
      return name + " " + address + " " + number;
   }
   public String getName() {
      return name;
   }
   public String getAddress() {
      return address;
   }
   public void setAddress(String newAddress) {
      address = newAddress;
   }
   public int getNumber() {
     return number;
   }
}

// 文件名 : Salary.java
class Salary extends Employee{
   private double salary; // 全年工资
   public Salary(String name, String address, int number, double salary) {
       super(name, address, number);
       setSalary(salary);
   }
   public void mailCheck() {
       System.out.println("Salary 类的 mailCheck 方法 ");
       System.out.println("邮寄支票给：" + getName()
       + " ，工资为：" + salary);
   }
   public double getSalary() {
       return salary;
   }
   public void setSalary(double newSalary) {
       if(newSalary >= 0.0) {
          salary = newSalary;
       }
   }
   public double computePay() {
      System.out.println("计算工资，付给：" + getName());
      return salary/52;
   }
}


/**class13*/
abstract class Father{
	int hello = 22;
	public void print() {
		System.out.println(hello);
	}
	
	public abstract int sayHello();
}

class Son extends Father {
	String hello = "hello";

	public void print() {
		System.out.println(hello);
	}

	@Override
	public int sayHello() {
		return 0;
	}
}


/** class14 */
abstract class Animal1 {
	private int age;

	public Animal1(int age) {
		this.age = age;
		System.out.println("初始化Animal");
	}

	public void move() {
		System.out.println("跑步数：" + this.age);
	}
}

abstract class Dog extends Animal1 {
	public Dog(int age) {
		super(age);// 去掉会报异常,父类带参数的构造函数需要初始化
		System.out.println("初始化Dog");
	}

}


/**interface1*/
interface InterfaceTest{
//	可以把接口理解为一个特殊的抽象类，因为接口中只能定义“全局静态常量”和“抽象方法”
//    int NUM = 10;//public static  final
//    void fly();//public abstract
/**default void fly() {}  //public default void fly(){} 
 * default不是访问修饰符，与定义类的default不一致
 */
//	若类实现了接口中所有的抽象方法，该类为具体类，可以创建实例
//  若实现类没有实现接口中所有的抽象方法，该类必须是抽象类，不能创建实例
	String hello = "23";
	String hello2 = "23";
	void interTest1();   //public abstract void interTest1();
	static void interTest2() {   //接口的静态方法，必须有{}方法体  public static void interTest2(){}
		System.out.println("helloStatic");
	}
	default void interTest5() {   //接口的默认方法，必须有{}方法体  public default void interTest5(){}
		System.out.println("helloDefault");
	}
}

/**interface2*/
interface InterfaceTest2{
	int hello = 12;
	int interTest3();
	void interTest4();
}

/**class implements interface*/
class MyInterface implements InterfaceTest, InterfaceTest2{
	public void interTest1() {}
	public void interTest2() {}
	public int interTest3() {
		return 1;
	}
	public void interTest4() {}
	
	//重写接口默认方法
	public void interTest5() {
		System.out.println("overwrite MyInterface.interTest5");
	}
		
}

/**abstract class*/
abstract class MyAbstractInterface implements InterfaceTest, InterfaceTest2{
	//  抽象类无需要实现接口内所有方法
	//非抽象类  需要实现接口内所有方法
}

/**interface3*/
interface InterfaceTest3 extends InterfaceTest{
	//String hello = "45";
	String hello2 = "45";
	void interTest1();
	
	//重写接口默认方法
	default void interTest5() {
		System.out.println("overwrite interfaceTest3.interTest5");
	}
}


/**class15*/
class EnumVector{
	Enumeration<String> days;
	Vector<String> dayNames = new Vector<String>();
	
	public void exec() {
		dayNames.add("Sunday");
		dayNames.add("Monday");
		dayNames.add("Tuesday");
		dayNames.add("Wednesday");
		dayNames.add("Thursday");
		dayNames.add("Friday");
		dayNames.add("Saturday");
		days = dayNames.elements();
		while (days.hasMoreElements()){
			System.out.println(days.nextElement()); 
		}
	}
  
	public void vector() {
  	  	// initial size is 3, increment is 2
  	  	//向量初始尺寸=3，当在向量中新增元素超过向量尺寸时向量的递增长度=2
		//枚举对象中有has与next方法，但是向量对象中没有，所以需要输出向量所有内容需要Enumeration vEnum = v.elements();
		Vector<Object> v = new Vector<Object>(3, 2); //Vector的默认扩容是定义时的两倍：Vector(3, 2) -> 增加元素个数=4时，容量变为6
		System.out.println("Initial size: " + v.size());
		System.out.println("Initial capacity: " + v.capacity());
		v.addElement(1);
		v.addElement(new Integer(2));
		v.addElement(new String("3"));
		v.addElement(new Integer(4));
		v.addElement(new Integer(5));
		v.addElement(new Integer(6));
		System.out.println("Capacity after four additions: " + v.capacity());

		v.addElement(new Double(5.45));
		System.out.println("Current capacity: " + v.capacity());
		v.addElement(new String("testString"));
		v.addElement(new Integer(7));
		System.out.println("Current capacity: " + v.capacity());
		v.iterator();

		Enumeration<Object> vEnum = v.elements();
		System.out.println("Elements in vector:");
		while(vEnum.hasMoreElements()) {
         System.out.print(vEnum.nextElement() + " ");
		}
	}
}


/**class16*/
class StackObject{
	Stack<Object> st = new Stack<Object>();
	
	public void runObject() {
        System.out.println("stack: " + st);
        st.push("hi");
        st.push(66);
        System.out.println("stack: " + st);
        st.push("hello");
        System.out.println("stack: " + st);
        Object a = st.pop();
        System.out.println(a);
        EnumVector enumV = new EnumVector();
        st.push(enumV);
        System.out.println(st);
        //stack: [hi, 66, hello]
      	//hello
      	//[hi, 66, LetsGo.EnumVector@70dea4e]
	}
}


/**class17*/
class HashObject{
	// Create a hash map
    Hashtable<Object, Object> balance = new Hashtable<Object, Object>();
    Enumeration<Object> names;
    String str;
    double bal;
    public void runHash() {
    	balance.put("Zara", new Double(3434.34));
        balance.put("Mahnaz", new Double(123.22));
        balance.put("Ayan", new Double(1378.00));
        balance.put("Daisy", new Double(99.22));
        balance.put("Qadir", new Double(-19.08));
        
        // Show all balances in hash table.
        names = balance.keys();
        while(names.hasMoreElements()) {
           str = (String) names.nextElement();
           System.out.println(str + ": " + balance.get(str));
        }
        
        System.out.println();
        Enumeration<Object> values = balance.elements();
        while(values.hasMoreElements()) {
        	System.out.println(values.nextElement());
        }
        
        System.out.println();
        // Deposit 1,000 into Zara's account
        bal = ((Double)balance.get("Zara")).doubleValue();
        Double old = (Double)balance.put("Zara", new Double(bal+1000)); //Hashtable集合中Key的唯一性
        System.out.println("old value: " + old);
        System.out.println("Zara's new balance: " + balance.get("Zara"));  
    }
    
}


/**class18*/
class PropertiesObject{
	Properties capitals = new Properties();
    Set<Object> states;
    Iterator<Object> itr;
    String str;
    
    public void runProperties() {
    	capitals.put("Illinois", "Springfield");
        capitals.put("Missouri", "Jefferson City");
        capitals.put("Washington", "Olympia");
        capitals.put("California", "Sacramento");
        capitals.put("Indiana", "Indianapolis");

        // Show all states and capitals in hashtable.
        states = capitals.keySet(); // get set-view of keys
        itr = states.iterator();
        while(itr.hasNext()) {
           str = (String) itr.next();
           System.out.println("The capital of " + str + " is " + capitals.getProperty(str));
        }
        System.out.println();
        
        // look for state not in list -- specify default
        str = capitals.getProperty("Illinois", "Not Found");
        System.out.println("The capital of str is " + str);
        str = capitals.getProperty("llinois", "Not Found");
        System.out.println("The capital of str is " + str);
    }
    
}

/**class19*/
class ArrayListObject{
	
	List<String> list=new ArrayList<String>();
	
	public void runArray() {
	    list.add("Hello");
	    list.add("World");
	    list.add("HAHAHAHA");
	    //第一种遍历方法使用foreach遍历List
	    for(String str : list){
	    	System.out.println(str);
    	}
	    System.out.println();

	    //第二种遍历，把链表变为数组相关的内容进行遍历
	    String[] strArray=new String[list.size()];
	    list.toArray(strArray);
	    for(int i=0;i<strArray.length;i++){
	    	System.out.println(strArray[i]);
	    }
	    System.out.println();
	    
	    //第三种遍历 使用迭代器进行相关遍
	    Iterator<String> ite = list.iterator();
	    while(ite.hasNext()){
	        System.out.println(ite.next());
	    }
	//}
	}
}

/**class20*/
class MapObject{
	
	Map<String, String> map = new HashMap<String, String>();
	
	public void runMap() {
		map.put("1", "value1");
	    map.put("2", "value2");
	    map.put("3", "value3");
	      
	    //第一种：普遍使用，二次取值
	    for (String key : map.keySet()) {
	     System.out.println("key= "+ key + " and value= " + map.get(key));
	    }
	    System.out.println();
	  
	    //第二种
	    Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	     Map.Entry<String, String> entry = it.next();
	     System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
	    }
	    System.out.println();
	  
	    //第三种：推荐，尤其是容量大时
	    for (Map.Entry<String, String> entry : map.entrySet()) {
	     System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
	    }
	    System.out.println();
	
	    //第四种
	    for (String v : map.values()) {
	     System.out.println("value= " + v);
	    }
	    System.out.println();
	    
	}
}

/**class21*/
//Java中泛型<E>中E为对象类型，不能是基础数据类型！
class GenericObject{
	//泛型<XX>定义在函数返回值之前
    public <XX> void printArray(XX[] inputArray){
    	for(XX element : inputArray){
    		System.out.printf("%s ", element);
        }
    }
    
    public <XX> void printArray(XX input){
    	System.out.printf("%s ", input);
    }
    
    public <XX> XX printArray2(XX input){
    	return input;  //返回泛型变量
    }
    
    public void printObject(Object inputObject){
    	System.out.printf("%s ", inputObject);
    }
 
    public void runGeneric() {
        Integer[] intArray = { 1, 2, 3, 4, 5 };
        Double[] doubleArray = { 1.1, 2.2, 3.3, 4.4 };
        Character[] charArray = { 'H', 'E', 'L', 'L', 'O' };
        Object[] objectArray = {"test", 1, 2.2, 'h'};
        Double doubleValue = 2.34d;
        Object objectValue = "find123";
 
        System.out.print("整型数组元素为: ");
        printArray(intArray); 
 
        System.out.print("\n双精度型数组元素为: ");
        printArray(doubleArray); 
 
        System.out.print("\n字符型数组元素为: ");
        printArray(charArray); 
        
        System.out.print("\nObject数组元素为: ");
        printArray(objectArray); 
        
        System.out.print("\nDouble元素为: ");
        printArray(doubleValue); 
        
        System.out.print("\nObject元素为: ");
        printArray(objectValue); 
        
        System.out.print("\nDouble元素为: ");
        printObject(doubleValue); 
        
        System.out.print("\nObject元素为: ");
        printObject(objectValue); 
    } 
}

class GenericObject2{
	//泛型的继承<XX entends Comparable<XX>>    ->   对比类Comparable from java.io.Comparable
	public <XX extends Comparable<XX>> XX maximum(XX x, XX y, XX z){
		XX max = x; 
		if(y.compareTo(max) > 0){
			System.out.println(y + ".compareTo(" + max + ") = " + y.compareTo(max));
			max = y; 
		}
		if(z.compareTo(max) > 0){
			System.out.println(z + ".compareTo(" + max + ") = " + z.compareTo(max));
			max = z;     
		}
		return max; 
	}
   
	public void runGeneric(){
		System.out.printf( "%d, %d 和 %d 中最大的数为 %d\n\n",3, 4, 5, maximum( 3, 4, 5 ) );
		System.out.printf( "%.1f, %.1f 和 %.1f 中最大的数为 %.1f\n\n",6.6, 8.8, 7.7, maximum( 6.6, 8.8, 7.7 ) );
		System.out.println( "Max of three String : " + maximum( "AAA", "aaa", "bbb" ) );
		//System.out.println( "Max of three Generic : " + maximum( "AAA", 123, 14.5 ) );  ->Error
	}
   
}

class GenericObject3<XX> {
	//在泛型类class GenericClass<XX>中可以直接定义泛型XX变量
	private XX genericXX;
	//private Integer integerClass = 0;
	private String[] stringClass;
	private String stringValue;
	 
	public void add(XX genericXX) {
		this.genericXX = genericXX;
	}
	 
	public XX get() {
		System.out.println("****Type is:" + genericXX.getClass());
		return genericXX;
	}
	 
	public void runGeneric() {
		GenericObject3<Integer> integerBox = new GenericObject3<Integer>();
		GenericObject3<String> stringBox = new GenericObject3<String>();
		Double[] doubleArray = {1.2, 2.3, 4.5};
		GenericObject3<Double[]> doubleArrayBox = new GenericObject3<Double[]>();
		String[] stringArray = {"aa", "bb", "cc"};
		GenericObject3<String[]> stringArrayBox = new GenericObject3<String[]>();
		Object[] objectArray = {"aa", 12, 3.54d};
		GenericObject3<Object[]> objectArrayBox = new GenericObject3<Object[]>();
		 
	    integerBox.add(new Integer(10));
	    stringBox.add(new String("Generic"));
	    doubleArrayBox.add(doubleArray);
	    stringArrayBox.add(stringArray);
	    objectArrayBox.add(objectArray);
		 
	    System.out.printf("整型值为 :%d\n\n", integerBox.get());
		System.out.printf("字符串为 :%s\n\n", stringBox.get());
		System.out.println(doubleArrayBox.get() + "\n");
		if(((stringClass = stringArrayBox.get()).length) != 0) {
			for(String str : stringClass) {
				stringValue += (" " + str); //没有初始化变量的值会自动为null
			}
			System.out.println(stringValue + "\n");
		}else {
			System.out.println("no variables\n");
		}
		System.out.println(stringArrayBox.get() + "\n");
		System.out.println(objectArrayBox.get() + "\n");
  	}
}

/**class22*/
class ListGeneric{
	List<String> name = new ArrayList<String>();
    List<Integer> age = new ArrayList<Integer>();
    List<Number> number = new ArrayList<Number>();
    List<Object> obj = new ArrayList<Object>();
    //List<?> obj2 = new ArrayList<?>();   ->   Error
    
	public void runList(){
		name.add("icon");
        age.add(18);
        number.add(314);
        getData(name);
        getData(age);
        getData(number);
        
        obj.add("hello");
        obj.add(123);
        obj.add(45.67d);
        getData(obj);
        
        //getDataGeneric(name);  //The method getDataGeneric(List<? extends Number>) in the type ListVar is not applicable for the arguments (List<String>)
        getDataGeneric(age);
        getDataGeneric(number);
        //getDataGeneric(obj);  //The method getDataGeneric(List<? extends Number>) in the type ListVar is not applicable for the arguments (List<Object>)
        
        getDataGeneric2(name);
        getDataGeneric2(age);
        getDataGeneric2(number);
        getDataGeneric2(obj);
        
        //getDataGeneric3(name);  //The method getDataGeneric3(List<? super Number>) in the type ListVar is not applicable for the arguments (List<String>)
        //getDataGeneric3(age);  //The method getDataGeneric3(List<? super Number>) in the type ListVar is not applicable for the arguments (List<Integer>)
        getDataGeneric3(number);
        getDataGeneric3(obj);
        
        //getDataGeneric4(name);  //The method getDataGeneric3(List<? super Number>) in the type ListVar is not applicable for the arguments (List<String>)
        //getDataGeneric4(age);  //The method getDataGeneric3(List<? super Number>) in the type ListVar is not applicable for the arguments (List<Integer>)
        //getDataGeneric4(number);  //The method getDataGeneric3(List<? super Number>) in the type ListVar is not applicable for the arguments (List<Number>)
        getDataGeneric4(obj);
       
	}
	
    //集合类的通配符用 ? 表示
    public void getData(List<?> data) {
    	if(data.size() == 1) {
    		System.out.println("data :" + data.get(0));
    	}else if(data.size() > 1){
    		//fun1 use Iterator
    		Iterator<?> iter = data.iterator();
    		while(iter.hasNext()) {
    			System.out.print(" " + iter.next());
    		}
    		System.out.println();
    		
    		//fun2 use foreach
    		for(Object obj : data) {
    			System.out.print(" " + obj);
    		}
    		System.out.println();
    		
    		//fun3 user for
    		for(int i=0; i<data.size(); i++) {
    			System.out.print(" " + data.get(i));
    		}
    		System.out.println();
    	}
    }
    
    public void getDataGeneric(List<? extends Number> data) {
    	System.out.println("<? extends Number>: " + data.get(0));
    }
    public void getDataGeneric2(List<? extends Object> data) {
    	System.out.println("<? extends Object>: " + data.get(0));
    }
    //<? super Number> -> 类型只能接受Number及其三层父类类型，如Objec类型的实例
    public void getDataGeneric3(List<? super Number> data) {
    	System.out.println("<? super Number>: " + data.get(0));
    }
    //<? super Object> -> 类型只能接受Object及其三层父类类型
    public void getDataGeneric4(List<? super Object> data) {
    	System.out.println("<? super Object>: " + data.get(0));
    }
    
}

/**class23*/
//检验一个类的实例是否能序列化十分简单， 只需要查看该类有没有实现 java.io.Serializable接口。
class SerializableClass implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	//	SerializableClass sc = new SerializableClass();
	//	sc.runSerializable();
	//	sc.runDeserialize();
	//	sc.objectSerializable();
	//	sc.testSerializable();
	public String name;
	public String address;
	public transient int SSN; //该类的所有属性必须是可序列化的。如果有一个属性不是可序列化的，则该属性必须注明是短暂的
	public int number;
	
	public void mailCheck(){
      System.out.println("Mailing a check to " + name + " " + address);
	}
	
	//序列化此对象到.ser文件
	public void runSerializable() {
		this.name = "Reyan Ali";
		this.address = "Phokka Kuan, Ambehta Peer";
		this.SSN = 11122333;
		this.number = 101;
  		try{
  			String fileName = System.getProperty("user.dir"); //程序运行时的相对路径
  		    fileName += "\\src\\SerializableClass.ser";  //路径也可以是"\src\SerializableClass.ser"
  			FileOutputStream fileOut = new FileOutputStream(fileName);
  			ObjectOutputStream out = new ObjectOutputStream(fileOut);
  			out.writeObject(this);
  			out.close();
  			fileOut.close();
  			System.out.println("Serialized data is saved in \n" + fileName + "\n");
  		}catch(IOException i){
  			i.printStackTrace();
  		}
	}
	
	//从.ser文件中反序列化对象出来
	public void runDeserialize() {
		SerializableClass sc = null;
      	try{
      		String fileName = System.getProperty("user.dir"); //程序运行时的相对路径
  		    fileName += "\\src\\SerializableClass.ser";  //路径也可以是"\src\SerializableClass.ser"
      		FileInputStream fileIn = new FileInputStream(fileName);
      		ObjectInputStream in = new ObjectInputStream(fileIn);
      		//对于 JVM 可以反序列化对象，它必须是能够找到字节码的类。如果JVM在反序列化对象的过程中找不到该类，则抛出一个 ClassNotFoundException 异常
      		sc = (SerializableClass)in.readObject();
      		in.close();
      		fileIn.close();
      	}catch(IOException i){
      		i.printStackTrace();
      		return;
  		}catch(ClassNotFoundException c){
  			System.out.println("SerializableClass class not found");
  			c.printStackTrace();
  			return;
  		}
      	System.out.println("Deserialized:");
      	System.out.println("Name: " + sc.name);
      	System.out.println("Address: " + sc.address);
      	System.out.println("SSN: " + sc.SSN);
      	System.out.println("Number: " + sc.number);
      	sc.mailCheck();
	}
	
	public void objectSerializable() {
  		try{
  		    Vector<Object> vc = new Vector<Object>(3);
			vc.add("hello");
			vc.add(123);
			vc.add(45.67d);
			String vcFileName = System.getProperty("user.dir") + "\\src\\vcSerializableClass.ser";
			FileOutputStream vcFileOut = new FileOutputStream(vcFileName);
			ObjectOutputStream vcOut = new ObjectOutputStream(vcFileOut);
			//测试writeObject() -> Object, int, Vector都可成功写入.ser -> vcOut.writeObject(obj);
			vcOut.writeObject(vc);
			vcOut.close();
			vcFileOut.close();
			System.out.println();
  			System.out.println("Custom Object is saved in \n" + vcFileName + "\n");
  		}catch(IOException i){
  			i.printStackTrace();
  		}
	}
	
	public void testSerializable() {
		Vector<?> vc;
		Enumeration<?> em;
		String str = "";
      	try{
      		String vcFileName = System.getProperty("user.dir") + "\\src\\vcSerializableClass1.ser";
      		FileInputStream fileIn = new FileInputStream(vcFileName);
      		ObjectInputStream in = new ObjectInputStream(fileIn);
      		//对于 JVM 可以反序列化对象，它必须是能够找到字节码的类。如果JVM在反序列化对象的过程中找不到该类，则抛出一个 ClassNotFoundException 异常
      		vc = (Vector<?>)in.readObject();
      		in.close();
      		fileIn.close();
      	}catch(IOException i){
      		i.printStackTrace();
      		return;
  		}catch(ClassNotFoundException c){
  			System.out.println("Vector Object not found");
  			c.printStackTrace();
  			return;
  		 //在上面的try/catch中文件名vcSerializableClass1写错了，但是在catch ClassNotFoundException的return之前还是会执行下面的finally{}
  		}finally{
  			try{
  	      		String vcFileName = System.getProperty("user.dir") + "\\src\\vcSerializableClass.ser";
  	      		FileInputStream fileIn = new FileInputStream(vcFileName);
  	      		ObjectInputStream in = new ObjectInputStream(fileIn);
  	      		//对于 JVM 可以反序列化对象，它必须是能够找到字节码的类。如果JVM在反序列化对象的过程中找不到该类，则抛出一个 ClassNotFoundException 异常
  	      		vc = (Vector<?>)in.readObject();
  	      		in.close();
  	      		fileIn.close();
  	      		
  	      		//use Enumeration to append value
		      	em = vc.elements();
		      	while(em.hasMoreElements()) {
		      		str += " " + em.nextElement();
		      	}
		      	System.out.println("Vector Object = " + str);
  	      	}catch(IOException i){
  	      		i.printStackTrace();
  	      		//return;
  	  		}catch(ClassNotFoundException c){
  	  			System.out.println("Vector Object not found");
  	  			c.printStackTrace();
  	  			//return;
  	  		}	
  		}
      	
	}//function testSerializable end
	
} /**class23 end*/

/**class24*/
class SocketClass{
	
	public void run(String [] args){
		String serverName = args[0];
		int port = Integer.parseInt(args[1]);
		try{
			System.out.println("连接到主机：" + serverName + " ，端口号：" + port);
			Socket client = new Socket(serverName, port);
			System.out.println("远程主机地址：" + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
 
			out.writeUTF("Hello from " + client.getLocalSocketAddress());
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			System.out.println("服务器响应： " + in.readUTF());
			client.close();
		}catch(IOException e){
         e.printStackTrace();
		}
   }
	
}

class ServerSocketClass extends Thread{
	private ServerSocket serverSocket;
   
	public ServerSocketClass(int port) throws IOException{
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
	}
 
	public void run(){
		while(true){
			try{
				System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				System.out.println("远程主机地址：" + server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(server.getInputStream());
				System.out.println(in.readUTF());
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				out.writeUTF("谢谢连接我：" + server.getLocalSocketAddress() + "\nGoodbye!");
				server.close();
			}catch(SocketTimeoutException s){
				System.out.println("Socket timed out!");
				break;
			}catch(IOException e){
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void begin(String[] args){
		int port = Integer.parseInt(args[0]);
		try{
			Thread t = new ServerSocketClass(port);
			t.run();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}

/**class25*/
class URLObject{
	public  void run(){
		try{
			URL url = new URL("http://www.runoob.com/index.html?language=cn#j2se");
			System.out.println("URL 为：" + url.toString());
			System.out.println("协议为：" + url.getProtocol());
			System.out.println("验证信息：" + url.getAuthority());
			System.out.println("文件名及请求参数：" + url.getFile());
			System.out.println("主机名：" + url.getHost());
			System.out.println("路径：" + url.getPath());
			System.out.println("端口：" + url.getPort());
			System.out.println("默认端口：" + url.getDefaultPort());
			System.out.println("请求参数：" + url.getQuery());
			System.out.println("定位位置：" + url.getRef());
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}

class URLConnObject{
	public void run(){
		try{
			URL url = new URL("http://www.runoob.com");
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection connection = null;
			if(urlConnection instanceof HttpURLConnection){
				connection = (HttpURLConnection) urlConnection;
			}
			else{
				System.out.println("请输入 URL 地址");
				return;
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String urlString = "";
			String current;
			while((current = in.readLine()) != null){
				urlString += current;
			}
			System.out.println(urlString);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}

/**class26-发送电子邮件*/
class SendEmail{
	public void send(){
		String to = "abcd@gmail.com"; // 收件人电子邮箱
		String from = "web@gmail.com"; // 发件人电子邮箱
		String host = "localhost"; // 指定发送邮件的主机为 localhost
		Properties properties = System.getProperties(); // 获取系统属性
		properties.setProperty("mail.smtp.host", host); // 设置邮件服务器
		Session session = Session.getDefaultInstance(properties); // 获取默认session对象
		
		try{
			MimeMessage message = new MimeMessage(session); // 创建默认的 MimeMessage对象
			message.setFrom(new InternetAddress(from)); // Set From: 
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // Set To: 
			message.setSubject("This is the Subject Line!"); // Set Subject: 
			
			/*设置消息体*/
			message.setText("This is actual message");
			/*设置消息体*/
			
			/*插入html标签*/
         	message.setContent("<h1>This is actual message</h1>", "text/html" );
         	/*插入html标签*/
         	
         	/*带有附件的 E-mail*/
            BodyPart messageBodyPart = new MimeBodyPart(); // 创建消息部分
            messageBodyPart.setText("This is message body"); // 消息
            Multipart multipart = new MimeMultipart(); // 创建多重消息
            multipart.addBodyPart(messageBodyPart); // 设置文本消息部分
            
            messageBodyPart = new MimeBodyPart(); // 附件部分
            String filename = "file.txt";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            
            message.setContent(multipart); // 发送完整消息
        	/*带有附件的 E-mail*/
            
			Transport.send(message); // 发送消息
			System.out.println("Sent message successfully....");
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
}

/**class27 实现接口Runnable创建线程*/
class RunnableClass implements Runnable {
	private Thread t;
	private String threadName;

	RunnableClass(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	@SuppressWarnings("deprecation")
	public void run() {
		System.out.println("Running " + threadName);
		try {
			for (int i = 4; i > 0; i--) {
				System.out.println("Thread: " + threadName + ", " + i);
				Thread.sleep(50);
				if (threadName == "Thread-1" && i == 2) {
					t.stop(); // Thread类中已废弃stop()方法
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}
		System.out.println("Thread " + threadName + " exiting.");
		if (t.getName() == "Thread-1") {
			t.destroy(); // Thread类中已废弃destroy()方法
		}
	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

	public void go() {
		RunnableClass R1 = new RunnableClass("Thread-1");
		R1.start(); // 该线程是使用独立的 Runnable运行对象构造的，JVM会调用该线程的 run()方法，否则start()方法不执行任何操作并返回。

		RunnableClass R2 = new RunnableClass("Thread-2");
		R2.start(); // 该线程是使用独立的 Runnable运行对象构造的，JVM会调用该线程的 run()方法，否则start()方法不执行任何操作并返回。
	}
}

/**class27 继承Thread类创建线程*/
//通过继承Thread来创建线程本质上也是实现了 Runnable接口的一个实例
class ThreadClass extends Thread {
	   private Thread t;
	   private String threadName;
	   
	   ThreadClass( String name) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	   }
	   
	@SuppressWarnings("deprecation")
	public void run() {
	      System.out.println("Running " +  threadName );
	      try {
	         for(int i = 4; i > 0; i--) {
	            System.out.println("Thread: " + threadName + ", " + i);
	            Thread.sleep(50);
	            Thread.yield(); /*yield()告诉当前正在执行的线程把运行机会交给线程池中拥有相同优先级的线程
	                         yield()不能保证使得当前正在运行的线程迅速转换到可运行的状态
	                         yield()仅能使一个线程从运行状态转到可运行状态，而不是等待或阻塞状态*/
	            t.join();  /*当前运行着的线程将阻塞直到这个使用join()的线程实例完成了执行
	                         在join()方法内设定超时，使得join()方法的影响在特定超时后无效。当超时时，主方法和任务线程申请运行的时候是平等的。
	                         然而，当涉及sleep时，join()方法依靠操作系统计时，所以你不应该假定join()方法将会等待你指定的时间*/
	            if(threadName =="Thread-1" && i ==2) {
	            	t.stop(); //Thread类中已废弃stop()方法
	            }
	         }
	      }catch (InterruptedException e) {
	         System.out.println("Thread " +  threadName + " interrupted.");
	      }
	      System.out.println("Thread " +  threadName + " exiting.");
	      if(t.getName() == null || t == null) {
	    	  t.destroy(); //Thread类中已废弃destroy()方法
	      }
	   }
	   
	   public void start () {
	      System.out.println("Starting " +  threadName );
	      if (t == null) {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }
	
	   public static void go() {
		   ThreadClass T1 = new ThreadClass( "Thread-1");
	      T1.start(); //该线程是使用独立的 Runnable运行对象构造的，JVM会调用该线程的 run()方法，否则start()方法不执行任何操作并返回。
	      
	      ThreadClass T2 = new ThreadClass( "Thread-2");
	      T2.start(); //该线程是使用独立的 Runnable运行对象构造的，JVM会调用该线程的 run()方法，否则start()方法不执行任何操作并返回。
	   }   
}

//继承Thread创建线程取随机数
//GuessANumber gn = new GuessANumber(80);
//gn.start();
class GuessANumber extends Thread {
	private int number;
	public GuessANumber(int number) {
		this.number = number;
	}
	   
	public void run() {
		int counter = 0;
		int guess = 0;
      	do{
      		guess = (int) (Math.random() * 100 + 1);
      		System.out.println(this.getName() + " guesses " + guess);
      		counter++;
      	} while(guess != number);
      		System.out.println("** Correct!" + this.getName() + "in" + counter + "guesses.**");
	   	}
}

/**class27 实现接口Callable<V>来创建线程*/
class CallableThreadTest implements Callable<Integer> {
    public static void main(String[] args)  
    {  
    	//创建 Callable 实现类的实例，使用 FutureTask 类来包装 Callable 对象，该 FutureTask 对象封装了该 Callable 对象的 call() 方法的返回值
        CallableThreadTest ctt = new CallableThreadTest();  
        FutureTask<Integer> ft = new FutureTask<Integer>(ctt); 
        for(int i = 0;i < 100;i++) {  
            System.out.println(Thread.currentThread().getName()+" 的循环变量i的值"+i);  
            if(i==20) {  
                new Thread(ft,"有返回值的线程").start();  //会调用到Callable中的call()函数
            }  
        }  
        try {  
            System.out.println("子线程的返回值："+ft.get());  //get()方法会的到Callable对象的 call()方法的返回值
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        } catch (ExecutionException e) {  
            e.printStackTrace();  
        }  
  
    }
    @Override //Callable<V>.call() 
    public Integer call() throws Exception  
    {  
        int i = 0;  
        for(;i<100;i++) {  
            System.out.println(Thread.currentThread().getName()+"fsociety="+i);  
        }  
        return i; //return V;
    }  
}

/**class28 使用线程池ExecutorService*/
class MyExecutor {
    public void execTaskRunnable() {
        //创建线程池对象  参数5，代表有5个线程的线程池
        ExecutorService service = Executors.newFixedThreadPool(5); //10表示最高优先级，1表示最低优先级，5是普通优先级
        //创建Runnable线程任务对象
        TaskRunnable task = new TaskRunnable();
        //从线程池中获取线程对象
        service.submit(task);
        System.out.println("----------------------");
        //再获取一个线程对象
        service.submit(task);
        //关闭线程池
        service.shutdown();
    }
    public void execTaskCallable() {
        //创建线程池对象  参数5，代表有5个线程的线程池
        ExecutorService service = Executors.newFixedThreadPool(5); //10表示最高优先级，1表示最低优先级，5是普通优先级
        //创建Runnable线程任务对象
        TaskCallable task = new TaskCallable();
        //从线程池中获取线程对象
        service.submit(task);
        System.out.println("**********************");
        //再获取一个线程对象
        service.submit(task);
        //关闭线程池
        service.shutdown();
    }
}

class TaskRunnable implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("Runnable线程任务在执行"+i);
        }
    }
}

class TaskCallable implements Callable<Object>{
    @Override
    public Object call() throws Exception {
        for (int i = 0; i < 100; i++) {
            System.out.println("Callable<Object>线程任务在执行"+i);
        }
        return null;
    }
}

/**class28 使用接口Future<Object>来接收线程池中Callable<V>线程的返回值*/
class ThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //创建线程池对象
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        
        //创建一个Callable接口子类对象
        MyCallable c = new MyCallable(100, 200);
        MyCallable c2 = new MyCallable(10, 20);
        
        //Future<Object>可以拿到异步执行任务的返回值
        Future<Integer> result = threadPool.submit(c); //submit(Callable<Integer> task)
        Integer sum = result.get();
        System.out.println("sum=" + sum);
        
        result = threadPool.submit(c2);
        sum = result.get();
        System.out.println("sum=" + sum);
        
        threadPool.shutdown();
    }
}

class MyCallable implements Callable<Integer> {
    //成员变量
    int x = 5;
    int y = 3;
    //构造方法
    public MyCallable(){
    }
    public MyCallable(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public Integer call() throws Exception {
        return x+y;
    }
}

/**class29-jdk1.8-Lambda表达式：
 *    ()     ->   {}
 * 形参列表  ->   语句块，只有一行可以不用{}
 */
//Lambda表达式主要用来定义行内执行的方法类型接口，例如，一个简单方法接口
class LambdaClass {
	
	final String salutation = "Hello! ";
	String salutation2 = "Hello? ";
	final String salutation3 = "Hello#";
	
	public void runLambda(){
		LambdaClass tester = new LambdaClass();
		// 类型声明
		MathOperation addition = (int a, int b) -> a + b;
		// 不用类型声明
		MathOperation subtraction = (a, b) -> a - b;
		// 大括号中的返回语句
		MathOperation multiplication = (int a, int b) -> { return a * b; };
		// 没有大括号及返回语句
		MathOperation division = (int a, int b) -> a / b;
		System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
		System.out.println("10+5=" + addition.operation(10, 5)); //第二种写法
		System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
		System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));
		System.out.println("10 / 5 = " + tester.operate(10, 5, division));
        
		// 不用括号
		GreetingService greetService1 = message -> System.out.println("Hello " + message);
		// 用括号
		GreetingService greetService2 = (message) -> System.out.println("Hello " + message);
		greetService1.sayMessage("1Runoob");
		greetService2.sayMessage("2Google");
		//不能在 lambda内部修改定义在域外的局部变量，否则会编译错误
		GreetingService greetService3 = message -> System.out.println(salutation + message);
		GreetingService greetService4 = message -> System.out.println(salutation2="salutation2"); //可能造成编译错误
		greetService3.sayMessage("3Runoob");
		greetService4.sayMessage("4Runoob");
		String str = "Haha@ "; 
		GreetingService greetService5 = message -> System.out.println(str.length() + " " + message);
		greetService5.sayMessage("5Runoob");
		//str = "Ha ";   => Error => lambda 表达式的局部变量可以不用声明为 final，但是必须不可被后面的代码修改（即隐性的具有 final 的语义）
	}
    
	interface MathOperation {
		int operation(int a, int b);

		//接口内写默认方法不会影响Lambda表达式
		default int addition(int a, int b){
	        return (a+b)*2;
		}
	}
    
	interface GreetingService {
		void sayMessage(String message);
	}
    
	private int operate(int a, int b, MathOperation mathOperation){
		return mathOperation.operation(a, b);
	}
	
}

/**
 *函数式接口(Functional Interface)就是一个具有一个方法的普通接口。
 *函数式接口可以被隐式转换为lambda表达式。
 *函数式接口可以现有的函数友好地支持 lambda。
 *JDK 1.8之前已有的函数式接口:
 java.lang.Runnable
 java.util.concurrent.Callable
 java.security.PrivilegedAction
 java.util.Comparator
 java.io.FileFilter
 java.nio.file.PathMatcher
 java.lang.reflect.InvocationHandler
 java.beans.PropertyChangeListener
 java.awt.event.ActionListener
 javax.swing.event.ChangeListener
 *JDK 1.8 新增加的函数接口：
 java.util.function
 */
@FunctionalInterface
interface Supplier<T> {
	T get();

	default void print(){
		System.out.println("Supplier");
	}
	default void print2(){
		System.out.println("Supplier.print2");
	}
}

class Car {
	public Car() {
		System.out.println("Using class Car's constructor sucess!");
	}
	//Supplier是jdk1.8的接口，这里和lamda一起使用了
	public static Car create(final Supplier<Car> supplier) {
		return supplier.get();
	}

	public static void collide(final Car car) {
		System.out.println("Collided " + car.toString());
	}

	public void follow(final Car another) {
		System.out.println("Following the " + another.toString());
	}

	public void repair() {
		System.out.println("Repaired " + this.toString());
	}
}

/**class30-jdk1.8-方法引用*/
class FunUsing {
	
	public void fun() {
		//构造器引用: 它的语法是Class::new，或者更一般的Class< T >::new实例如下：
		final Car car = Car.create( Car::new );
		final List< Car > cars = Arrays.asList( car );
		
		//静态方法引用: 它的语法是Class::static_method，实例如下：
		cars.forEach( Car::collide );
		
		//特定类的任意对象的方法引用: 它的语法是Class::method实例如下：
		cars.forEach( Car::repair );
		
		//特定对象的方法引用: 它的语法是instance::method实例如下：
		final Car police = Car.create( Car::new );
		cars.forEach( police::follow );
	}
	
}

class FunUsing2 {
	   public void using(){
		   List<String> names = new ArrayList<String>();
	        
		   names.add("Google");
		   names.add("Runoob");
		   names.add("Taobao");
		   names.add("Baidu");
		   names.add("Sina"); 
	        
	      names.forEach(System.out::println);
	   }
}

/**class30-jdk1.8-函数式接口*/
class FunctionalInterfaceClass {
   	public void gogogo(){
      List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
	  
      System.out.println("输出所有数据:");
      // Predicate<Integer> predicate = n -> true
      // n 是一个参数传递到 Predicate 接口的 test 方法
      // n 如果存在则 test 方法返回 true      
      eval(list, n -> true); // 传递参数 n
	        
      // Predicate<Integer> predicate1 = n -> n%2 == 0
      // n 是一个参数传递到 Predicate 接口的 test 方法
      // 如果 n%2 为 0 test 方法返回 true  
      System.out.println("输出所有偶数:");
      eval(list, n -> n % 2 == 0 );
	  
      System.out.println("输出大于 3 的所有数字:");
      // Predicate<Integer> predicate2 = n -> n > 3
      // n 是一个参数传递到 Predicate 接口的 test 方法
      // 如果 n 大于 3 test 方法返回 true 
      eval(list, n -> n > 3 );
   }
	    
   public static void eval(List<Integer> list, Predicate<Integer> predicate) {
      for(Integer n: list) {
	        
         if(predicate.test(n)) {
            System.out.println(n + " ");
         }
      }
   }
   
}

/**class31-Java8-默认方法*/
//简单说，默认方法就是接口可以有实现方法，而且不需要实现类去实现其方法
//我们只需在方法名前面加个default关键字即可实现默认方法

//首先，之前的接口是个双刃剑，好处是面向抽象而不是面向具体编程，缺陷是，当需要修改接口时候，需要修改全部实现该接口的类，目前的java 8之前的集合框架没有foreach方法，
//通常能想到的解决办法是在JDK里给相关的接口添加新的方法及实现。然而，对于已经发布的版本，是没法在给接口添加新方法的同时不影响已有的实现。所以引进的默认方法。
//他们的目的是为了解决接口的修改与现有的实现不兼容的问题。
interface Supplier2 {
	default void print(){
		System.out.println("Supplier2");
	}

	static void printStatic(){
		System.out.println("Supplier2_Static");
	}
}

class DefaultFuncationClass implements Supplier<Object>, Supplier2 {
	//创建自己的默认方法，来覆盖重写接口的默认方法
	public void print(){
      System.out.println("DefaultFuncationClass");
   	}

	@Override
	public String get() {
		//接口Supplier<T>中含有get()方法，需要重写
		return null;
	}
}

class DefaultFuncationClass2 implements Supplier<String>, Supplier2 {
	//使用 super 来调用指定接口的默认方法
	public void print(){ //两个接口的返回值要一致，不然会报错
		Supplier.super.print();
		Supplier.super.print2();
		Supplier2.super.print();
		Supplier2.printStatic(); //Supplier2接口静态方法
   	}

	@Override
	public String get() {
		//接口Supplier<T>中含有get()方法，需要重写
		return null;
	}
}

/**class32-jdk1.8-Stream*/
/*+--------------------+       +------+   +------+   +---+   +-------+
  | stream of elements +-----> |filter+-> |sorted+-> |map+-> |collect|
  +--------------------+       +------+   +------+   +---+   +-------+
 */
//List<String>类是继承于集合类Collection<E>的，stream()是Collection<E>接口中的默认方法
class StreamClass{
	List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
	List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
	
	public void gogogo() {
		
		//Collectors类实现了很多归约操作，例如将流转换成集合和聚合元素
		List<String> filtered = strings.stream() //为集合创建串行流
				                       .filter(string -> !string.isEmpty())
				                       .collect(Collectors.toList()); //Collectors可用于返回列表
		for(String str : filtered) {
			System.out.print(" \"" + str + "\"");
		}
		System.out.println("\n筛选列表: " + filtered);
		String mergedString = strings.stream() //为集合创建串行流
									 .filter(string -> !string.isEmpty())
				                     .collect(Collectors.joining(",, ")); //Collectors可用于返回字符串
		System.out.println("合并字符串: " + mergedString);
		
		//Stream 提供了新的方法 forEach来迭代流中的每个数据
		//                limit方法用于获取指定数量的流
		//      		  sorted 方法用于对流进行排序
		Random random = new Random();
		random.ints()
		      .limit(10)
		      .sorted()
		      .forEach(System.out::println);
		
		// 获取对应的平方数
		List<Integer> squaresList = numbers.stream()
										   .map( i -> i*i)
										   .distinct()
										   .collect(Collectors.toList());
		for(Integer it : squaresList) {
			System.out.print(" " + it);
		}
		System.out.println();
		
		//stream()为集合创建串行流
		long count = strings.stream()
				           .filter(string -> string.isEmpty())
				           .count();
		System.out.println("count: " + count);
		
		//parallelStream()为集合创建并行流
		long count2 = strings.parallelStream()
				           .filter(string -> string.isEmpty())
				           .count();
		System.out.println("count2: " + count2);
		
		//另外，一些产生统计结果的收集器也非常有用。它们主要用于int、double、long等基本类型上，它们可以用来产生类似如下的统计结果。 
		IntSummaryStatistics stats = numbers.stream()
				                            .mapToInt((x) -> x)
				                            .summaryStatistics();
		System.out.println("列表中最大的数 : " + stats.getMax());
		System.out.println("列表中最小的数 : " + stats.getMin());
		System.out.println("所有数之和 : " + stats.getSum());
		System.out.println("平均数 : " + stats.getAverage());
	}
}

/**class33-jdk1.8-Optional类*/
//Optional类是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象。
//Optional是个容器：它可以保存类型T的值，或者仅仅保存null。Optional提供很多有用的方法，这样我们就不用显式进行空值检测。
//Optional类的引入很好的解决空指针异常。
class OptionalClass {
	   public static void main(String args[]) {
		   OptionalClass oc = new OptionalClass();
		   Integer value1 = null;
		   Integer value2 = new Integer(10);
	       
		   try {
			   @SuppressWarnings("unused")
			   Optional<Integer> c = Optional.of(value1); //如果传递的参数是 null，抛出异常 NullPointerException
		   }catch(NullPointerException e){
			   e.printStackTrace();
			   System.out.println("e.getMessage() => " + e.getMessage());
		   }finally {
			   Optional<Integer> a = Optional.ofNullable(value1); //允许传递为 null 参数
			   Optional<Integer> b = Optional.of(value2); //of()不能接收null
			   System.out.println(oc.sum(a,b));
		   }
	   }
	    
   		public Integer sum(Optional<Integer> a, Optional<Integer> b){
   			System.out.println("第一个参数值存在: " + a.isPresent()); //boolean isPresent()判断值是否存在
   			System.out.println("第二个参数值存在: " + b.isPresent()); //boolean isPresent()判断值是否存在
	        
   			Integer value1 = a.orElse(new Integer(2)); //如果值存在则返回它，否则返回括号内穿的参数(2)
	        
   			//Optional.get - 获取值，值需要存在
   			Integer value2 = b.get();
   			return value1 + value2;
   		}
}

/**class33-jdk1.8-Nashorn JavaScript*/
//从JDK1.8开始，Nashorn取代Rhino(JDK1.6, JDK1.7)成为Java的嵌入式JavaScript引擎，这带来了2到10倍的性能提升
//Nashorn完全支持ECMAScript5.1规范以及一些扩展。它使用基于JSR292的新语言特性
//其中包含在JDK7中引入的 invokedynamic，将JavaScript编译成Java字节码
class NashornClass {
	public void gogogo(){
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");
		
		String name = "Hi Guys!";
		Integer result = null;
	      
		try{
			nashorn.eval("print('" + name + "')"); // Script: void print('Hi Guys')
			result = (Integer)nashorn.eval("10 + 2"); // Script: return (10 + 2)
		}catch(ScriptException e){
			System.out.println("执行脚本错误: "+ e.getMessage());
		}
	    
		System.out.println(result.toString());
	}
}

/**class34-jdk1.8-日期时间 API*/
/*
 *Java 8通过发布新的Date-Time API (JSR 310)来进一步加强对日期与时间的处理
 *在旧版的 Java 中，日期时间 API 存在诸多问题，其中有：
	非线程安全      java.util.Date 是非线程安全的，所有的日期类都是可变的，这是Java日期类最大的问题之一
	设计很差       Java的日期/时间类的定义并不一致，在java.util和java.sql的包中都有日期类，此外用于格式化和解析的类在java.text包中定义
	java.util.Date同时包含日期和时间，而java.sql.Date仅包含日期，将其纳入java.sql包并不合理。另外这两个类都有相同的名字，这本身就是一个非常糟糕的设计
	时区处理麻烦        日期类并不提供国际化，没有时区支持，因此Java引入了java.util.Calendar和java.util.TimeZone类，但他们同样存在上述所有的问题
 *Java 8 在 java.time 包下提供了很多新的 API。以下为两个比较重要的 API：
	Local(本地)  简化了日期时间的处理，没有时区的问题
	Zoned(时区)  通过制定的时区处理日期时间
 *新的java.time包涵盖了所有处理日期，时间，日期/时间，时区，时刻(instants)，过程(during)与时钟(clock)的操作
 */
class DateTimeJSR310{
	public void testLocalDateTime(){
		LocalDateTime currentTime = LocalDateTime.now();
		System.out.println("当前时间: " + currentTime);
	        
		LocalDate date1 = currentTime.toLocalDate();
		System.out.println("date1: " + date1);
	        
		Month month = currentTime.getMonth();
		int day = currentTime.getDayOfMonth();
		int seconds = currentTime.getSecond();
		System.out.println("月: " + month +", 日: " + day +", 秒: " + seconds);
	        
		LocalDateTime date2 = currentTime.withDayOfMonth(10).withMonth(2).withYear(2012);
		System.out.println("date2: " + date2);
	        
		// 12 december 2014
		LocalDate date3 = LocalDate.of(2014, Month.DECEMBER, 12);
		System.out.println("date3: " + date3);
	        
		// 22 小时 15 分钟
		LocalTime date4 = LocalTime.of(22, 15);
		System.out.println("date4: " + date4);
	        
		// 解析字符串
		LocalTime date5 = LocalTime.parse("20:15:30");
		System.out.println("date5: " + date5);
		System.out.println();
	}
	
	public void testZonedDateTime(){  
		// 获取当前时间日期
		ZonedDateTime date1 = ZonedDateTime.parse("2015-12-03T10:15:30+05:30[Asia/Shanghai]");
		System.out.println("date1: " + date1);
        
		//时区
		ZoneId id = ZoneId.of("Europe/Paris");
		System.out.println("ZoneId: " + id);
        
		//当前时区
		ZoneId currentZone = ZoneId.systemDefault();
		System.out.println("当期时区: " + currentZone);
	}
	
}

/**class35-jdk1.8-Base64*/
//String encoderStr = "default value";
//byte[] encoderBytes = encoderStr.getBytes("utf-8");
//String encodedStrBase64 = Base64.getEncoder().encodeToString(encoderBytes); //Base64编码
//byte[] decodedBytes = Base64.getDecoder().decode(encodedStrBase64);
//String decoderStr = String(decodedBytes, "utf-8"); //Base64解码
class Base64Class {
	
	public void gogogo(){
		String encodedString;
		byte[] decodedBytes;
		
		try{
			//编码(基本)
			encodedString = Base64.getEncoder().encodeToString("runoob?java8".getBytes("utf-8"));
			System.out.println("Base64编码字符串 (基本) :" + encodedString);
			//解码(基本)
			decodedBytes = Base64.getDecoder().decode(encodedString);
			System.out.println("Base64解码字符串 (基本) :" + new String(decodedBytes, "utf-8"));
			System.out.println();
			
			//编码(URL)
			encodedString = Base64.getUrlEncoder().encodeToString("TutorialsPoint?java8".getBytes("utf-8"));
			System.out.println("Base64编码字符串 (URL) :" + encodedString);
			//解码(URL)
			decodedBytes = Base64.getUrlDecoder().decode(encodedString);
			System.out.println("Base64解码字符串 (URL) :" + new String(decodedBytes, "utf-8"));
			System.out.println();
	        
			//编码(MIME)
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < 10; ++i) {
				stringBuilder.append(UUID.randomUUID().toString());
			}
			encodedString = Base64.getUrlEncoder().encodeToString(stringBuilder.toString().getBytes("utf-8"));
			System.out.println("Base64编码字符串 (MIME) :" + encodedString);
			//解码(MIME)
			decodedBytes = Base64.getMimeDecoder().decode(encodedString);
			System.out.println("Base64解码字符串 (MIME) :" + new String(decodedBytes, "utf-8"));
			
		}catch(UnsupportedEncodingException e){
			System.out.println("Error :" + e.getMessage());
		}
	}
	
}

/**class36-增强*/
class MyStrong{
	final public String st;
	
	//私有化构造器
	@SuppressWarnings("unused")
	private MyStrong(String str,int ... args){
		st = "A"; //所有构造器都要给定义了final的常量赋值
	} 
	
	public MyStrong(){
		st = "A"; //所有构造器都要给定义了final的常量赋值
	}
	public MyStrong(String str){
		st = str; //所有构造器都要给定义了final的常量赋值
	}
	
	//switch case
	public void switchControl() {
		
		//switch1
		String str = "hello";
		switch(str) { //表达式结果的数据类型，只能是 byte  short  char  int  enum   String(jdk1.7后)
			case("take"):
				System.out.println("take");
			    break;
			case("hello"):
				System.out.println("hello");  //这个case种没有break；会导致代码进入后面所有的case内，且遇到break后才会跳出
			case("wrong"):
				System.out.println("wrong"); //没有匹配case但是会进入
			case("break"):
				System.out.println("break"); //跳出
			    break;
			default:
				System.out.println("default");
				//calcSort();  //静态方法必须调用静态方法
		}

		//switch2
		String hello = "", className = null;
		Scanner sc = new Scanner(System.in);
		System.out.println("1. Java\n" + "2. C++\n" + "3. Python\n" + "4. C#\n" + "5. GoLang");
		System.out.println("请选择你要学习的课程:");
		
		ChoiceClass:
		do {
			switch (hello) {
			case "1":
				className = "Java";
				System.out.println("需要选择Java课程吗？(y/n)");
				break;
			case "2":
				className = "C++";
				System.out.println("需要选择C++课程吗？(y/n)");
				break;
			case "3":
				className = "Python";
				System.out.println("需要选择Python课程吗？(y/n)");
				break;
			case "4":
				className = "C#";
				System.out.println("需要选择C#课程吗？(y/n)");
				break;
			case "5":
				className = "GoLang";
				System.out.println("需要选择GoLang课程吗？(y/n)");
				break;
			case "y":
				if (className != null) {
					//return; -> 退出整个方法
					break ChoiceClass;
				}
				System.out.println("***选择错误，请重新选择课程***");
				break;
			case "n":
				if (className == null) {
					System.out.println("***选择错误，请重新选择课程***");
					break;
				} else {
					System.out.println("退出此次选择，请重选课程: ");
					className = null;
					break;
				}
			default:
				if (hello != "") {
					System.out.println("***选择错误，请重新选择课程***");
				}
				//break; ->  default内无需加break，因为代码已经执行到结尾
			}
				
		} while ((hello = sc.next()) != null);
		System.out.println("同学，你选择的课程是《" + className +  "》，欢迎上路！");
	}
	
	//内存自动回收
	@SuppressWarnings("unused")
	public void gc() {
		Base64Class b64c = new Base64Class();
		b64c = null;
		System.gc(); //通知垃圾回收机制可以释放内存，但是并不能保证立即释放，加快释放。
	}
	
	//(int... args) -> 传递0个或多个 int 型实际参数
	public void moreArgs() {
		System.out.println(add(1, 2, 7, 9));
		System.out.println(add("haha:", 1, 2, 7, 9));
	}
	public String add(int... args){ //当调用方法时，可以传递0个或多个 int 型实际参数
		String str = "";
	    for(Integer x : args) {
	    	str += " " + x.toString();
	    }
	    return str;
	}
	public String add(String str, int ... args){ //可变参数要写在参数列表的末尾
	//public void add(String ... strs, int ... args){} -> Error
		for(Integer x : args) {
	    	str += " " + x.toString();
	    }
	    return str;
	}
	
	 //类内部创建对象
//    public  final StrongClass SPRING = new StrongClass();
//    public  final StrongClass SUMMER = new StrongClass();   
//    public  final StrongClass AUTUMN = new StrongClass();   
//    public  final StrongClass WINTER = new StrongClass();   
    
	//使用 enum 关键字创建枚举类
    enum Season{
    	SPRING, SUMMER, AUTUMN, WINTER;
    	void hello(){}
    } 
   /*枚举类常用方法：
    > valueOf(String name) : 根据name枚举类对象名称，获取指定的枚举类对象
    > values() : 获取当前枚举类中所有枚举类对象的数组
    */
    @SuppressWarnings("unused")
	public void getEnumValue() {
    	Season halo = Season.valueOf("SPRING");
    	Season[] hala = Season.values();
    }
    
    //自定义注解
    public @interface MyAnnotation{
        String value() default "atguigu";
    }
    
    //二者之间的区别：
    String str1 = "abc";  //str1 : 代表一个对象，至少在内存中开辟一块内存空间
    String str2 = new String("abc"); //str2 : 代表两个对象，至少在内存中开辟两块内存空间
    
    //消息提示类: JOptionPane
    public void myMessage() {
    	Object[] fruits = {"苹果","梨子","香蕉","西瓜","荔枝"}; 
    	
    	//错误消息框
    	JOptionPane.showMessageDialog(null, "错误","提示",JOptionPane.ERROR_MESSAGE); 
    	
    	//输入消息框
    	String sp = JOptionPane.showInputDialog(null,"我的新世界");
    	if(sp != null && !sp.equals("") ) {  // 不能写 && sp != "" ，包装类一定要用equals()来判断值是否一致
    		System.out.println(sp);
    	} else {
    		System.out.println("没有输入内容");
    	} 
    	
    	//点击消息框
    	int op = JOptionPane.showOptionDialog(null, 
    			                              "你喜欢什么水果",
    			                              "标题1",
    			                              JOptionPane.YES_NO_CANCEL_OPTION,
    			                              JOptionPane.QUESTION_MESSAGE,
    			                              null,
    			                              fruits,
    			                              fruits[0]); 
    	if(op >= 0) {
    		System.out.println((String)fruits[op]);
    	} 	
    	else {
    		System.out.println("没有点击水果");
    	}	
    		
    	//下拉消息框
    	String sp2 = (String)JOptionPane.showInputDialog(null,
										    			"你喜欢什么水果",
										    			"标题2", 
										    			JOptionPane.QUESTION_MESSAGE,
										    			null,
										    			fruits,
										    			fruits[2]);
    	if(sp2 != null) {
    		System.out.println(sp2);
    	}  	
    	else {
    		System.out.println("没有选中水果");
    	}	
    }
    
    //扫描类Scanner
    public void scannerRun(){
    	Scanner sc = new Scanner(System.in);
    	System.out.println(sc.nextLine());
    	
    	Scanner sc2 = new Scanner(System.in);
    	System.out.println(sc.nextInt()); //输入的是非整型的值 -> Exception in thread "main" java.util.InputMismatchException
    	
    	sc.close();
    	sc2.close();
    }
    
    /*断言
    在Java中，assert关键字是从JAVA SE 1.4 引入的，为了避免和老版本的Java代码中使用了assert关键字导致错误，Java在执行的时候默认是不启动断言检查的（这个时候，所有的断言语句都将忽略！），
    如果要开启断言检查，则需要用开关-enableassertions或-ea来开启。当需要在一个值为FALSE时中断当前操作的话，可以使用断言。单元测试必须使用断言（Junit/JunitX）
    
    1、assert关键字需要在运行时候显式开启才能生效，否则你的断言就没有任何意义。而现在主流的Java IDE工具默认都没有开启-ea断言检查功能。
         这就意味着你如果使用IDE工具编码，调试运行时候会有一定的麻烦。并且，对于Java Web应用，程序代码都是部署在容器里面，你没法直接去控制程序的运行，
         如果一定要开启-ea的开关，则需要更改Web容器的运行配置参数。这对程序的移植和部署都带来很大的不便。
         
    2、用assert代替if是陷阱之二。assert的判断和if语句差不多，但两者的作用有着本质的区别：assert关键字本意上是为测试调试程序时使用的，
         但如果不小心用assert来控制了程序的业务流程，那在测试调试结束后去掉assert关键字就意味着修改了程序的正常的逻辑。
         
    3、assert断言失败将面临程序的退出。这在一个生产环境下的应用是绝不能容忍的。一般都是通过异常处理来解决程序中潜在的错误。但是使用断言就很危险，一旦失败系统就挂了。*/
    public void assertExample() {
    	int num = 1;
    	//如果<boolean表达式>为true，则程序继续执行。如果为false，则程序抛出AssertionError，并终止执行
        assert(num > 0);
        assert(num > 0) : "System quit from assertExample()";
    }
    
    //所有对象都是继承于Object类，重写Object类中的toString()方法以后，调用System.out.print(实例对象) 可以直接打印出实例对象的内容！！
    String hello = "123";
    @Override
    public String toString() {
    	return hello;
    }
    
  //对象实现了Iterable接口可以使用增强for循环
    public void forEach() {
    	List<String> lists = Arrays.asList("hello","world");
        for(String go : lists){
            System.out.println(go);
        }
    }
    
    //返回父类申明的新对象
    public List<String> reFather() {
    	return (List<String>)new ArrayList<String>();
    }
    
}


/**class37-枚举类型具体内部实现*/
//java中类的定义使用class，枚举类的定义使用enum。在Java的字节码结构中，其实并没有枚举类型，枚举类型只是一个语法糖，
//在编译完成后被编译成一个普通的类。这个类继承java.lang.Enum，并被final关键字修饰。
enum Fruit {
    APPLE,ORINGE
}
/**上面这个枚举类编译成字节码文件再反编译可以得到如下代码：
final class Fruit extends Enum{
	public static Fruit[] values(){
    	return (Fruit[])$VALUES.clone();
	}

	public static Fruit valueOf(String s){
    	return (Fruit)Enum.valueOf(Fruit, s);
	}

	private Fruit(String s, int i){
    	super(s, i);
	}
	//枚举类型常量
	public static final Fruit APPLE;
	public static final Fruit ORANGE;
	private static final Fruit $VALUES[];//使用数组进行维护

	static{
    	APPLE = new Fruit("APPLE", 0);
    	ORANGE = new Fruit("ORANGE", 1);
    	$VALUES = (new Fruit[] {APPLE, ORANGE});
	}
}
*/


/**class38-反射java.lang.reflect*/
class MyReflect {
	
	@SuppressWarnings("unused")
	public void reflectExample() throws Exception {
		//得到Class类的对象的三种方法：
		Class<?> clazz1 = Class.forName("letsgo.Frank");
		Class<?> clazz2 = (new Frank()).getClass();
		Class<?> clazz3 = Frank.class;
		
		//通过Frank类(clazz1)执行这个类的方法add(String str, int ... args)
		Object obj = clazz1.newInstance(); //一定要有无参构造方法
		Method method = clazz1.getMethod("add", String.class, int.class, int.class);
		Object returnValue = method.invoke(obj, "hello", 2, 3);
		
		//获得构造方法public Frank(String str, int ... args)
		Constructor<?> con1 = clazz1.getConstructor(String.class, int.class, int.class);
		//获得构造方法public Frank()
		Constructor<?> con2 = clazz1.getConstructor();
		Object obj2 = con2.newInstance("hello", 4, 5); //通过构造方法创建对象
		Object obj3 = con2.newInstance(); //通过构造方法创建对象
		
		//获取私有的方法add(String str)
		Method add = clazz1.getDeclaredMethod("add", String.class);
		//访问私有方法前要更改私有方法为可以访问
		add.setAccessible(true);
		add.invoke(obj2);
		
		//获取所有字段(包括私有字段)
		Field[] declaredFields = clazz1.getDeclaredFields();
		for (Field field : declaredFields) {
			int declare = field.getModifiers(); //以整数形式返回由此Field对象表示的字段的Java语言修饰符
			String name = field.getName();
		}
		
		//获取注解对象(只能获取自定义注解对象)
		Annotation[] annotation = clazz1.getAnnotations();
		for (Annotation ann : annotation) {
			System.out.println(ann);
		}
	}
	
}

class Frank {
	public String hi = "hi";
	@SuppressWarnings("unused")
	private String hello = "hello";
	public Frank() {}
	public Frank(String str, int ... args) {}
	public void add(String str, int ... args) {}
	@SuppressWarnings("unused")
	private void add(String str) {}
}


/**class39-内存地址与HashCode*/
class MemoryAndHash {
	
	public static void main(String[] args) {
		/**hashCode与内存地址*/
		String a = "Programming";  //这里"Programming"的值被分配在JVM静态存储区（常量池）中
        String b = new String("Programming");  //这里new String("Programming")被分配在堆内存中
        String c = "Program" + "ming";  //静态存储区（常量池）中已经存在了"Programming"所以利用+相加以后对象的值的内存地址是指向静态存储区（常量池）中"Programming"的
        
        //对象与对象的==是将栈中引用的内存地址进行比较
        //对象与静态存储区（常量池）中的值的==是将对象进行裁箱后进行值的比较
        //两个静态存储区（常量池）中的值的==是直接比较值
        System.out.println(a == b);  //false（对比的内存地址）
        System.out.println(a == c);  //true（对比的内存地址）
        System.out.println(System.identityHashCode(a)); //a的内存地址与c相等
        System.out.println(System.identityHashCode(b)); //a的内存地址与b不相等
        System.out.println(System.identityHashCode(c));
        
        //hash码的主要用途就是在对对象进行散列的时候作为key输入，据此很容易推断出，我们需要每个对象的hash码尽可能不同，这样才能保证散列的存取性能（在带Hash的集合框架尤为重要）。
        //事实上，Object类提供的默认实现确实保证每个对象的hash码不同（在对象的内存地址基础上经过特定算法返回一个hash码）
        /*总结：
          	1、如果两个对象equals，Java运行时环境会认为他们的hashcode一定相等。 
			2、如果两个对象不equals，他们的hashcode有可能相等。 
			3、如果两个对象hashcode相等，他们不一定equals。 
			4、如果两个对象hashcode不相等，他们一定不equals。
        */
        System.out.println(a.hashCode()); 
        System.out.println(b.hashCode());
        System.out.println(c.hashCode());
        
        /**常量池误区*/
        Integer f1 = 100, f2 = 100, f3 = 150, f4 = 150;  
        
        //如果对象字面量的值在-128到127之间，那么不会new新的Integer对象，而是直接引用静态存储区（常量池）中的Integer对象
        System.out.println(f1 == f2);  //true
        System.out.println(f3 == f4);  //false
        
        /*new包装类:
        	String d = new String("xyz"); -> 会创建两个字符串对象：一个是静态存储区的"xyz",一个是用new创建在堆上的对象
        */
	}
}


/**创建对象顺序*/
//执行结果：1a2b2b
//创建对象时构造器的调用顺序是：先初始化静态成员，然后调用父类构造器，再初始化非静态成员，最后调用自身构造器
class A {
	static {
		System.out.print("1");
	}
	public A() {
		System.out.print("2");
	}
}

class B extends A {
	static {
		System.out.print("a");
	}
	public B() {
		System.out.print("b");
	}
}

class TestAB {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		A ab = new B();
		ab = new B();
	}
}


/**递归实现字符串反转*/
class ReverseString {
	public String reverse(String str) {
		if (str == null) {
			return null;
		}
		if (str.length() == 0) {
			return "";
		}
		return reverse(str.substring(1)) + str.charAt(0);
		//在源码String.substring(beginIndex)内最后return (beginIndex == 0) ? this : new String(value, beginIndex, subLen);通过返回自身来结束代码运行
	}
}


/**栈内存溢出*/
class StackOE {
	public static void main(String[] args) {
		main(null);
	}
}


/**条件判断代码规范*/
class ConditionStandard {
	@SuppressWarnings("null")
	public static void main(String[] args) {
		String var = "ha123";
		Boolean bVar = true;
		// null值写前面可以减少失误，""写左边不会报空指针异常
		if (null != var && "" != var) {
			System.out.println(var);
		}
		//编译可以通过，但是可能存在写错，null值判断都要放左边
		if (bVar = null) {
			System.out.println(bVar);
		}
	}
}


/**单例模式下的对象*/
class SingletonTest {

	public static void main(String[] args) {
		Ihaha1 haha1 = HaHa.getInstance();
		System.out.println(haha1);
		Ihaha2 haha2 = HaHa.getInstance();
		System.out.println(haha2);
		
		haha1.say1();
		haha2.say2();
		System.out.println(haha1 == haha2);  //内存地址一致，声明接口不一致，两个对象可以调用的方法不一致
	}
	
}

class HaHa implements Ihaha1, Ihaha2 {
	
	private static HaHa haha;
	
	static {
		haha = new HaHa();
	}
	
	private HaHa() {}
	
	public static HaHa getInstance() {
		return haha;
	}

	public void say2() {
		System.out.println("222222222");
	}

	public void say1() {
		System.out.println("111111111");
	}
}

interface Ihaha1 {
	void say1();
}

interface Ihaha2 {
	void say2();
}









































