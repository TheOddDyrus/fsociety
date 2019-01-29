package com.thomax.letsgo.spring.test;

import com.thomax.letsgo.spring.dao.ICustomDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext5.xml")
public class SpringAOPTest2 {
	
	/**AOP联盟包：com.springsource.org.aopalliance-1.0.0.jar
	 * 动态织入包：com.springsource.org.aspectj.weaver-1.6.8.RELEASE.jar
	 * AOP核心包：spring-aop-4.2.4.RELEASE.jar
	 *			  spring-aspects-4.2.4.RELEASE.jar
	 *			  
	 * AOP名词：
	 *   Joinpoint（连接点）：目标对象中，所有可以增强的方法
	 *   Pointcut（切入点）：目标对象，已经增强的方法
	 *   Advice（通知/增强）：增强的代码
	 *   Target（目标对象）：被代理的对象
	 *   Weaving（织入）：将通知应用到切入点的过程
	 *   Proxy（代理）：将通知织入到目标对象后，形成代理对象
	 *   aspect（切面）：切入点 + 通知
	 */
	
	@Resource(name="customDao2")
	private ICustomDao customDao;
	
	@Test
	public void test01() {
		customDao.add();  //非异常
		System.out.println("-----------------------");
		customDao.update();  //异常
	}
}
