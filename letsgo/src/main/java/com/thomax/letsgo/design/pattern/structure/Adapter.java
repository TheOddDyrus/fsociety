package com.thomax.letsgo.design.pattern.structure;

/**适配器模式：将某个类的接口转换成客户端期望的另一个接口表示，目的是消除由于接口不匹配所造成的类的兼容性问题
 *	1.类的适配器模式
 *	2.对象的适配器模式
 *    >装饰模式、代理模式、外观模式、桥接模式、组合模式、享元模式->这6个模式起源于对象的适配器模式
 *	3.接口的适配器模式
 */
public class Adapter {
	public static void main(String[] args) {
		//类的适配器模式的使用
		Targetable target = new ClassAdapter();  
        target.method1(); //扩展的Source类的方法
        target.method2(); //接口的方法
        //对象的适配器模式的使用
        Targetable target2 = new ObjectAdapter(new Source());
        target2.method1(); //扩展的Source类的方法
        target2.method2(); //接口的方法
        //接口的适配器模式的使用
        Targetable target3 = new InterfaceAdapter1();
        Targetable target4 = new InterfaceAdapter2();
        target3.method1(); //重写的抽象类的method1()
        target3.method2(); //抽象类的method2()，无具体实现
        target4.method1(); //抽象类的method2()，无具体实现
        target4.method2(); //重写的抽象类的method2()
	}
}

//Source类拥有一个方法，待适配目标接口Targetable，
class Source {  
    public void method1() {  
        System.out.println("this is source method!");
    }  
}

interface Targetable {  
    void method1();  //与Source类中的方法相同
    void method2();  //新类的方法
}

/**1.类的适配器模式：
 * 通过继承Source类来实现Targetable接口的方法
 */
class ClassAdapter extends Source implements Targetable {  
    public void method2() {  
        System.out.println("this is the targetable method!");
    }  
}


/**2.对象的适配器模式：
 * 在类的适配器模式基础修改为使用Source类的实例
 */
class ObjectAdapter implements Targetable {  
    private Source source;  
      
    public ObjectAdapter(Source source){  
        super();  
        this.source = source;  
    }  
    
    public void method2() {  
        System.out.println("this is the targetable method!");
    }  
  
    public void method1() {  
        source.method1();  
    }  
}


//抽象类实现了Targetable接口的所有方法
abstract class abstractTargetable implements Targetable{  
    public void method1(){}  //无需写具体实现
    public void method2(){}  //无需写具体实现
}

/**3.接口的适配器模式：
 * 当不希望实现Targetable接口中所有的方法时，继承实现了该接口所有方法的抽象类abstractTargetable来实现指定的方法
 */
class InterfaceAdapter1 extends abstractTargetable {  
    public void method1() {  
        System.out.println("this is the InterfaceAdapter1 method1!");
    }
}

class InterfaceAdapter2 extends abstractTargetable {  
    public void method2() {  
        System.out.println("this is the InterfaceAdapter2 method2!");
    }
} 




