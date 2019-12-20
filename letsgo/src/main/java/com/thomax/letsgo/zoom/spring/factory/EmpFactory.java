package com.thomax.letsgo.zoom.spring.factory;

import com.thomax.letsgo.zoom.spring.domain.Emp;

public class EmpFactory {

	public static Emp getInstance() {
		System.out.println("使用静态工厂方法创建对象（工厂方法会在程序启动时被Spring自动加载）");
		return new Emp();
	}
	
	public Emp getEmp() {
		System.out.println("使用对象工厂方法创建对象（工厂方法会在程序启动时被Spring自动加载）");
		return new Emp();
	}
}
