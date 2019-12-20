package com.thomax.letsgo.zoom.spring.dao.impl;

import com.thomax.letsgo.zoom.spring.dao.ICustomDao;
import org.springframework.stereotype.Repository;

@Repository("customDao2")
public class CustomDao2 implements ICustomDao {

	@Override
	public void add() {
		System.out.println("增加用户（add）");
	}

	@Override
	public void update() {
		System.out.println("更新用户（update）");
		System.out.println(10 /0);  //设置异常
	}

	@Override
	public void delete() {
		System.out.println("删除用户（delete）");
	}

	@Override
	public void query() {
		System.out.println("查询用户（query）");
	}

}
