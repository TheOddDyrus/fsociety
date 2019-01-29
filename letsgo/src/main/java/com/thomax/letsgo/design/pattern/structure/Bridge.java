package com.thomax.letsgo.design.pattern.structure;

/**桥接模式：将抽象化与实现化解耦，使得二者可以独立变化
 *	>我们常用的JDBC桥DriverManager一样，JDBC进行连接数据库的时候，在各个数据库之间进行切换，基本不需要动太多的代码，甚至丝毫不用动，
 *	>原因就是JDBC提供统一接口，每个数据库提供各自的实现，用一个叫做数据库驱动的程序来桥接就行了
 *
 */
public class Bridge {
	public static void main(String[] args) {
		SourceSub1 ss1 = new SourceSub1(); //实现类对象
		SourceSub2 ss2 = new SourceSub2(); //实现类对象
		MyBridge mb = new MyBridge();  //功能桥
		mb.setSource(ss1); //桥接
		mb.method(); //使用桥接后的方法
		mb.setSource(ss2); //桥接
		mb.method(); //使用桥接后的方法
	}
}

interface Sourceable3 {  
    void method();  
}

//实现类1
class SourceSub1 implements Sourceable3 {  
    public void method() {  
        System.out.println("this is the SourceSub1 method!");
    }  
}

//实现类2
class SourceSub2 implements Sourceable3 {  
    public void method() {  
        System.out.println("this is the SourceSub2 method!");
    }  
}

//定义一个抽象的桥
abstract class abstractBridge {  
    private Sourceable3 source;  
    //桥接后实现动态方法调用
    public void method(){  
        source.method();  
    }  
    
    public Sourceable3 getSource() {  
        return source;  
    }  
    //桥接
    public void setSource(Sourceable3 source) {  
        this.source = source;  
    }  
}

//继承使用抽象的桥
class MyBridge extends abstractBridge {  
    public void method(){  
        getSource().method();  
    }  
}  
