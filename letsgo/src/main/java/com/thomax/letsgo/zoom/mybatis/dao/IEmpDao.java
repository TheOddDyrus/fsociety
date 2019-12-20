package com.thomax.letsgo.zoom.mybatis.dao;

import com.thomax.letsgo.zoom.mybatis.domain.Emp;

import java.util.List;

public interface IEmpDao {

	List<Emp> findAll(String mapperRelation);
	List<Emp> findEmpsByFuzzy(String mapperRelation, String fuzzy);
	void save(String mapperRelation, Emp emp);
	void remove(String mapperRelation, int empno);
}
