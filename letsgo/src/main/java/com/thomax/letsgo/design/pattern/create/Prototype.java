package com.thomax.letsgo.design.pattern.create;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**原型模式：
 *  将一个对象作为原型，对其进行复制、克隆，产生一个和原对象类似的新对象
 *	>浅复制:将一个对象复制后，基本数据类型的变量都会重新创建，而引用类型，指向的还是原对象所指向的
 *	>深复制:将一个对象复制后，不论是基本数据类型还有引用类型，都是重新创建的。
 *          简单来说，就是深复制进行了完全彻底的复制，而浅复制不彻底
 */
public class Prototype {
	public static void main(String[] args) throws Exception {
		ClonePrototype cp = new ClonePrototype();
		SerializableObject so = new SerializableObject();
		cp.setObj(so);  //设置测试对象SerializableObject
		cp.setString("Hi, guys!");  //设置属性
		ClonePrototype cpClone = cp.clone();  //浅复制1
		ClonePrototype cpClone2 = cp.clone();  //浅复制2
		ClonePrototype cpDeepClone = cp.deepClone();  //深复制

		//对比属性值
		System.out.println(cpClone.getString());
		cpClone2.setString("thomas"); //浅复制2的对象修改属性值
		System.out.println(cpClone.getString()); //浅复1属性值不受浅复制2影响，说明属性被重新创建了副本
		System.out.println(cpDeepClone.getString());
		
		//对比测试对象SerializableObject引用的指向
		System.out.println(so);  //测试对象SerializableObject的指向
		System.out.println(cpClone.getObj());  //浅复制对象的指向(hashcode)一样
		System.out.println(cpDeepClone.getObj());  //深复制对象的指向(hashcode)不一样
		System.out.println(cpClone.getObj().hello2);  //浅复制对象内的属性值一样
		System.out.println(cpDeepClone.getObj().hello2);  //深复制对象内的属性值一样
	}
}

class ClonePrototype implements Cloneable, Serializable {
	  
    private static final long serialVersionUID = 1L;  //默认的1L
    private String string;
  
    private SerializableObject obj;  
  
    /**浅复制-重写Cloneable接口的clone()*/
    public ClonePrototype clone() throws CloneNotSupportedException {
    	ClonePrototype proto = (ClonePrototype)super.clone(); 
        return proto;  
    }  
  
    /**深复制-复制的对象要实现Serializable接口*/
    public ClonePrototype deepClone() throws IOException, ClassNotFoundException {
        //写入当前对象的二进制流 
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);  
        //读出二进制流产生的新对象
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (ClonePrototype)ois.readObject();  
    }  
  
    public String getString() {
        return string;  
    }  
  
    public void setString(String string) {
        this.string = string;  
    }  
  
    public SerializableObject getObj() {  
        return obj;  
    }  
  
    public void setObj(SerializableObject obj) {  
        this.obj = obj;  
    }  
}

//测试对象:实现了Serializable接口才能进行二进制流的深复制
class SerializableObject implements Serializable {
    private static final long serialVersionUID = 1L;  //默认的1L
    public String hello2 = "Hello!";
}





