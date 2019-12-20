package com.thomax.letsgo.zoom.spring.test;

import com.thomax.letsgo.zoom.spring.domain.Dept;
import com.thomax.letsgo.zoom.spring.domain.Emp;
import com.thomax.letsgo.zoom.spring.service.IEmpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext3.xml")
public class SpringAnnotationTest {
	
	/**一般情况下，使用@Autowired与@Qualifier进行联合使用，可以简写只用@Resource
		@Autowired
		@Qualifier("empService")
	*/
	@Resource(name="empService1")
	private IEmpService empService;
	
	@Resource(name="dept")
	private Dept dept;
	@Resource(name="dept")
	private Dept dept1;
	
	@Test
	//注解的作用写在EmpService类里面
	public void test01() {
		List<Emp> list = empService.findAll();
		for(Emp emp : list){
			System.out.println(emp);
		}
	}
	
	@Test
	//Dept中加入了@Scope注解成为非单例的对象
	public void test02() {
		System.out.println("是否是单例：" + (dept == dept1));
		System.out.println(dept);
	}
	
	@Test
	//普通方式获得注入对象
	public void test03() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext2.xml");
		IEmpService empService1 = (IEmpService) ac.getBean("empService");
		List<Emp> list = empService1.findAll();
		for(Emp emp : list){
			System.out.println(emp);
		}
		ac.close();
	}
	
}
