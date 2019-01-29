package com.thomax.letsgo.design.pattern.behavior;

import java.util.Enumeration;
import java.util.Vector;

/**观察者模式:
 * 当一个对象变化时，其它依赖该对象的对象都会收到通知，并且随着变化！
 * 对象之间是一(被依赖对象)对多(依赖对象)的关系
 */
public class Observer {
	public static void main(String[] args) {
		//观察者模式的使用：
		Subject sub = new MySubject();  
        sub.add(new Observer1());  
        sub.add(new Observer2());  
        sub.operation();  //被依赖对象操作自身，并且通知所有依赖对象进行操作
	}
}

interface IObserver {  
    void update();  
}

//依赖，实现类1
class Observer1 implements IObserver {  
    public void update() {  
        System.out.println("observer1 has received!");
    }  
}

//依赖，实现类2
class Observer2 implements IObserver {  
    public void update() {  
        System.out.println("observer2 has received!");
    }  
  
}

interface Subject {  
    void add(IObserver observer);  //增加依赖对象
    void del(IObserver observer);  //删除依赖对象
    void notifyObservers();  //通知所有的依赖对象
    void operation();  //被依赖对象自身的操作
}

//被依赖，抽象类
abstract class AbstractSubject implements Subject {  
    private Vector<IObserver> vector = new Vector<IObserver>();  //依赖关系一对多的对象池
    
    //增加依赖对象
    public void add(IObserver observer) {  
        vector.add(observer);  
    }  
    
    //删除依赖对象
    public void del(IObserver observer) {  
        vector.remove(observer);  
    }  
    
    //通知所有依赖对象，只通知，具体更新方法在依赖对象内实现
    public void notifyObservers() {  
        Enumeration<IObserver> enumo = vector.elements();
        while(enumo.hasMoreElements()){  
            enumo.nextElement().update();  
        }  
    }  
}

class MySubject extends AbstractSubject {  
    public void operation() {  
        System.out.println("update self!");  //操作自身
        notifyObservers();  //通知所有
    }  
  
}  

