package com.thomax.letsgo.design.pattern.behavior;

/**备忘录模式：
 *	>目的是保存一个对象的某个状态，以便在适当的时候恢复对象
 *	>通俗的讲下：假设有原始类A，A中有各种属性，A可以决定需要备份的属性，备忘录类B是用来存储A的一些内部状态，
 *	 			 类C呢，就是一个用来存储备忘录的，且只能存储，不能修改等操作
 */
public class Memento {
	public static void main(String[] args) {
		//备忘录模式的使用：
        Original origi = new Original("original");  //创建原始类
        MyMemento mc = origi.createMemento(); //把原始类备份到备忘录
        Storage storage = new Storage(mc);  //创建备忘录管理器  
        
        System.out.println("原始状态为：" + origi.getValue());
        
        origi.setValue("New");  // 修改原始类的状态
        System.out.println("修改后的状态为：" + origi.getValue());
        
        origi.restoreMemento(storage.getMemento());  //从备忘录管理器获得备忘录来回复到原始类的状态
        System.out.println("恢复后的状态为：" + origi.getValue());
	}
}

//原始类
class Original {  
    private String value;
      
    public String getValue() {
        return value;  
    }  
  
    public void setValue(String value) {
        this.value = value;  
    }  
  
    public Original(String value) {
        this.value = value;  
    }  
    //创建备忘录，可以决定备份哪些属性
    public MyMemento createMemento(){  
        return new MyMemento(value);  
    }  
    //还原备忘录的备份，可以决定还原哪些属性
    public void restoreMemento(MyMemento memento){  
        this.value = memento.getValue();  
    }  
}

//备忘录，备份原始类的状态
class MyMemento {  
    private String value;
  
    public MyMemento(String value) {
        this.value = value;  
    }  
    
    public String getValue() {
        return value;  
    }  
  
    public void setValue(String value) {
        this.value = value;  
    }  
}

//备忘录管理器，操作备忘录
class Storage {  
    private MyMemento memento;  
      
    public Storage(MyMemento memento) {  
        this.memento = memento;  //需要备忘录来构造备忘录管理器
    }  
    //从备忘录管理器内得到具体的备忘录
    public MyMemento getMemento() {  
        return memento;  
    }  
    //设置具体的备忘录到备忘录管理器
    public void setMemento(MyMemento memento) {  
        this.memento = memento;  
    }  
}  

