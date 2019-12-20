package com.thomax.letsgo.zoom.jdbc.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/**SUN公司制定了jdbc规范：
 * 		DriverManager类
 * 一套接口规范：
 * 		Connection 连接接口
 * 		Statement  命令接口
 * 		ResultSet  结果集接口
 * @author _thomas
 */
public class DBUtils {
	
	private static String JDBC_DRIVER;
	private static String DB_URL;
	private static String USER;
	private static String PWD;
	
	public static ThreadLocal<Connection> localConn = new ThreadLocal<Connection>();
	
	static {
		try {
			/**使用ClassLoader的get Resource方法获得配置文件的io输入流*/
			Properties prop = new Properties();
			//InputStream is = ClassLoader.getSystemResourceAsStream("db.properties"); //读取src目录的文件
			InputStream is = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
			prop.load(is);
			JDBC_DRIVER = prop.getProperty("db.driver");
			DB_URL = prop.getProperty("db.url");
			USER = prop.getProperty("db.user");
			PWD = prop.getProperty("db.pwd");

			/*
			Class.forName("");的作用是要求JVM查找并加载指定的类，如果在类中有静态初始化器的话，JVM必然会执行该类的静态代码 段。
			而在JDBC规范中明确要求这个Driver类必须向DriverManager注册自己，即任何一个JDBC Driver的 Driver类的代码都必须类似如下： 
			DriverManager.registerDriver(new OracleDriver());  //等效于Class.forName("oracle.jdbc.driver.OracleDriver");
			*/			
			//将驱动类的class文件装载到内存中，并且形成一个描述此驱动类结构的Class类实例，并且初始化此驱动类，这样jvm就可以使用它了
			Class.forName(JDBC_DRIVER);
		} catch (Exception e) {
			System.out.println("DBUtils静态代码段异常：" + e.getMessage());
		}
	}
	
	/**
	 * 适用于SELECT：
	 * 后续用close(Statement stmt, Connection connection, ResultSet rs)来关闭此事务
	 * @return Connection 非线程事务
	 */
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(DB_URL, USER, PWD);
		} catch (SQLException e) {
			System.out.println("DriverManager获得Connection失败：" + e.getMessage());
		}
		return null;
	}

	public static Connection getConnection2() {
		try {
			return DriverManager.getConnection(DB_URL + "另一个数据库url", USER, PWD);
		} catch (SQLException e) {
			System.out.println("DriverManager获得Connection失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 关闭Statement，Connection，ResultSet
	 * @param stmt
	 * @param connection
	 * @param rs
	 */
	public static void close(Statement stmt, Connection connection, ResultSet rs) {
		try {
			if (connection != null) {
				connection.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println("JDBC规范接口关闭异常：" + e.getMessage());
		}
	}
	
	/**
	 * 开启非自动Commit的事务保存至本地静态变量localConn
	 */
	public static void beginNoAutoTransaction() {
		try {
			Connection conn = DBUtils.getConnection();
			conn.setAutoCommit(false);
			localConn.set(conn);
		} catch (SQLException se) {
			System.out.println("开启非自动Commit事务发生异常：" + se.getMessage());
		}
	}
	
	/**
	 * 提交事务
	 */
	public static void commit() {
		try {
			Connection conn = localConn.get();
			if (conn != null) {
				conn.commit();
				conn.close();
				localConn.remove();
			}
		} catch (SQLException se) {
			System.out.println("事务Commit时发生异常：" + se.getMessage());
		}
	}
	
	/**
	 * 回退事务
	 */
	public static void rollback() {
		try {
			Connection conn = localConn.get();
			if (conn != null) {
				conn.rollback();
				conn.close();
				localConn.remove();
			}
		} catch (SQLException se) {
			System.out.println("事务rollback时发生异常：" + se.getMessage());
		}
	}
	
	/**
	 * 适用于INSERT, DELETE, UPDATE：
	 * 得到本地多线程事务；后续用必须要用commit()或rollback()来处理此事务，
	 * 在使用commit()或rollback()时会关闭此事务
	 * @return Connection 多线程事务
	 */
	public static Connection getLocalConnection() {
		return localConn.get();
	}

}
