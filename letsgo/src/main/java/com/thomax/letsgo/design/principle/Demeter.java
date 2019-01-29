package com.thomax.letsgo.design.principle;

/**迪米特法则（最少知道原则）:
 *	>如果两个类不必彼此直接通信，那么这两个类就不应当直接的相互作用，如果其中一个类需要调用另一个类的某一个方法的话，可以通过第三者转发这个调用
 *	>迪米特法则其根本思想，是强调了类之间的松耦合。在程序设计时，类之间的耦合越弱，越有利于复用，一个处在弱耦合的类被修改，不会对有关系的类造成波及
 */
public class Demeter {

	public static void main(String[] args) {
		//调用者-利用ItManager第三方来调用
		ItDepartment xl = new XiaoLi();  
        ItDepartment xz = new XiaoZhang();  
        ItManager xw = new ItManager();  
        xw.managerPc(xz);  
        xw.managerPc(xl);

	}

}

interface ItDepartment {  
    void installSystem();  
    void fixComputer();  
}

//IT部门员工小张-被调用类
class XiaoZhang implements ItDepartment {  
	
  @Override
  public void installSystem() {  
      System.out.println("小张在给你安装系统");
  }  

  @Override
  public void fixComputer() {  
      System.out.println("小张在给你修电脑");
  }  
}

//IT部门员工小李-被调用类
class XiaoLi implements ItDepartment { 
	
  @Override
  public void installSystem() {  
      System.out.println("小李在给你安装系统");
  }  

  @Override
  public void fixComputer() {  
      System.out.println("小李在给你修电脑");
  }  
}

//IT部门总经理-第三者转发调用，可以在这里维护调用行为（处在弱耦合）
class ItManager {  
    public void managerPc(ItDepartment it) {  
        it.installSystem();  
        it.fixComputer();  
    }  
}  