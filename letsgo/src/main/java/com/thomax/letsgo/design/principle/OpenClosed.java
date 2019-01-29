package com.thomax.letsgo.design.principle;

/**开闭原则：
 *	>对于扩展是开放的，对于修改是关闭的，这意味着模块的行为是可以扩展的。
 *	>当应用的需求改变时，我们可以对模块进行扩展，使其具有满足那些改变的新行为。
 *	>对模块行为进行扩展时，不必改动模块的源代码或者二进制代码（无论是可链接的库、DLL或者.EXE文件，都无需改动）
 */
public class OpenClosed {
	public static void main(String[] args) {
		Fruit apple = new Apple(6.3);
		Fruit banana = new Banana(2.6);
		apple.buy(); //购买一个苹果
		apple.buy(); //再购买一个苹果
		banana.buy(); //购买一个香蕉
		banana.buy(); //再购买一个香蕉
		Fruit.totalMoney();  //购买的总价
	}
}

class Fruit {
	//对于修改是关闭的，没有set方法
	private String name;
	private double price;
	//对扩展开放
	protected static double total;
	
	public Fruit(){}
	
	public Fruit(String name, double price) {
		this.name = name;
		this.price = price;
	}
	
	//对扩展开放
	protected void buy() { 
		total += price;
	}
	
	public static double totalMoney() {
		return total;
	}
	
	//对扩展开放
	protected void getNmae() {
		System.out.println("我是" + name);
	}
	
	//对扩展开放
	protected double getPrice() {
		return price;
	}
	
}

/*
 * Fruit类开放了扩展新水果的功能，可以增加水果类
 */

//水果1-苹果
class Apple extends Fruit {
	
	public Apple(double price) {
		super("苹果", price);
	}
	
	
	@Override
	public void buy() {
		//扩展苹果打8则
		total += getPrice() * 0.8; 
	}
	
	@Override
	public void getNmae() {
		//扩展输出英文名
		System.out.println("我是Apple");
	}
	
}

//水果2-香蕉
class Banana extends Fruit {
	
	public Banana(double price) {
		super("香蕉", price);
	}
	
	@Override
	public void getNmae() {
		//扩展输出英文名
		System.out.println("我是Banana");
	}
	
}





