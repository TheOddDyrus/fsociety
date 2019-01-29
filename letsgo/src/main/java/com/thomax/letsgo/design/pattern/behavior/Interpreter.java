package com.thomax.letsgo.design.pattern.behavior;

/**解释器模式：一般主要应用在OOP开发中的编译器的开发中，所以适用面比较窄
 *	>下面案例中利用表达式类与上下文类来进行计算
 */
public class Interpreter {
	public static void main(String[] args) {
		//解释器模式的使用：
		Context2 con = new Context2(9, 2);  //上下文存入9与2
		Plus3 plus = new Plus3();
		int value1 = plus.interpret(con);  //在表达式1中对上下文中9与2相加，得到值11
		Context2 con2 = new Context2(value1, 8); //上下文中存入11与8
		Minus3 minu = new Minus3();
		int result = minu.interpret(con2); //在表达式2中对上下文中的11与8相减，得到值3
        System.out.println(result);
        
        int result2 = new Minus3().interpret((new Context2(new Plus3().interpret(new Context2(9, 2)), 8))); 
        System.out.println(result2);
	}
}

interface Expression {  
    int interpret(Context2 context);  
}

//实现的表达式1
class Plus3 implements Expression {  
    public int interpret(Context2 context) {  
        return context.getNum1() + context.getNum2();  //进行上下文中值的运算
    }  
}

//实现的表达式2
class Minus3 implements Expression {  
    public int interpret(Context2 context) {  
        return context.getNum1() - context.getNum2();  //进行上下文中值的运算
    }  
}

//上下文，可以作为编译器
class Context2 {  
    private int num1;  
    private int num2;  
      
    public Context2(int num1, int num2) {  
        this.num1 = num1;  
        this.num2 = num2;  
    }  
    
    public int getNum1() {  
        return num1;  
    }  
    //可以在这个函数对num1进行翻译或者解释
    public void setNum1(int num1) {  
        this.num1 = num1;  
    }  
    public int getNum2() {  
        return num2;  
    }  
    //可以在这个函数对num2进行翻译或者解释
    public void setNum2(int num2) {  
        this.num2 = num2;  
    }   
}  




