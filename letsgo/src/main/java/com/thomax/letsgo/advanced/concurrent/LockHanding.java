package com.thomax.letsgo.advanced.concurrent;

import org.junit.runner.notification.RunListener.ThreadSafe;

import java.util.*;

public class LockHanding {
}

/**
 * 对象锁和类锁
 */
@ThreadSafe
class ObjectLock {
    private final ObjectLock objLock = new ObjectLock();

    public void exec() {
        synchronized (objLock) {
            //对象锁只会锁住这个对象实例，因为相同类型对象可以有多个
        }
    }

    public void exec2() {
        synchronized (ObjectLock.class) {
            //类锁作用域为整个类，例如声明在静态方法上的synchronized也是持有的类锁
        }
    }
}

/**
 * synchronized是互斥锁，从继承角度来看也具有可重入性
 */
class OriginalClass {
    public synchronized void exec() {}
}
class ReentrantClass extends OriginalClass {
    public synchronized void exec() {
        System.out.println("doing something");
        super.exec(); //如果不具有可重入性当线程在子类方法中调用父类方法，将产生死锁
    }
}

/**
 * Collections集合工具类中通过包装器工厂方法synchronizedXX()来new出自己的一个实现了以待同步对象为互斥锁（mutex）的静态内部类
 * 这也是一种通过封闭机制来让非线程安全类变为线程安全的方式
 */
class synchronizedUtil<K, V> {
    public Map<K, V> getThreadSafeMap(Map<K, V> map) {
        return Collections.synchronizedMap(map);
    }

    public Set<V> getThreadSafeSet(Set<V> set) {
        return Collections.synchronizedSet(set);
    }

    public List<V> getThreadSafeList(List<V> list) {
        return Collections.synchronizedList(list);
    }
}
