package com.thomax.letsgo.design.pattern.behavior;

/**状态模式：
 *	>当对象的状态改变时，同时改变其行为！
 *	>就拿QQ来说，有几种状态，在线、隐身、忙碌等，每个状态对应不同的操作，而且你的好友也能看到你的状态，
 *	 所以，状态模式就两点：1.可以通过改变状态来获得不同的行为  2.你的好友能同时看到你的变化
 */
public class State {
	public static void main(String[] args) {
		//状态模式的使用：
		MyState state = new MyState();  
        Context context = new Context(state);  
        state.setValue("state1");  //设置第一种状态  
        context.method();  //执行行为，行为会随状态变化而变化
        state.setValue("state2");  //设置第二种状态 
        context.method();  //执行行为，行为会随状态变化而变化
	}
}

//具备所有行为的状态类
class MyState {  
    private String value; //状态类中具体的状态
      
    public String getValue() {
        return value;  
    }  
  
    public void setValue(String value) {
        this.value = value;  
    }  
  
    public void method1(){  
        System.out.println("execute the first opt!");
    }  
      
    public void method2(){  
        System.out.println("execute the second opt!");
    }  
}

//切换行为的类
class Context {  
    private MyState state; //状态类
  
    public Context(MyState state) {  
        this.state = state;  
    }  
  
    public MyState getState() {  
        return state;  
    }  
  
    public void setState(MyState state) {  
        this.state = state;  
    }  
    //行为通过状态来改变
    public void method() {  
        if (state.getValue().equals("state1")) {  
            state.method1();  
        } else if (state.getValue().equals("state2")) {  
            state.method2();  
        }  
    }  
}  
