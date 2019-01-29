package com.thomax.letsgo.spring.test;

import com.thomax.letsgo.spring.domain.Emp;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class SpringIoCTest {
	
	/**如果使用junit 进行测试，需要添加关于日志的两个包:
		  com.springsource.org.apache.commons.logging-1.1.1.jar
		  com.springsource.org.apache.log4j-1.2.15.jar
		
		注释代码1：
		//ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	*/
	
	@Test
	//使用空构造方法创建单例对象
	public void test01() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		Emp emp = (Emp) ac.getBean("emp01");
		Emp emp2 = (Emp) ac.getBean("emp01");
		System.out.println(emp);
		System.out.println("是否单例：" + (emp == emp2));
		ac.close();
	}
	
	@Test
	//使用静态工厂方法创建对象
	public void test02() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		Emp emp = (Emp) ac.getBean("emp02");
		System.out.println(emp);
		ac.close();
	}

	@Test
	//使用对象工厂方法创建对象
	public void test03() {
		//ApplictionContext 在Spring 容器一启动时就会将配置文件中所有的bean 加载到容器中，所以效率更高
		//一般在web 开发中，使用AppcationContext 对象
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		Emp emp = (Emp) ac.getBean("emp03");
		System.out.println("是否是Emp对象：" + (emp instanceof Emp));
		ac.close();
		
		System.out.println("---------------------------");
		
		//BeanFactory 是在需要对象时才会从配置文件中加载对象
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));
		Emp emp2 = (Emp) factory.getBean("emp03");
		System.out.println("是否是Emp对象：" + (emp2 instanceof Emp));
	}
}












