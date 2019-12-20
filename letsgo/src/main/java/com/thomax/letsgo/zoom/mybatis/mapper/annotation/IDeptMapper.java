package com.thomax.letsgo.zoom.mybatis.mapper.annotation;

import com.thomax.letsgo.zoom.mybatis.domain.Dept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IDeptMapper {
	
	//@Results配置结果集映射，只要定义一次即可，下面可以用deptRS作为映射结果集
	@Results(id="deptRS", value={@Result(column="deptno",id=true, property="deptno",javaType= Integer.class),
								@Result(column="dname", property="dnameX",javaType= String.class),
								@Result(column="loc", property="locX",javaType= String.class)})
	@Select("SELECT * FROM dept")
    List<Dept> findAll();
	
	@Select("SELECT * FROM dept WHERE deptno=#{deptno}")
	@ResultMap("deptRS")
	Dept findDeptByDeptno(int deptno);
	
	@Select("SELECT * FROM dept WHERE dname=#{hah} and deptno=#{007}")
	@ResultMap("deptRS")
	Dept findDeptByStmtIndex(@Param("007") int deptno, @Param("hah") String dname);
	
	/*如果主键使用到了自动增长列，则用如下写法：
	@SelectKey(statement="SELECT dept_seq.nextval FROM dual", before=true, keyProperty="deptno", resultType=Integer.class)
	*/
	/**如果使用#{}，参数格式是对象的话需要在里面指定为对象的属性名；如果参数格式非对象且只有一个参数，使用#{}可以随意指定名字*/
	@Insert("INSERT INTO dept VALUES(#{deptno}, #{dnameX}, #{locX})")
	@ResultMap("deptRS")
	void save(Dept dept);
	
	/**如果使用${}，只有一个参数需要在里面指定为value*/
	@Delete("DELETE FROM dept WHERE deptno=${value}")
	void remove(int deptno);
	
}
