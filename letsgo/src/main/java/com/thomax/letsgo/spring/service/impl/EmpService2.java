package com.thomax.letsgo.spring.service.impl;

import com.thomax.letsgo.spring.dao.IEmpDao;
import com.thomax.letsgo.spring.domain.Emp;
import com.thomax.letsgo.spring.service.IEmpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

@Service("empService2")
public class EmpService2 implements IEmpService {
	
	@Resource
	private IEmpDao empDao2;
	
	@Override
	public List<Emp> findAll() {
		try {
			return empDao2.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int save(Emp emp) {
		try {
			return empDao2.save(emp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int update(Emp emp) {
		try {
			return empDao2.update(emp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int delete(int empno) {
		try {
			return empDao2.delete(empno);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Emp findEmpByNo(int empno) {
		try {
			return empDao2.findEmpByNo(empno);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
