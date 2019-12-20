package com.thomax.letsgo.zoom.spring.dao.impl;

import com.thomax.letsgo.zoom.spring.dao.IEmpDao;
import com.thomax.letsgo.zoom.spring.domain.Emp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("empDao2")
public class EmpDao2 implements IEmpDao, RowMapper<Emp> {

	@Resource
	private JdbcTemplate jdbcTemplateX; //定义jdbc模板对象
	
	@Override
	//实现RowMapper<T>接口的映射方法
	public Emp mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Emp(rs.getInt("empno"), rs.getString("ename"), rs.getString("job"), rs.getInt("mgr"), 
				rs.getString("hiredate"), rs.getInt("sal"), rs.getInt("comm"), rs.getInt("deptno"));
	}
	
	@Override
	public List<Emp> findAll() throws SQLException {
		System.out.println("方案1");
		String sql = "SELECT * FROM emp";
		return jdbcTemplateX.query(sql, this);
	}

	@Override
	public int save(Emp emp) throws SQLException {
		String sql = "INSERT INTO emp VALUES(?,?,?,?,?,?,?,?)";
		return jdbcTemplateX.update(sql, emp.getEmpno(), emp.getEname(), emp.getJob(), emp.getMgr(),
					emp.getHiredate(), emp.getSal(), emp.getComm(), emp.getDeptno());
	}

	@Override
	public int update(Emp emp) throws SQLException {
		String sql = "UPDATE emp SET ename=?,job=?,mgr=?,hiredate=?,sal=?,comm=?,deptno=? WHERE empno=?";
		return jdbcTemplateX.update(sql, emp.getEname(), emp.getJob(), emp.getMgr(),
					emp.getHiredate(), emp.getSal(), emp.getComm(), emp.getDeptno(), emp.getEmpno());
	}

	@Override
	public int delete(int empno) throws SQLException {
		String sql = "DELETE FROM emp WHERE empno=?";
		return jdbcTemplateX.update(sql, empno);
	}

	@Override
	public Emp findEmpByNo(int empno) throws SQLException {
		String sql = "SELECT * FROM emp WHERE empno=?";
		return jdbcTemplateX.queryForObject(sql, this, empno);
	}

}
