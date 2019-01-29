package com.thomax.letsgo.handling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.concurrent.Future;
//import java.lang.annotation.*; //核心


/*javadoc 工具软件识别以下标签:                                              
 *   @author	标识一个类的作者                                                
 *   @deprecated	指名一个过期的类或成员                                         
 *   {@docRoot}	指明当前文档根目录的路径                                        
 *   @exception	标志一个类抛出的异常                                          
 *   {@inheritDoc}	从直接父类继承的注释                                      
 *   {@link}	插入一个到另一个主题的链接                                           
 *   {@linkplain}	插入一个到另一个主题的链接，但是该链接显示纯文本字体                      
 *   @param	说明一个方法的参数	                                            
 *   @return	说明返回值类型
 *   @see	指定一个到另一个主题的链接
 *   @serial	说明一个序列化属性                  
 *   @serialData	说明通过writeObject( ) 和 writeExternal( )方法写的数据
 *   @serialField	说明一个ObjectStreamField组件                         
 *   @since	标记当引入一个特定的变化时
 *   @throws	和 @exception标签一样
 *   {@value}	显示常量的值，该常量必须是static属性
 *   @version	指定类的版本
 */

@Retention(RetentionPolicy.RUNTIME) //代表注解运行时机
@Target(ElementType.TYPE) //代表注解使用的位置
public @interface JavaDoc { //自定义注解
	String name();
	int num();
	String[] values();
}

@JavaDoc(name = "JavaDoc", num = 14, values = {"Java", "Doc"})
class AnnoParser {
	public void runAnno() throws Exception {
		//获取自定义注解
		Class<JavaDoc> clazz = JavaDoc.class; //获得注解的class对象
		Method method = clazz.getMethod("values", String[].class); //得到注解所在的方法
		JavaDoc jd = method.getAnnotation(JavaDoc.class); //得到方法上的注解
		String name = jd.name();
		int num = jd.num();
		String[] values = jd.values();
		
		System.out.println("name:" + name + "num:" + num);
		System.out.print("values:");
		for (String str : values) {
			System.out.println(str + "\t");
		}
	}
	
	@Override  //重写方法的注解，可以进入源码查看内部结构与JavaDoc类似
	public String toString() {
		System.out.println("Override");
		return "Override";
	}
}


/**
 * An {@link MyJavaDoc} that provides methods to manage termination and
 * methods that can produce a {@link Future} for tracking progress of
 * one or more asynchronous tasks.
 * @since 1.5
 * @author David Thomas
 */
class JavaDocExample {
	
	/**
	 * This method returns the square of num.
	 * This is a multiline description. You can use
	 * as many lines as you like.
	 * @param num The value to be squared.
	 * @return num squared.
	 * @deprecated 废弃当前类，方法，变量
	 */
	public double square(double num) {
      return num * num;
	}
	
	/**
	 * This method inputs a number from the user.
	 * @return The value input as a double.
	 * @exception IOException On input error.
	 * @see IOException
	 */
	public double getNumber() throws Exception {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader inData = new BufferedReader(isr);
      String str;
      str = inData.readLine();
      return (new Double(str)).doubleValue();
	}
	
	/**
	 * This method demonstrates square().
	 * @param args Unused.
	 * @return Nothing.
	 * @exception IOException On input error.
	 * @see IOException
	 */
	public static void main(String args[]) throws Exception {
		JavaDocExample jd = new JavaDocExample();
		double val;
		System.out.println("Enter value to be squared: ");
		val = jd.getNumber();
		val = jd.square(val);
		System.out.println("Squared value is " + val);
	}

}
