package com.thomax.letsgo.design.pattern.behavior;

/**访问者模式：
 *	>把数据结构和作用于结构上的操作解耦合，使得操作集合可相对自由地演化。
 *	>适用于数据结构相对稳定算法又易变化的系统。因为访问者模式使得算法操作增加变得容易。
 *	>优点是增加操作很容易，因为增加操作意味着增加新的访问者。
 *	 缺点就是增加新的数据结构很困难
 */
public class Visitor {
	public static void main(String[] args) {
		//访问者模式的使用：
		IVisitor visitor = new MyVisitor1();  
		IVisitor visitor2 = new MyVisitor2(); 
		Subject2 sub = new MySubject2();  
        sub.accept(visitor);  //接受访问者，且接受了访问者的访问动作
        sub.accept(visitor2);  //接受访问者，且接受了访问者的访问动作
	}
}

interface IVisitor {  
    void visit(Subject2 sub);  
}

//访问者1
class MyVisitor1 implements IVisitor {  
    public void visit(Subject2 sub) {  
    	//访问动作
        System.out.println("more："+sub.getSubject());
    }  
}

//访问者2
class MyVisitor2 implements IVisitor {  
	public void visit(Subject2 sub) {  
		//访问动作
		System.out.println("less："+sub.getSubject());
	}  
}

interface Subject2 {  
    void accept(IVisitor visitor);  
    String getSubject();
}

//目标类
class MySubject2 implements Subject2 {
	//接受将要访问它的访问者
    public void accept(IVisitor visitor) {  
        visitor.visit(this);  //给予访问动作
    }  
    
    //获取将要被访问的属性
    public String getSubject() {
        return "love";  
    }  
}  



