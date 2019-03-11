package com.thomax.letsgo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 请在此处对LetsGo进行测试！
 * @author _thomas
 */
public class CodeX {

	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newFixedThreadPool(100);

		for (int i = 0; i < 200; i++) {
			Runnable runnable1 = () -> {
				Test.test();
			};
			Runnable runnable2 = () -> {
				Test.test2();
			};

			threadPool.submit(runnable1);
			threadPool.submit(runnable2);
		}

	}

}

class Test {

	public static void test() {
		System.out.println("A");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("B");
	}

	public static void test2() {

			System.out.println("A2");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("B2");

	}

}

