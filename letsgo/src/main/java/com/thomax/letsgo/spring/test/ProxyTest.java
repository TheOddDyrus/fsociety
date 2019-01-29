package com.thomax.letsgo.spring.test;

import com.thomax.letsgo.spring.dao.ICustomDao;
import com.thomax.letsgo.spring.dao.impl.CustomDao;
import com.thomax.letsgo.spring.proxy.CustomDaoCglibProxy;
import com.thomax.letsgo.spring.proxy.CustomDaoDynamicProxy;
import com.thomax.letsgo.spring.proxy.CustomDaoStaticProxy;
import org.junit.Test;

public class ProxyTest {

	@Test
	//静态代理类测试
	public void test01() {
		ICustomDao customProxy = new CustomDao();
		CustomDaoStaticProxy cdsp = new CustomDaoStaticProxy(customProxy);
		cdsp.add();
		System.out.println("-------------------");
		cdsp.update();
		System.out.println("-------------------");
		cdsp.delete();
		System.out.println("-------------------");
		cdsp.query();
	}
	
	@Test
	//动态代理类测试
	public void test02() {
		ICustomDao customProxy = new CustomDao();
		CustomDaoDynamicProxy cddp = new CustomDaoDynamicProxy(customProxy);
		ICustomDao proxyObject = cddp.getProxyObject();
		proxyObject.add();
		System.out.println("-------------------");
		proxyObject.update();
		System.out.println("-------------------");
		proxyObject.delete();
		System.out.println("-------------------");
		proxyObject.query();
		System.out.println("-------------------");
		System.out.println("代理类是ICustomDao实现类：" + (proxyObject instanceof ICustomDao));
	}
	
	@Test
	//CGLIB代理类测试
	public void test03() {
		CustomDaoCglibProxy ccp = new CustomDaoCglibProxy();
		CustomDao proxyObject = ccp.getProxyObject();
		proxyObject.add();
		System.out.println("-------------------");
		proxyObject.update();
		System.out.println("-------------------");
		proxyObject.delete();
		System.out.println("-------------------");
		proxyObject.query();
		System.out.println("-------------------");
		System.out.println("代理类是CustomDao子类：" + (proxyObject instanceof CustomDao));
	}
	
}
