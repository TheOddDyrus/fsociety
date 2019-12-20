package com.thomax.letsgo.zoom.spring.dao.impl;

import com.thomax.letsgo.zoom.spring.dao.IEmpDao;
import com.thomax.letsgo.zoom.spring.domain.Emp;
import com.thomax.letsgo.zoom.spring.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository("empDao1")
public class EmpDao implements IEmpDao {
	
	QueryRunner qr;
	
	public EmpDao() {
		qr = new QueryRunner(JdbcUtils.getDataSource());
	}

	@Override
	public List<Emp> findAll() throws SQLException {
		String sql = "SELECT * FROM emp";
		return qr.query(sql, new BeanListHandler<>(Emp.class));
	}

	@Override
	public int save(Emp emp) throws SQLException {
		String sql = "INSERT INTO emp VALUES(?,?,?,?,?,?,?,?)";
		return qr.update(sql, emp.getEmpno(), emp.getEname(), emp.getJob(), emp.getMgr(),
					emp.getHiredate(), emp.getSal(), emp.getComm(), emp.getDeptno());
	}

	@Override
	public int update(Emp emp) throws SQLException {
		String sql = "UPDATE emp SET ename=?,job=?,mgr=?,hiredate=?,sal=?,comm=?,deptno=? WHERE empno=?";
		return qr.update(sql, emp.getEname(), emp.getJob(), emp.getMgr(),
					emp.getHiredate(), emp.getSal(), emp.getComm(), emp.getDeptno(), emp.getEmpno());
	}

	@Override
	public int delete(int empno) throws SQLException {
		String sql = "DELETE FROM emp WHERE empno=?";
		return qr.update(sql, empno);
	}

	@Override
	public Emp findEmpByNo(int empno) throws SQLException {
		String sql = "SELECT * FROM emp WHERE empno=?";
		return qr.query(sql, new BeanHandler<>(Emp.class), empno);
	}

}
