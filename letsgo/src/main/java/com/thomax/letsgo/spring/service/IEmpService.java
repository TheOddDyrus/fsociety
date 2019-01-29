package com.thomax.letsgo.spring.service;

import com.thomax.letsgo.spring.domain.Emp;

import java.util.List;

public interface IEmpService {

	List<Emp> findAll();
	int save(Emp emp);
	int update(Emp emp);
	int delete(int empno);
	Emp findEmpByNo(int empno);
	
}
