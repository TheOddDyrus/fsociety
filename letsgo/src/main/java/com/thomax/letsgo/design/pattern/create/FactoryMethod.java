package com.thomax.letsgo.design.pattern.create;

/**工厂方法模式:
 *	1.普通工厂模式
 *	2.多个工厂模式
 *	3.静态工厂模式
 *	>总体来说，工厂方法模式适合：凡是出现了大量的产品需要创建，并且具有共同的接口时，可以通过工厂方法模式进行创建。
 *	>在以上的三种模式中，第一种如果传入的字符串有误，不能正确创建对象，
 *	>第三种相对于第二种，不需要实例化多个工厂类，所以，大多数情况下，我们会选用第三种——静态工厂模式
 */
public class FactoryMethod {
	public static void main(String[] args) {
		//普通工厂模式的使用
		SendFactory factory = new SendFactory();  
        Sender sender = factory.produce("sms");  
        sender.Send();
        //多个工厂模式的使用
        SendFactory2 factory2 = new SendFactory2();  
        Sender sender2 = factory2.produceMail();  
        sender2.Send(); 
        //静态工厂模式
        Sender sender3 = SendFactory3.produceSms();
        sender3.Send();
	}
}

/**1.普通工厂模式:
 * 建立一个工厂类，对实现了同一接口的一些类进行实例的创建
 */
interface Sender {  
    void Send();  
}

//实现类
class MailSender implements Sender {    
    public void Send() {  
        System.out.println("this is mailsender!");
    }  
}

//实现类
class SmsSender implements Sender {  
    public void Send() {  
        System.out.println("this is sms sender!");
    }  
}

//建立工厂
class SendFactory {  
	//通过识别传递的字符串来返回接口申明的实现类
    public Sender produce(String type) {
    	switch (type) {
    		case "mail":
    			return new MailSender();
    		case "sms":
    			return new SmsSender();
    		default:
    			System.out.println("请输入正确的类型!");
            	return null;
        }  
    }  
}

/**2.多个工厂模式:
 * 是对普通工厂模式的改进，在普通工厂模式中，如果传递的字符串出错，则不能正确创建对象，而多个工厂模式是提供多个工厂方法，分别创建对象
 */
class SendFactory2{
	//改进工厂返回接口申明的实现类
	public Sender produceMail(){  
	    return new MailSender();  
	}  
	public Sender produceSms(){  
	    return new SmsSender();  
	} 
}

/**3.静态工厂模式:
 * 将多个工厂模式里的方法置为静态的，不需要创建实例，直接调用即可
 */
class SendFactory3 {  
	//静态返回接口申明的实现类
    public static Sender produceMail(){  
        return new MailSender();  
    }  
    public static Sender produceSms(){  
        return new SmsSender();  
    }  
}  










