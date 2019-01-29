package com.thomax.letsgo;

/**
 * 请在此处对LetsGo进行测试！
 * @author _thomas
 */
public class CodeX {

	public static void main(String[] args) {
		new T3().function3().function2().function1();
		System.out.println();
	}

}

class T1 {
	public void function1() {
		System.out.println("1");

	}
}

class T2 {
	public T1 function2() {
		System.out.println("2");
		return new T1();
	}
}

class T3 {
	public T2 function3() {
		System.out.println("3");
		return new T2();
	}
}
