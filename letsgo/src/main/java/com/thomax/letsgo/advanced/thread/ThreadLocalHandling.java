package com.thomax.letsgo.advanced.thread;

import com.thomax.letsgo.jdbc.util.DBUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Thread类中存在一个Map<ThreadLocal<XXX>, XXX> threadLocals的属性
 * 当通过ThreadLocal<XXX>来保存线程封闭所需对象时，以这个ThreadLocal<XXX> this为key保存所需对象到上面的threadLocals中，
 * 这样就避免了在调用每个方法时都要传递执行上下文信息来获得对象，稍微进行了耦合
 * 也可以看做ThreadLocal<XXX>是Thread的工具类，属于外观设计模式
 */
public class ThreadLocalHandling {

    //ThreadLocal.get()的时候会调用initialValue()得到value和以自身为key保存到当前线程的threadLocals中（JDK1.8的写法）
    private static ThreadLocal<Connection> connectionHolder2 = ThreadLocal.withInitial(DBUtils::getConnection2);

    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
        @Override
        public Connection initialValue() { //ThreadLocal.get()的时候会调用initialValue()得到value和以自身为key保存到当前线程的threadLocals中（JDK1.7的写法）
            return DBUtils.getConnection();
        }
    };

    public static Connection getConnection() {
        return connectionHolder.get();
    }
    public static Connection getConnection2() {
        return connectionHolder2.get();
    }

    //ThreadLocalMap中Entry继承自WeakReference<ThreadLocal<?>，不要直接将connectionHolder设为null，这样无法有效gc回收Entry内的引用
    public static void removeConnection() {
        connectionHolder.remove();
    }
    public static void removeConnection2() {
        connectionHolder2.remove();
    }
}

class UseThreadLocal {

    public void exec() throws SQLException {
        Connection connection = ThreadLocalHandling.getConnection();
        Connection connection2 = ThreadLocalHandling.getConnection2();
        connection.setAutoCommit(false);
        connection2.setAutoCommit(false);
        /*
        中间的增删改查操作
         */
        connection.commit();
        connection2.commit();
        connection.close();
        connection2.close();
    }
}
