package com.thomax.letsgo.mybatis.mapper.annotation;

import com.thomax.letsgo.mybatis.domain.Dept;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface IDeptMapper3 {
	
	@Results(id="deptRS2", value={
			@Result(column="dname",property="dnameX"),
			@Result(column="loc",property="locX"),
			@Result(column="deptno",property="deptno",id=true,
			one=@One(select="mybatis.mapper.IDeptMapper2.findEmpsByDeptno",
			fetchType=FetchType.LAZY))
	})
	@Select("SELECT * FROM dept")
	//注解的一对一查询，利用懒加载模式
    List<Dept> findAll();
	
}
