package com.thomax.letsgo.design.pattern.structure;

/**装饰模式：
 *	>需要扩展一个类的功能。
 *	>动态的为一个对象增加功能，而且还能动态撤销。（继承不能做到这一点，继承的功能是静态的，不能动态增删。）
     缺点：产生过多相似的对象，不易排错！
 */
public class Decorator {
	public static void main(String[] args) {
		//装饰模式的使用
		Sourceable source = new Source2();
		source.method(); //原始方法
		MyDecorator dc = new MyDecorator(source);
		dc.method(); //被装饰以后的方法
	}
}


interface Sourceable {  
    void method();  
}

//被装饰类
class Source2 implements Sourceable {  
    public void method() {  
        System.out.println("the Source2 method!");
    }  
}

/**装饰类
 * 可以为被装饰类动态的添加一些功能
 */
class MyDecorator implements Sourceable {  
    private Sourceable source;  
      
    public MyDecorator(Sourceable source){  
        super();  
        this.source = source;  
    }  
    
    public void method() {  
        System.out.println("before decorator!");
        source.method();  
        System.out.println("after decorator!");
    }  
}  