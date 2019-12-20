package com.thomax.letsgo.zoom.spring.proxy;

import com.thomax.letsgo.zoom.spring.dao.ICustomDao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//利用JDK的动态代理技术（Proxy.newProxyInstance）实现
public class CustomDaoDynamicProxy implements InvocationHandler {

	private ICustomDao customDao;
	
	public CustomDaoDynamicProxy(ICustomDao customDao) {
		this.customDao = customDao;
	}
	
	public ICustomDao getProxyObject() {
		/**参数1：获取当前目标类的类加载器对象
		 * 参数2：获取目标对象所实现接口的class对象
		 * 参数3：代表实现了InvocationHandler接口的对象，在此代表当前对象
		 */
		ICustomDao proxyObject = (ICustomDao) Proxy.newProxyInstance(customDao.getClass().getClassLoader(),
																	customDao.getClass().getInterfaces(),
																	this);
		return proxyObject;
	}

	@Override
	/**参数1：代理对象
	 * 参数2：被调用的方法
	 * 参数3：被调用方法的参数
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("query") || method.getName().equals("delete")) {
			return method.invoke(customDao, args);
		}
		checkSecurity();
		return method.invoke(customDao, args);
	}

	private void checkSecurity() {
		System.out.println("检查安全性。。。");
	}
	
}
