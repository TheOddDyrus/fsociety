package com.thomax.letsgo.handling;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegularExpressions {

	public static void main(String[] args) throws PatternSyntaxException {

       String REGEX = "\\bcat(1|2)?\\b";
       String INPUT = "cc cat1 hello cat cat2 cattie cat1 end";
       StringBuffer sb = new StringBuffer();
 
       Pattern p = Pattern.compile(REGEX);
       Matcher m = p.matcher(INPUT); // 获取 matcher 对象
       int count = 0;
       
       while(m.find()) {
         count++;
         System.out.println("Match number " + count);
         System.out.println("start(): " + m.start());
         System.out.println("end(): " + m.end());
         System.out.println("group(0): " + m.appendReplacement(sb, "x").group(0));
         System.out.println("appendReplacement(): " + sb);
      }
       m.appendTail(sb);
       System.out.println("sb: " + sb);
	}
}


/**正则表达式标准用法*/
//注意在Java里面需要两个反斜线\\代表一个正则表达式的反斜线
//例如： \b是Java里面的转义字符(边界符), 用作正则表达式需要用\\b
class BasicRegex {
	
	String regex = "";
	CharSequence input = "";  //CharSequence的值是可读可写序列，而String的值是只读序列，正则表达式中传入内容参数input也可以用String类型代替
	static Pattern pa;
	static Matcher ma;
	boolean isMatch, isFind, isLookingAt;
	int groupCount;
	String allMatchStr, firstGroup, allReplaceStr, firstReplaceStr;
	StringBuffer appendSb;
	String normalStr;
	
	BasicRegex(String regex, CharSequence input) {
		this.regex = regex;
		this.input = input;
	}
	
	public void runRegex() {
		try {
			//Pattern类没有公共构造方法，需要调用静态方法compile()返回一个Pattern对象
			pa = Pattern.compile(regex);
			//Matcher类没有公共构造方法，需要调用静态方法matcher()返回一个Matcher对象
			ma = pa.matcher(input);
			
			isMatch = Pattern.matches(regex, input); //regex是否与input全部匹配 用法1
			isMatch = Pattern.compile(regex).matcher(input).matches(); //regex是否与input全部匹配 用法2
			
		   /*捕获组是把多个字符当一个单独单元进行处理的方法，它通过对括号内的字符分组来创建。
			  从左至右计算其开括号"("来编号。例如，在表达式(A)(B(C))，有三个这样的组（group(0)不算）：
			    (A)(B(C))     ->  group(0)
				(A)           ->  group(1)  -> 第一个"("的组
				(B(C))        ->  group(2)
				(C)           ->  group(3)
			  => 调用Matcher对象的groupCount()方法可以返回表达式有多少个分组( 不包含group(0) )
			  => 特殊的组group(0)，它代表整个表达式的所有内容
			*/
			isFind =  Pattern.compile(regex).matcher(input).find();  //尝试查找与该模式匹配的输入序列的下一个子序列
			groupCount = Pattern.compile(regex).matcher(input).groupCount();  //存在多少个分组
			allMatchStr = Pattern.compile(regex).matcher(input).group(0);  //代表整个表达式中匹配到的值
			firstGroup = Pattern.compile(regex).matcher(input).group(1);  //代表第一个分组匹配到的值
			
			//利用find()遍历匹配到的子序列
			int count = 0;
			while(ma.find()) { //存在匹配返回true，类似Iterator的hasNext()
				count++;
				System.out.println("Match number: " + count);
				System.out.println("start(): " + ma.start()); //start()返回在以前的匹配操作期间，由给定组所捕获的子序列的初始索引
				System.out.println("end(): " + ma.end()); //end()的返回值是最后一个匹配字符的索引加1
				System.out.println("Match String: " + ma.group(0)); //可以利用group(0)来获得每次匹配到的值
				//遍历每个子序列中的组
				for (int i = 1; i <= ma.groupCount(); i++) {
					System.out.println("group(" + i + "): " + ma.group(i));
				}
				/*appendReplacement()将当前匹配子串替换为指定字符串" "，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到appendSb
				   比如 Pattern.compile("\\bcat(1|2)?\\b").matcher("cc cat1 hello cat cat2 cattie cat1 end").appendReplacement(appendSb, "x")
				 		第一次find() -> "cc x"
				 		第二次find() -> "cc x hello x"
				 		第三次find() -> "cc x hello x x"
				 		第三次find() -> "cc x hello x x cattie x"
			 	   => 这个while()结束以后，可以用appendTail()来接收appendReplacement()最后一次匹配工作后剩余的字符串 -> " end"
				 */
				ma.appendReplacement(appendSb, " ");
				System.out.println("appendReplacement(): " + appendSb);
			}
			System.out.println("appendTail(): " + ma.appendTail(appendSb));  //原理：appendSb.append(最后一次匹配工作后剩余的字符串)
			isLookingAt = ma.lookingAt();  //与matches()类似但是无需全部匹配，匹配成功了不再继续匹配(多次执行这个函数以后start()与end()的返回值不会变)
			
			allReplaceStr = Pattern.compile(regex).matcher(input).replaceFirst(" ");  //将第一个匹配到的子序列替换成" "
			firstReplaceStr = Pattern.compile(regex).matcher(input).replaceAll(" ");  //将所有匹配到的子序列替换成" "
			
			System.out.println("IsMatch: " + "hello234".matches(regex));  //String类里面已经写了matches(String regex)方法
			
		} catch (PatternSyntaxException pse) {  //非强制异常类，它表示一个正则表达式模式中的语法错误
			System.out.println(pse.getDescription());  //获取错误的描述
			System.out.println(pse.getIndex());  //获取错误的索引
			System.out.println(pse.getPattern());  //获取错误的正则表达式模式
			System.out.println(pse.getMessage());  //返回多行字符串，包含语法错误及其索引的描述、错误的正则表达式模式和模式中错误索引的可视化指示
		}
	}
}










