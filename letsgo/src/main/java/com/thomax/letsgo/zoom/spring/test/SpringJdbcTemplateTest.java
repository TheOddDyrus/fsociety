package com.thomax.letsgo.zoom.spring.test;

import com.thomax.letsgo.zoom.spring.domain.Emp;
import com.thomax.letsgo.zoom.spring.service.IEmpService;
import com.thomax.letsgo.zoom.spring.service.IEmpTransferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext7.xml")
//@ContextConfiguration("classpath:applicationContext6.xml")  //在7中已经导入了6，直接使用7
//@ContextConfiguration("classpath:applicationContext*.xml")  //如果7中没有导入6，可以写成*
public class SpringJdbcTemplateTest {

	@Resource
	private IEmpService empService2;
	
	@Resource
	private IEmpService empService3;
	
	@Resource(name="empService3")
	private IEmpTransferService empTransferService;
	
	@Resource
	private IEmpTransferService empTransferService2;
	
	@Test
	public void test01() {
		List<Emp> list = empService2.findAll();
		System.out.println("TEST01-----------------------------");
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}
	
	@Test
	public void test02() {
		List<Emp> list = empService3.findAll();
		System.out.println("TEST02-----------------------------");
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}
	
	@Test
	//XML注入方式：测试利用事务转账（可以手动在Dao层增加异常来测试事务）
	public void test03() {
		System.out.println(empService2.findEmpByNo(7777));
		System.out.println(empService2.findEmpByNo(7999));
		boolean flag = empTransferService.transfer(7999, 7777, 100);
		System.out.println("转账是否成功：" + flag);
		System.out.println(empService2.findEmpByNo(7777));
		System.out.println(empService2.findEmpByNo(7999));
	}
	
	@Test
	//注解方式：测试利用事务转账（可以手动在Dao层增加异常来测试事务）
	public void test04() {
		System.out.println(empService2.findEmpByNo(7777));
		System.out.println(empService2.findEmpByNo(7999));
		boolean flag = empTransferService2.transfer(7999, 7777, 100);
		System.out.println("转账是否成功：" + flag);
		System.out.println(empService2.findEmpByNo(7777));
		System.out.println(empService2.findEmpByNo(7999));
	}
}
