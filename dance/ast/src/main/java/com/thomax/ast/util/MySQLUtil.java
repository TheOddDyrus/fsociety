package com.thomax.ast.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MySQLUtil {

	private final static Map<String, Connection> POOL = new HashMap<>();

	public final static ThreadLocal<ClosedObject> CLOSED_MANAGE = new ThreadLocal<>();

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
		String dbKey = prop.getProperty("addr") + prop.getProperty("port") + prop.getProperty("database");
		Connection connection = POOL.get(dbKey);
		if (connection == null) {
			String dbUrl = "jdbc:mysql://" + prop.getProperty("addr") + ":" + prop.getProperty("port") + "/"
					+ prop.getProperty("database") + "?useUnicode=true&characterEncoding=utf-8";
			connection = DriverManager.getConnection(dbUrl, prop.getProperty("user"), prop.getProperty("pwd"));
			POOL.put(dbKey, connection);
		}

		return connection;
	}

	/**
	 * 执行mysql获得结果集
	 *
	 * @param sql 生成的单表查询SQL
	 * @param prop 数据库属性
	 *
	 * @throws Exception
	 */
	public static ResultSet execMySQL(String sql, Properties prop) throws Exception {
		try {
			Connection connection = getConnection(prop);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);

			CLOSED_MANAGE.set(new ClosedObject(statement, resultSet));

			return resultSet;
		} catch (SQLException e) {
			throw new Exception("执行MySQL查询时发生异常：" + e.getMessage());
		}
	}

	/**
	 * 关闭
	 *
	 * @param resultSet
	 * @param statement
	 */
	public static void release() throws Exception {
		ClosedObject local = CLOSED_MANAGE.get();

		if (local.getResultSet() != null) {
			local.getResultSet().close();
		}

		if (local.getStatement() != null) {
			local.getStatement().close();
		}
	}

	private static class ClosedObject {
		private Statement statement;
		private ResultSet resultSet;

		public ClosedObject(Statement statement, ResultSet resultSet) {
			this.statement = statement;
			this.resultSet = resultSet;
		}

		public Statement getStatement() {
			return statement;
		}

		public void setStatement(Statement statement) {
			this.statement = statement;
		}

		public ResultSet getResultSet() {
			return resultSet;
		}

		public void setResultSet(ResultSet resultSet) {
			this.resultSet = resultSet;
		}
	}

}
