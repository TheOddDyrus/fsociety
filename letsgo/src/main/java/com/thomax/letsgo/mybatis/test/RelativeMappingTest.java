package com.thomax.letsgo.mybatis.test;

import com.thomax.letsgo.mybatis.domain.Dept;
import com.thomax.letsgo.mybatis.domain.DeptCustom;
import com.thomax.letsgo.mybatis.mapper.IDeptMapper2;
import com.thomax.letsgo.mybatis.mapper.annotation.IDeptMapper3;
import com.thomax.letsgo.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class RelativeMappingTest {

	@Test
	//一对一关联关系映射1（自定义domain子类）
	public void test01() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		IDeptMapper2 mapper = sqlSession.getMapper(IDeptMapper2.class);
		List<DeptCustom> list = mapper.findAllDepts1();
		for (DeptCustom dc : list) {
			System.out.println(dc);
		}
	}
	
	@Test
	//一对一关联关系映射2（XML配置文件中自定义ResultMap）
	//Dept与emp的关系是一对多，但是主表是Dept所以查出来的emp对应数据行数也要跟Dept一致
	public void test02() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		IDeptMapper2 mapper = sqlSession.getMapper(IDeptMapper2.class);
		List<Dept> list = mapper.findAllDepts2();
		for (Dept dept : list) {
			System.out.println(dept);
		}
	}
	
	@Test
	//一对一关联关系映射3（XML配置文件中自定义ResultMap,实现懒加载策略实现→经常用）
	//这里面的Emp与Dept是多对一的关系，在这里测试不了，要把emp改成list成为一对多才能测试成功
	//这里只作为演示测试代码，当做Emp与Dept是一对一的关系即可成功！
	public void test03() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		IDeptMapper2 mapper = sqlSession.getMapper(IDeptMapper2.class);
		List<Dept> list = mapper.findAllDepts3();
		for (Dept dept : list) {
			System.out.println(dept);
		}
	}
	
	@Test
	//一对一关联关系映射4（利用注解完成）
	public void test04() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		IDeptMapper3 mapper = sqlSession.getMapper(IDeptMapper3.class);
		List<Dept> list = mapper.findAll();
		for (Dept dept : list) {
			System.out.println(dept);
		}
	}
	
	@Test
	//一对多关联关系映射1（使用ResultMap 映射关联对象的字段信息）
	public void test05() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		IDeptMapper2 mapper = sqlSession.getMapper(IDeptMapper2.class);
		List<Dept> list = mapper.findAllDepts4();
		for (Dept dept : list) {
			System.out.println(dept);
		}
	}
	
	@Test
	//动态查询SQL测试
	public void test06() {
		//这里就不写多余的东西，只讲原理了：
		//1.SQL中的where条件的字段，放到对象DeptVo中去
		//2.动态代理的接口中传参为(DeptVo deptVo)，利用这个DeptVo对象中的属性作为动态的SQL条件
		//3.动态的SQL条件配置的主要内容在mybatis/mapper/DeptMapper.xml
	}
	
}
