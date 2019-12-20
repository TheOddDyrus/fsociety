package com.thomax.letsgo.zoom.mybatis.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtils {
	
	//获得SqlSessionFactory对象
	private static SqlSessionFactory getSqlSessionFactory(String config) {
		try {
			InputStream is = Resources.getResourceAsStream(config);
			return new SqlSessionFactoryBuilder().build(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//获得SqlSession对象
	public static SqlSession getSqlSession() {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory("SqlMapConfig.xml");
		return sqlSessionFactory.openSession();
	}
	
	//获得SqlSession对象(isAutoCommit判断是否自动提交)
	public static SqlSession getSqlSession(boolean isAutoCommit) {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory("SqlMapConfig.xml");
		return sqlSessionFactory.openSession(isAutoCommit);
	}
	
}
