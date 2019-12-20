package com.thomax.letsgo.zoom.spring.proxy;

import com.thomax.letsgo.zoom.spring.dao.ICustomDao;

public class CustomDaoStaticProxy implements ICustomDao {
	
	private ICustomDao customDao;
	
	public CustomDaoStaticProxy(ICustomDao customDao) {
		this.customDao = customDao;
	}

	@Override
	public void add() {
		checkSecurity();
		customDao.add();
	}

	@Override
	public void update() {
		checkSecurity();
		customDao.update();
	}

	@Override
	public void delete() {
		customDao.delete();
	}

	@Override
	public void query() {
		customDao.query();
	}
	
	private void checkSecurity() {
		System.out.println("进行安全性查询。。。");
	}

}
