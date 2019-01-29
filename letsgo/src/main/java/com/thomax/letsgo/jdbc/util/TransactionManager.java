package com.thomax.letsgo.jdbc.util;

import java.sql.Connection;
import java.sql.SQLException;

/**利用ThreadLoacl类来管理事务连接：
 * 让同一个线程使用同一个Connection对象
 * @author _thomas
 */
public class TransactionManager {
	
	private static ThreadLocal<Connection> local = new ThreadLocal<Connection>();
	
	public static void beginTransaction() throws SQLException {
		Connection conn = DBUtils.getConnection();
		conn.setAutoCommit(false);
		local.set(conn);
	}
	
	public static void commit() throws SQLException {
		Connection conn = local.get();
		if (conn != null) {
			conn.commit();
			conn.close();
			local.remove();
		}
	}
	
	public static void rollback() throws SQLException {
		Connection conn = local.get();
		if (conn != null) {
			conn.rollback();
			conn.close();
			local.remove();
		}
	}
	
	public static void close() throws SQLException {
		Connection conn = local.get();
		if (conn != null) {
			conn.close();
			local.remove();
		}
	}
	
	public static Connection getConnection() {
		return local.get();
	}
	
}
