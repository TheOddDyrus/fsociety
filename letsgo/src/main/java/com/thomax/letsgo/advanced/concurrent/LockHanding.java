package com.thomax.letsgo.advanced.concurrent;

public class LockHanding {
}

/**
 * 对象锁和类锁
 */
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
