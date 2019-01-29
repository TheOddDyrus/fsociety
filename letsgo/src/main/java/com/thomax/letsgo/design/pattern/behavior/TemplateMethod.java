package com.thomax.letsgo.design.pattern.behavior;

/**模板方法模式：
 *	>与策略模式类似，但是策略模式中抽象类是作为辅助工具，模板方法模式则将抽象类改为主体
 */
public class TemplateMethod {
	public static void main(String[] args) {
		//模板方法模式的使用：
        AbstractCalculator2 plus = new Plus2();  
        System.out.println(plus.calculate("8+8", "\\+")); //调用抽象类主方法，从而调用到子类方法
        AbstractCalculator2 minus = new Minus2();  
        System.out.println(minus.calculate("8-8", "\\-"));  //调用抽象类主方法，从而调用到子类方法
	}
}

//一个抽象类中，有一个主方法，再定义1...n个子方法，可以是抽象的，也可以是实际的方法
abstract class AbstractCalculator2 {  
    //主方法，实现对本类其它子方法的调用
    public final int calculate(String exp, String opt){
        int array[] = split(exp,opt);  
        return calculate(array[0],array[1]);  
    }  
      
    //需要子类重写的子方法
    public abstract int calculate(int num1,int num2);
    
    //实际的子方法
    public int[] split(String exp, String opt){
        String array[] = exp.split(opt);
        int arrayInt[] = new int[2];  
        arrayInt[0] = Integer.parseInt(array[0]);
        arrayInt[1] = Integer.parseInt(array[1]);
        return arrayInt;  
    }  
}

//重写抽象的子方法，通过调用抽象类实现对子类的调用
class Plus2 extends AbstractCalculator2 {  
    public int calculate(int num1,int num2) {  
        return num1 + num2;  
    }  
}

//重写抽象的子方法，通过调用抽象类实现对子类的调用
class Minus2 extends AbstractCalculator2 {  
  public int calculate(int num1,int num2) {  
      return num1 - num2;  
  }  
}  
