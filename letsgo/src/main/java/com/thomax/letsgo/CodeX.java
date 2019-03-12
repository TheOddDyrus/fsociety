package com.thomax.letsgo;

/**
 * 请在此处对LetsGo进行测试！
 * @author _thomas
 */
public class CodeX {

	public static void main(String[] args) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				System.out.println("1111111111");
				try {
					throw new RuntimeException("123");
				} catch (RuntimeException e) {
					Thread.currentThread().interrupt();
				}
				System.out.println("2222222222222222");
			}
		};

		thread.start();
	}

}


