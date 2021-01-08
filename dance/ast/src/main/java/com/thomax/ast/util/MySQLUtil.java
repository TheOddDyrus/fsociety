package com.thomax.ast.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MySQLUtil {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("初始化MySQL驱动异常：" + e.getMessage());
		}
	}

	/**
	 * 获得MySQL数据库连接
	 * @param dbUrl
	 * @param user
	 * @param pwd
	 * @return
	 */
	public static Connection getConnection(Properties prop) throws SQLException {
		String dbUrl = "jdbc:mysql://" + prop.getProperty("addr") + ":" + prop.getProperty("port") + "/"
				+ prop.getProperty("database") + "?useUnicode=true&characterEncoding=utf-8";

		return DriverManager.getConnection(dbUrl, prop.getProperty("user"), prop.getProperty("pwd"));
	}

	
	/**
	 * 关闭Statement，Connection，ResultSet
	 * @param stmt
	 * @param connection
	 * @param rs
	 */
	public static void close(Statement stmt, Connection connection, ResultSet rs) throws Exception {
		if (connection != null) {
			connection.close();
		}
		if (stmt != null) {
			stmt.close();
		}
		if (rs != null) {
			rs.close();
		}
	}


}
