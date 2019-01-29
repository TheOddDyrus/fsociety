package com.thomax.letsgo.spring.proxy;

import com.thomax.letsgo.spring.dao.impl.CustomDao;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

//利用CGLIB的动态代理技术（第三方代理，已经被Spring集成）继承实现
public class CustomDaoCglibProxy implements MethodInterceptor {

	public CustomDao getProxyObject() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(CustomDao.class); //继承实现,设置Ennhancer的父对象
		enhancer.setCallback(this);
		CustomDao customDao = (CustomDao) enhancer.create();
		return customDao;
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		if (method.getName().equals("query") || method.getName().equals("delete")) {
			return methodProxy.invokeSuper(proxy, args);
		}
		checkSecurity();
		return methodProxy.invokeSuper(proxy, args);
	}
	
	private void checkSecurity() {
		System.out.println("检查安全性。。。");
	}

}
