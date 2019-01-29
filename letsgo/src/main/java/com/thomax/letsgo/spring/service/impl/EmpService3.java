package com.thomax.letsgo.spring.service.impl;

import com.thomax.letsgo.spring.dao.IEmpDao;
import com.thomax.letsgo.spring.dao.IEmpTransferDao;
import com.thomax.letsgo.spring.domain.Emp;
import com.thomax.letsgo.spring.service.IEmpService;
import com.thomax.letsgo.spring.service.IEmpTransferService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

@Service("empService3")
public class EmpService3 implements IEmpService, IEmpTransferService {
	
	@Resource
	private IEmpDao empDao3;
	
	@Resource(name="empDao3")
	private IEmpTransferDao empTransferDao;
	
	@Override
	public List<Emp> findAll() {
		try {
			return empDao3.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int save(Emp emp) {
		try {
			return empDao3.save(emp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int update(Emp emp) {
		try {
			return empDao3.update(emp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int delete(int empno) {
		try {
			return empDao3.delete(empno);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Emp findEmpByNo(int empno) {
		try {
			return empDao3.findEmpByNo(empno);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean transfer(int from, int to, int money) {
		try {
			int out = empTransferDao.outMoney(from, money);
			System.out.println("out:" + out);
			//System.out.println(10 / 0);
			int in = empTransferDao.inMoney(to, money);
			System.out.println("in:" + in);
			if (out == 1 && in == 1) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
