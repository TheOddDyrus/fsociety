package com.thomax.letsgo.design.pattern.behavior;

/**中介者模式：
 *	>中介者模式也是用来降低类类之间的耦合的，因为如果类类之间有依赖关系的话，不利于功能的拓展和维护，
 *	 因为只要修改一个对象，其它关联的对象都得进行修改。
 *	>只需关心备操作对象和中介者的关系，具体类类之间的关系及调度交给中介者就行，这有点像spring容器的作用
 */
public class Mediator {
	public static void main(String[] args) {
		//中介者模式的使用：
		IMediator mediator = new MyMediator();  
        mediator.createMediator();  //中介者统一创建对象
        mediator.workAll();  //中介者统一安排对象工作
	}
}

interface IMediator {  
    void createMediator();  
    void workAll();  
}

//中介者类，实现统一接口，持有统一操作的动作
class MyMediator implements IMediator {  
    private User user1;  /**中介者中包含对象*/
    private User user2;  /**中介者中包含对象*/
      
    public User getUser1() {  
        return user1;  
    }  
  
    public User getUser2() {  
        return user2;  
    }  
    //创建所有需要进行操作的对象
    public void createMediator() {  
        user1 = new User1(this);   //将中介者传入对象内，中介者本身持有此对象
        user2 = new User2(this);   //将中介者传入对象内，中介者本身持有此对象
    }  
    //所有对象进行操作
    public void workAll() {  
        user1.work();  
        user2.work();  
    }  
}

//抽象的基类
abstract class User {  
    private IMediator mediator;  /**对象中包含中介者*/
    
    public User(IMediator mediator) {  
        this.mediator = mediator;  //需要中介者类来构造函数
    }  
    //得到对象中的中介者
    public IMediator getMediator(){  
        return mediator;  
    }  
    
    public abstract void work();  //统一抽象的动作
}

class User1 extends User {  
    public User1(IMediator mediator){  
        super(mediator);  
    }  
    //实现抽象的动作
    public void work() {  
        System.out.println("user1 execute!");
    }  
}

class User2 extends User {  
    public User2(IMediator mediator){  
        super(mediator);  
    }  
    //实现抽象的动作
    public void work() {  
        System.out.println("user2 execute!");
    }  
}  








