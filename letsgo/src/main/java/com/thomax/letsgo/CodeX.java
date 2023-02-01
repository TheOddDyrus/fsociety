package com.thomax.letsgo;

import java.io.IOException;

/**
 * Good luck have fun!!
 *
 * @author thomax
 */
public class CodeX {

	public static void main(String[] args) {
		sleep();
	}

	public static void sleep() {
		System.out.print("正在启动...");
		int seconds = 17 * 60;
		for (int i = 0; i < seconds; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				//
			}
		}

		try {
			Runtime.getRuntime().exec("shutdown -s -t 1");
		} catch (IOException e) {
			//
		}
	}

}



