package com.thomax.letsgo.design.pattern.create;

/**抽象工厂模式：
 *	>工厂方法模式有一个问题就是，类的创建依赖工厂类，也就是说，如果想要拓展程序，必须对工厂类进行修改，这违背了闭包原则，
 *	>所以，从设计角度考虑，有一定的问题，如何解决？
 *	>就用到抽象工厂模式，创建多个工厂类，这样一旦需要增加新的功能，直接增加新的工厂类就可以了，不需要修改之前的代码
 *
 */
public class AbstractFactory {
	public static void main(String[] args) {
		//抽象工厂模式的使用
		Provider provider = new SendMailFactory();
		Sender2 sender = provider.produce();
		sender.Send();
	}
}

interface Sender2 {  
    void Send();  
}

//抽象的工厂接口
interface Provider {  
    Sender2 produce();  
}

//实现类
class MailSender2 implements Sender2 {  
    public void Send() {  
        System.out.println("this is mailsender!");
    }  
}

//实现类
class SmsSender2 implements Sender2 {   
    public void Send() {  
        System.out.println("this is sms sender!");
    }  
}

//工厂类
class SendMailFactory implements Provider {  
    public Sender2 produce(){  
        return new MailSender2();  
    }  
}

//工厂类
class SendSmsFactory implements Provider{  
    public Sender2 produce() {  
        return new SmsSender2();  
    }  
}  