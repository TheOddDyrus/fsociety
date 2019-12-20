package com.thomax.letsgo.zoom.mybatis.test;

import com.thomax.letsgo.zoom.mybatis.dao.IEmpDao;
import com.thomax.letsgo.zoom.mybatis.dao.impl.EmpDao;
import com.thomax.letsgo.zoom.mybatis.domain.Emp;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class NativeDaoTest {
	
	/**
	 * 原始的Dao测试，相当于将XmlMappingTest的代码放到Dao内
	 */
	private IEmpDao empDao;
	
	@Before
	public void init() {
		empDao = new EmpDao();
	}
	
	@Test
	//查询所有
	public void test01() {
		List<Emp> list = empDao.findAll("empMapper.findAll");
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}
	
	@Test
	//模糊查询
	public void test02() {
		String fuzzy = "I";
		List<Emp> list = empDao.findEmpsByFuzzy("empMapper.findEmpsByFuzzy2", fuzzy);
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}
	
}
