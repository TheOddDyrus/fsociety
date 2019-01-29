package com.thomax.letsgo.design.pattern.structure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

/**享元模式:
 *	>主要目的是实现对象的共享，即共享池，当系统中对象多的时候可以减少内存的开销，通常与工厂模式一起使用
 *  >当一个客户端请求时，享元工厂需要检查当前对象池中是否有符合条件的对象，如果有，就返回已经存在的对象，如果没有，则创建一个新对象
 */
public class Flyweight {
	public static void main(String[] args) throws SQLException {
		//享元模式的使用
		ConnectionPool cp = ConnectionPool.getInstance(); //获得连接池
		Connection conn = cp.getConnection();  //得到一个连接对象
		conn.getAutoCommit(); //操作连接对象...
		cp.release();  //返回连接对象到连接池 
	}
}

class ConnectionPool {  
	
    private Vector<Connection> pool;  //连接池
    
    private String url = "jdbc:mysql://localhost:3306/test";
    private String username = "root";
    private String password = "root";
    private String driverClassName = "com.mysql.jdbc.Driver";
  
    private int poolSize = 100;  
    private static ConnectionPool instance = null;  
    Connection conn = null;
  
    //初始化工作...
    private ConnectionPool() {  
        pool = new Vector<Connection>(poolSize);
  
        for (int i = 0; i < poolSize; i++) {  
            try {  
                Class.forName(driverClassName);
                conn = DriverManager.getConnection(url, username, password);
                pool.add(conn);  
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  
            } catch (SQLException e) {
                e.printStackTrace();  
            }  
        }  
    }  
  
    //返回连接对象到连接池 
    public synchronized void release() {  
        pool.add(conn);  
    }  
  
    //返回连接池中的第一个连接对象，并且删除连接池中的这个连接对象
    public synchronized Connection getConnection() {
        if (pool.size() > 0) {  
            conn = pool.get(0);  
            pool.remove(conn);  
            return conn;  
        } else {  
            return null;  
        }  
    }

	public static ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}
}  

