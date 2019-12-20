package com.thomax.letsgo.zoom.handling;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

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
    private Unsafe example() {
        return Unsafe.getUnsafe(); //这个方法限制了只有引导类加载器才会返回实例，设计者希望只有rt.jar中的类才能使用Unsafe，所以我们需要通过反射获取才能使用
    }

    public static Unsafe getInstance()  {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
