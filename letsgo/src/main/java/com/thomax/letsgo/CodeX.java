package com.thomax.letsgo;

import java.io.UnsupportedEncodingException;

/**
 * Good luck have fun!!
 *
 * @author thomax
 */
public class CodeX {

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println("123".getBytes("UTF-8").length);
		System.out.println("ABC".getBytes("UTF-8").length);
		System.out.println("[]".getBytes("UTF-8").length);
		System.out.println("你们好".getBytes("UTF-8").length);
		System.out.println("你们1".getBytes("UTF-8").length);


	}

}



