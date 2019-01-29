package com.thomax.letsgo.mybatis.test;

import com.thomax.letsgo.mybatis.domain.Emp;
import com.thomax.letsgo.mybatis.domain.EmpX;
import com.thomax.letsgo.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class XmlMappingTest {
	
	/**
	 * 通过EmpMapper.xml映射Emp类，再将EmpMapper.xml配置到SqlMapConfig.xml文件中，通过SqlSession调用方法
	 */

	@Test
	//测试查询
	public void test01() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		
		System.out.println("**********findAll()**********");
		List<Emp> list = sqlSession.selectList("empMapper.findAll");
		for (Emp emp : list) {
			System.out.println(emp);
		}
		
		System.out.println("**********findAllX()**********");
		List<EmpX> list2 = sqlSession.selectList("empMapper.findAllX");
		for (EmpX empX : list2) {
			System.out.println(empX);
		}
		
		System.out.println("**********findAllByMap()**********");
		List<EmpX> list3 = sqlSession.selectList("empMapper.findAllByMap");
		for (EmpX empX2 : list3) {
			System.out.println(empX2);
		}
	}
	
	@Test
	//模糊查询
	public void test02() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		
		System.out.println("**********findEmpsByFuzzy1()**********");
		String fuzzy1 = "%S%";
		List<Emp> list1 = sqlSession.selectList("empMapper.findEmpsByFuzzy1", fuzzy1);
		for (Emp emp1 : list1) {
			System.out.println(emp1);
		}
		
		System.out.println("**********findEmpsByFuzzy2()**********");
		String fuzzy2 = "I";
		List<Emp> list2 = sqlSession.selectList("empMapper.findEmpsByFuzzy2", fuzzy2);
		for (Emp emp2 : list2) {
			System.out.println(emp2);
		}
	}
	
	@Test
	//测试新增
	public void test03() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
		Emp emp = new Emp(8000, "大卫", "CLERK", 7789, new Date(), 1000, 2000, 20);
		sqlSession.update("empMapper.save", emp);
		//sqlSession.commit();  //如果getSqlSession的时候没有传入参数，可以在末尾commit
	}
	
	@Test
	//测试删除
	public void test04() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
		int empno = 8000;
		sqlSession.update("empMapper.remove", empno);
		//sqlSession.commit();  //如果getSqlSession的时候没有传入参数，可以在末尾commit
	}
	
}
