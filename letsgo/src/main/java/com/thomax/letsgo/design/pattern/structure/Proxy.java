package com.thomax.letsgo.design.pattern.structure;

/**代理模式：
 *	>如果已有的方法在使用的时候需要对原有的方法进行改进，此时有两种办法：
 *	  >>修改原有的方法来适应。这样违反了“对扩展开放，对修改关闭”的原则。
 *	  >>就是采用一个代理类调用原有的方法，且对产生的结果进行控制。这种方法就是代理模式。
	    使用代理模式，可以将功能划分的更加清晰，有助于后期维护！
 */
public class Proxy {
	public static void main(String[] args) {
		//代理模式的使用
		Source3 source = new Source3();
		source.method(); //被代理前的方法
		MyProxy pc = new MyProxy();
		pc.method(); //被代理后的方法
	}
}

interface Sourceable2 {  
    public void method();  
}

//被代理类
class Source3 implements Sourceable2 {  
    public void method() {  
        System.out.println("the Source3 method!");
    }  
}

//代理类
class MyProxy implements Sourceable2 {  
    private Source3 source;  
    
    //代理类已知被代理类所以构造方法是创建被代理类的对象
    public MyProxy(){  
        super();  
        this.source = new Source3();  
    }  
    
    public void method() {  
        before();  
        source.method();  
        atfer();  
    }  
    
    //代理方法1
    private void atfer() {  
        System.out.println("after proxy!");
    }  
    
    //代理方法2
    private void before() {  
        System.out.println("before proxy!");
    }  
}  
