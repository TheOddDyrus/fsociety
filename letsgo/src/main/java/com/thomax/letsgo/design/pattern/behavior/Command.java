package com.thomax.letsgo.design.pattern.behavior;

/**命令模式：目的就是达到命令的发出者和执行者之间解耦，实现请求和执行分开
 *	>举个例子，司令员下令让士兵去干件事情，从整个事情的角度来考虑，司令员的作用是，发出口令，口令经过传递，传到了士兵耳朵里，士兵去执行。
 *	 这个过程好在，三者相互解耦，任何一方都不用去依赖其他人，只需要做好自己的事儿就行，司令员要的是结果，不会去关注到底士兵是怎么实现的
 */
public class Command {
	public static void main(String[] args) {
		//命令模式的使用：
		Receiver receiver = new Receiver();  
        ICommand cmd = new MyCommand(receiver);  //设置接受者到第三者中去
        Invoker invoker = new Invoker(cmd);  //设置第三者到调用者中去
        invoker.action(); //调用者发出动作，通过第三者传递给接收者去执行
	}
}

interface ICommand {  
    void execute();  
}

//传递命令的第三者
class MyCommand implements ICommand {  
    private Receiver receiver;  
      
    public MyCommand(Receiver receiver) {  
        this.receiver = receiver;  
    }  
  
    public void execute() {  
        receiver.action();  
    }  
}

//接收者
class Receiver {  
    public void action(){  
        System.out.println("command received!");
    }  
}

//调用者
class Invoker {  
    private ICommand command;  
      
    public Invoker(ICommand command) {  
        this.command = command;  
    }  
  
    public void action(){  
        command.execute();  
    }  
}  

