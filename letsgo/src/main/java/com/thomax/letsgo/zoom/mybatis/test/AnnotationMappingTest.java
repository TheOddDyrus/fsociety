package com.thomax.letsgo.zoom.mybatis.test;

import com.thomax.letsgo.zoom.mybatis.domain.Dept;
import com.thomax.letsgo.zoom.mybatis.mapper.annotation.IDeptMapper;
import com.thomax.letsgo.zoom.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class AnnotationMappingTest {
	
	/**
	 * 通过注解将方法的实现与属性映射到接口上，在SqlMapConfig.xml文件内配置这个映射接口可以产生效果
	 * 接口一般命名方式为实体类 + Mapper，不过名字自定义也没关系
	 */

	@Test
	//查询所有
	public void test01() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		IDeptMapper deptMapper = sqlSession.getMapper(IDeptMapper.class);
		List<Dept> list = deptMapper.findAll();
		for (Dept dept : list) {
			System.out.println(dept);
		}
	}
	
	@Test
	//根据编号查询
	public void test02() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		IDeptMapper deptMapper = sqlSession.getMapper(IDeptMapper.class);
		Dept dept = deptMapper.findDeptByDeptno(10);
		System.out.println(dept);
	}
	
	@Test
	//根据编号和名字查询
	public void test03() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		IDeptMapper deptMapper = sqlSession.getMapper(IDeptMapper.class);
		Dept dept = deptMapper.findDeptByStmtIndex(10, "ACCOUNTING");
		System.out.println(dept);
	}
	
	@Test
	//新增数据
	public void test04() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
		IDeptMapper deptMapper = sqlSession.getMapper(IDeptMapper.class);
		Dept dept = new Dept(50, "CustomName", "CustomLoc");
		deptMapper.save(dept);
		//sqlSession.commit();  //没有在getSqlSession()的时候传入true需要手动commit
	}
	
	@Test
	//删除数据1
	public void test05() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
		IDeptMapper deptMapper = sqlSession.getMapper(IDeptMapper.class);
		int deptno = 50;
		deptMapper.remove(deptno);
		sqlSession.commit();  //没有在getSqlSession()的时候传入true需要手动commit
	}
	
}



