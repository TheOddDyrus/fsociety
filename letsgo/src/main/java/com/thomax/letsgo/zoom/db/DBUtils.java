package com.thomax.letsgo.zoom.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 原生数据库工具
 *   Connection  连接接口
 *   Statement  命令接口
 *   ResultSet  结果集接口
 */
public class DBUtils {

	private static String DB_URL;

	private static String DB_USER;

	private static String DB_PWD;
	
	public static ThreadLocal<Connection> CONNECT_MANAGER = new ThreadLocal<>();
	
	static {
		try {
			Properties prop = new Properties();
			prop.load(DBUtils.class.getClassLoader().getResourceAsStream("db.properties"));

			DB_URL = prop.getProperty("db.url");
			DB_USER = prop.getProperty("db.user");
			DB_PWD = prop.getProperty("db.pwd");

			Class.forName(prop.getProperty("db.driver"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//select
		List<Result> select = select("SELECT xx FROM table1");

		//modify
		int row = modify("UPDATE table1 SET xx = xx WHERE xx");

		//multi modify
		try {
			beginTransaction();
			int row2 = multiModify("UPDATE table2 SET xx = xx WHERE xx");
			//doing something
			int row3 = multiModify("UPDATE table3 SET xx = xx WHERE xx");
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
		} finally {
			closeTransaction();
		}
	}

	/**
	 * SELECT
	 */
	public static List<Result> select(String sql) {
		List<Result> resultList = new ArrayList<>();

		try (Connection connection = getConnection();
			 Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				Result result = new Result();
				result.setId(resultSet.getObject("id", Long.class));
				result.setName(resultSet.getObject("name", String.class));
				resultList.add(result);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return resultList;
	}

	/**
	 * INSERT/UPDATE/DELETE（不需要事务）
	 */
	public static int modify(String sql) {
		try (Connection connection = getConnection();
			 Statement statement = connection.createStatement()) {
			return statement.executeUpdate(sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * INSERT/UPDATE/DELETE（需要事务）
	 */
	public static int multiModify(String sql) {
		Connection connection = CONNECT_MANAGER.get();
		try (Statement statement = connection.createStatement()) {
			return statement.executeUpdate(sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得数据库连接
	 */
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 开启事务
	 */
	public static void beginTransaction() {
		try {
			Connection connection = getConnection();
			connection.setAutoCommit(false);
			CONNECT_MANAGER.set(connection);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 提交事务
	 */
	public static void commitTransaction() {
		try {
			Connection connection = CONNECT_MANAGER.get();
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 回退事务
	 */
	public static void rollbackTransaction() {
		try {
			Connection connection = CONNECT_MANAGER.get();
			connection.rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 关闭事务
	 */
	public static void closeTransaction() {
		try {
			Connection connection = CONNECT_MANAGER.get();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			CONNECT_MANAGER.remove();
		}
	}

	public static class Result {
		private Long Id;
		private String Name;

		public Long getId() {
			return Id;
		}
		public void setId(Long id) {
			Id = id;
		}
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			Name = name;
		}
	}

}
