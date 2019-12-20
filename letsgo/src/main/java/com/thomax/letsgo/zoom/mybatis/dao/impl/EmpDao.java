package com.thomax.letsgo.zoom.mybatis.dao.impl;

import com.thomax.letsgo.zoom.mybatis.dao.IEmpDao;
import com.thomax.letsgo.zoom.mybatis.domain.Emp;
import com.thomax.letsgo.zoom.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class EmpDao implements IEmpDao {

	@Override
	public List<Emp> findAll(String mapperRelation) {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		return sqlSession.selectList(mapperRelation);
	}

	@Override
	public List<Emp> findEmpsByFuzzy(String mapperRelation, String fuzzy) {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		return sqlSession.selectList(mapperRelation, fuzzy);
	}

	@Override
	public void save(String mapperRelation, Emp emp) {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		sqlSession.update(mapperRelation, emp);
	}

	@Override
	public void remove(String mapperRelation, int empno) {
		SqlSession sqlSession = MyBatisUtils.getSqlSession();
		sqlSession.update(mapperRelation, empno);
	}

}
