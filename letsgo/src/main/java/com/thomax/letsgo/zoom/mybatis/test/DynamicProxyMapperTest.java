package com.thomax.letsgo.zoom.mybatis.test;

import com.thomax.letsgo.zoom.mybatis.domain.Emp;
import com.thomax.letsgo.zoom.mybatis.domain.EmpX;
import com.thomax.letsgo.zoom.mybatis.mapper.IEmpMapper;
import com.thomax.letsgo.zoom.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class DynamicProxyMapperTest {

	/**Mapper 动态代理开发方式：（经常使用）
		① 接口的完全限定名与xml 文件中的namespace 要一致。
		② 接口中的方法名与xml 文件中的某个标签的id 名称一致。
		③ 接口中的方法的返回值（如果是集合，则是集合元素的类型）类型与xml 文件中的resultType 一致。
		④ 接口中的方法的参数类型与xml 文件中的parameterType 一致。
	 */
	
	@Test
	//测试查询
	public void test01() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		IEmpMapper mapper = sqlSession.getMapper(IEmpMapper.class);
		
		System.out.println("**********findAll()**********");
		List<Emp> list = mapper.findAll();
		for (Emp emp : list) {
			System.out.println(emp);
		}
		
		System.out.println("**********findAllX()**********");
		List<EmpX> list2 = mapper.findAllX();
		for (EmpX empX : list2) {
			System.out.println(empX);
		}
		
		System.out.println("**********findAllByMap()**********");
		List<EmpX> list3 = mapper.findAllByMap();
		for (EmpX empX2 : list3) {
			System.out.println(empX2);
		}
	}
	
	@Test
	//模糊查询
	public void test02() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		IEmpMapper mapper = sqlSession.getMapper(IEmpMapper.class);
		
		System.out.println("**********findEmpsByFuzzy1()**********");
		String fuzzy1 = "%S%";
		List<Emp> list1 = mapper.findEmpsByFuzzy1(fuzzy1);
		for (Emp emp1 : list1) {
			System.out.println(emp1);
		}
		
		System.out.println("**********findEmpsByFuzzy2()**********");
		String fuzzy2 = "I";
		List<Emp> list2 = mapper.findEmpsByFuzzy2(fuzzy2);
		for (Emp emp2 : list2) {
			System.out.println(emp2);
		}
	}
	
	@Test
	//测试新增
	public void test03() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
		IEmpMapper mapper = sqlSession.getMapper(IEmpMapper.class);
		Emp emp = new Emp(8000, "大卫", "CLERK", 7789, new Date(), 1000, 2000, 20);
		mapper.save(emp);
		//sqlSession.commit();  //如果getSqlSession的时候没有传入参数，可以在末尾commit
	}
	
	@Test
	//测试删除
	public void test04() {
		SqlSession sqlSession = MyBatisUtils.getSqlSession(true);
		IEmpMapper mapper = sqlSession.getMapper(IEmpMapper.class);
		int empno = 8000;
		mapper.remove(empno);
		//sqlSession.commit();  //如果getSqlSession的时候没有传入参数，可以在末尾commit
	}
	
}
