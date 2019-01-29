package com.thomax.letsgo.spring.test;

import com.thomax.letsgo.spring.domain.CollectionBean;
import com.thomax.letsgo.spring.domain.Emp;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SpringDITest {
	
	/**如果使用junit 进行测试，需要添加关于日志的两个包:
		  com.springsource.org.apache.commons.logging-1.1.1.jar
		  com.springsource.org.apache.log4j-1.2.15.jar
		
		注释代码1：
		//ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	*/
	
	@Test
	//set 方法注入
	public void test01() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		Emp emp = (Emp) ac.getBean("emp04");
		System.out.println(emp);
		ac.close();
	}

	@Test
	//构造方法注入
	public void test02() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		Emp emp = (Emp) ac.getBean("emp05"); //通过name
		Emp emp2 = (Emp) ac.getBean("emp06");  //通过index
		System.out.println(emp);
		System.out.println(emp2);
		ac.close();
	}
	
	@Test
	//p名称空间注入
	public void test03() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		Emp emp = (Emp) ac.getBean("emp07");
		System.out.println(emp);
		ac.close();
	}
	
	@Test
	//spel注入
	public void test04() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		Emp emp = (Emp) ac.getBean("emp08");
		System.out.println(emp);
		ac.close();
	}
	
	@Test
	//数组的注入
	public void test05() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		CollectionBean cb = (CollectionBean) ac.getBean("cb01");
		for (String str : cb.getStrArr()) {
			System.out.println(str);
		}
		ac.close();
	}
	
	@Test
	//List集合的注入
	public void test06() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		CollectionBean cb = (CollectionBean) ac.getBean("cb02");
		for (Emp emp : cb.getList()) {
			System.out.println(emp);
		}
		ac.close();
	}
	
	@Test
	//Map集合的注入
	public void test07() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		CollectionBean cb = (CollectionBean) ac.getBean("cb03");
		Map<Integer, Emp> map = cb.getMap();
		Set<Integer> set = map.keySet();
		for (Integer empno : set) {
			System.out.println(map.get(empno));
		}
		ac.close();
	}
	
	@Test
	//Properties集合的注入
	public void test08() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		CollectionBean cb = (CollectionBean) ac.getBean("cb04");
		Properties props = cb.getProperties();
		System.out.println(props.get("jdbcDriver"));
		System.out.println(props.get("jdbcUrl"));
		System.out.println(props.get("username"));
		System.out.println(props.get("password"));
		ac.close();
	}
	
	@Test
	//对象工厂方法创建对象 + spel注入 
	//=> DefaultListableBeanFactory不能解析spel，会把spel直接当做字符串
	public void test09() {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(new ClassPathResource("beans.xml"));
		Emp emp = (Emp) factory.getBean("emp09");
		Emp emp2 = (Emp) factory.getBean("emp10");
		System.out.println(emp);
		System.out.println(emp2);
	}
}













