package com.thomax.letsgo.mybatis.mapper;

import com.thomax.letsgo.mybatis.domain.Emp;
import com.thomax.letsgo.mybatis.domain.EmpX;

import java.util.List;

public interface IEmpMapper {
	
	/**Mapper 动态代理开发方式：（经常使用）
		① 接口的完全限定名与xml 文件中的namespace 要一致。
		② 接口中的方法名与xml 文件中的某个标签的id 名称一致。
		③ 接口中的方法的返回值（如果是集合，则是集合元素的类型）类型与xml 文件中的resultType 一致。
		④ 接口中的方法的参数类型与xml 文件中的parameterType 一致。
	 */

	List<Emp> findAll();
	List<EmpX> findAllX();
	List<EmpX> findAllByMap();
	List<Emp> findEmpsByFuzzy1(String fuzzy);
	List<Emp> findEmpsByFuzzy2(String fuzzy);
	void save(Emp emp);
	void remove(int empno);
}
