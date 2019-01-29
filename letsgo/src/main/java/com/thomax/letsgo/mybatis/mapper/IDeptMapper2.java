package com.thomax.letsgo.mybatis.mapper;

import com.thomax.letsgo.mybatis.domain.Dept;
import com.thomax.letsgo.mybatis.domain.DeptCustom;

import java.util.List;

public interface IDeptMapper2 {
	
	List<DeptCustom> findAllDepts1();
	
	List<Dept> findAllDepts2();
	
	List<Dept> findAllDepts3();
	
	List<Dept> findAllDepts4();
}
