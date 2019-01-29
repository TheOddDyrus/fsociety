package com.thomax.letsgo.design.pattern.create;

import java.util.ArrayList;
import java.util.List;

/**建造者模式：
 *	>工厂类模式提供的是创建单个类的模式，而建造者模式则是将各种产品集中起来进行管理，用来创建复合对象，
 *	>所谓复合对象就是指某个类具有不同的属性
 */
public class Builder {

	public static void main(String[] args) {
		//建造者模式的使用
		BuilderManager bm = new BuilderManager();
		List<Sender> listMailSender = bm.produceMailSender(5);
		listMailSender.add(new SmsSender()); //再给建造者加上一个其他对象元素
	}

}

//创建实现了Sender接口的子类的集合
class BuilderManager {  
    private List<Sender> list = new ArrayList<Sender>();
      
    public List<Sender> produceMailSender(int count){
        for(int i=0; i<count; i++){  
            list.add(new MailSender());  
        }  
        return list;
    }  
      
    public List<Sender> produceSmsSender(int count){
        for(int i=0; i<count; i++){  
            list.add(new SmsSender());  
        }  
        return list;
    }  
}  