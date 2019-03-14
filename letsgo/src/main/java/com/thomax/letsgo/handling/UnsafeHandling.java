package com.thomax.letsgo.handling;

/**
 * Unsafe类是在sun.misc包下，不属于Java标准。但是很多Java的基础类库，包括一些被广泛使用的高性能开发库都是基于Unsafe类开发的，
 * 比如Netty、Cassandra、Hadoop、Kafka等。Unsafe类在提升Java运行效率，增强Java语言底层操作能力方面起了很大的作用
 * 1.内存管理
 * 2.非常规的对象实例化
 * 3.操作类、对象、变量
 * 4.数组操作
 * 5.多线程同步
 * 6.挂起与恢复
 * 7.内存屏障
 */
public class UnsafeHandling {
    /*
    private Unsafe unsafe;

    public UnsafeHandling() throws IllegalAccessException, NoSuchFieldException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        unsafe = (Unsafe) f.get(null);
    }

    public Unsafe getUnsafe() {
        return unsafe;
    }*/

}
