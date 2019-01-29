package com.thomax.letsgo.design.pattern.behavior;

/**策略模式：多用在算法决策系统中，外部用户只需要决定用哪个算法
 *	>策略模式定义了一系列算法，并将每个算法封装起来，使他们可以相互替换，且算法的变化不会影响到使用算法的客户。
 *  >需要设计一个接口，为一系列实现类提供统一的方法，多个实现类实现该接口，
 *  >设计一个抽象类（可有可无，属于辅助类），提供辅助函数
 */
public class Strategy {
	public static void main(String[] args) {
		//策略模式的使用：
		ICalculator plus = new Plus(); 
        System.out.println(plus.calculate("2+8"));  //传入表达式进行+计算
        ICalculator minus = new Minus(); 
        System.out.println(minus.calculate("2-8"));   //传入表达式进行-计算
        ICalculator multiply = new Multiply(); 
        System.out.println(multiply.calculate("2*8"));  //传入表达式进行*计算
	}
}

//统一接口
interface ICalculator {  
    int calculate(String exp);
}

//作为辅助的抽象类
abstract class AbstractCalculator {  
    public int[] split(String exp, String opt){
        String array[] = exp.split(opt);
        int arrayInt[] = new int[2];  
        arrayInt[0] = Integer.parseInt(array[0]);
        arrayInt[1] = Integer.parseInt(array[1]);
        return arrayInt;  
    }  
}

//实现类1
class Plus extends AbstractCalculator implements ICalculator {  
    public int calculate(String exp) {
        int arrayInt[] = split(exp,"\\+");  //继承的抽象类中的辅助功能 
        return arrayInt[0]+arrayInt[1];  
    }  
}

//实现类2
class Minus extends AbstractCalculator implements ICalculator {   
    public int calculate(String exp) {
        int arrayInt[] = split(exp,"-");  //继承的抽象类中的辅助功能 
        return arrayInt[0]-arrayInt[1];  
    }  
}

//实现类3
class Multiply extends AbstractCalculator implements ICalculator {  
    public int calculate(String exp) {
        int arrayInt[] = split(exp,"\\*");  //继承的抽象类中的辅助功能 
        return arrayInt[0]*arrayInt[1];  
    }  
}  
