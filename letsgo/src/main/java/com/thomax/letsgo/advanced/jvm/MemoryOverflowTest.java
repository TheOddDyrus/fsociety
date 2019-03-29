package com.thomax.letsgo.advanced.jvm;

import com.thomax.letsgo.handling.UnsafeHandling;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import sun.misc.Unsafe;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试前请配置相对应的VM启动参数
 */
public class MemoryOverflowTest {
    public static void main(String[] args) {
        DirectOutOfMemory example = new DirectOutOfMemory();
        example.test();
    }
}

/**
 * 创建对象导致堆内存溢出
 * VM options： -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:/test.hprof
 *   ==> java.lang.OutOfMemoryError: Java heap space
 */
class HeapOutOfMemory {
    private static class TestObject {}
    public void test() {
        List<TestObject> list = new ArrayList<>();
        while (true) {
            list.add(new TestObject());
        }
    }
}

/**
 * 递归导致栈溢出
 * VM options： -Xss128k
 *   ==> Exception in thread "main" java.lang.StackOverflowError
 */
class StackOverflow {
    private int stackLength = 1;
    private void stackLeak() {
        stackLength++;
        stackLeak();
    }
    public void test() {
        try {
            stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length: " + stackLength);
            throw e;
        }
    }
}

/**
 * 创建线程导致内存溢出（测试此案例可能导致电脑死机）
 * VM options： -Xss2m
 *   ==> Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
 */
class ThreadOutOfMemory {
    private void dontStop() {
        while (true) { }
    }
    public void test() {
        while (true) {
            Thread thread = new Thread(this::dontStop);
            thread.start();
        }
    }
}

/**
 * 创建运行时常量池内存导致内存溢出
 * JDK1.7 VM options： -XX:PermSize=10m -XX:MaxPermSize=10m  （JDK7是永久带）
 *   ==> Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
 * JDK1.8 VM options： -XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m  （JDK8是元数据区）
 *   ==> Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
 */
class ConstantPoolOutOfMemory {
    public void test() {
        List<String> list = new ArrayList<>(); //利用list进行引用让垃圾回收无法进行
        int i = 0;
        while (true) {
            list.add((++i + "for test").intern());
        }
    }
    /*字符串引用测试：JDK1.6以上的String.intern()不会再复制实例，而是复制引用（"java"这个字符串已经存在于常量池，intern()当首次出现才会返回true）*/
    public void extendTest() {
        String str1 = new StringBuilder("佳").append("哇").toString();
        System.out.println(str1.intern() == str1); //JDK1.6->false, JDK1.7->true

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2); //JDK1.6->false, JDK1.7->false
    }

}

/**
 * 方法区内存溢出：借助cglib直接操作字节码运行时生成大量动态类。这类场景还有大量JSP或动态产生JSP应用、基于OSGI的应用（即使是同一个类文件，被不同加载器加载也会视为不同的类）
 * （方法区用于保存Class的相关信息，例如类名、访问修饰符、常量池、字段描述、方法描述等）
 * JDK1.7 VM options： -XX:PermSize=10m -XX:MaxPermSize=10m  （JDK7是永久带）
 *   ==> Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
 * JDK1.8 VM options： -XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m  （JDK8是元数据区中的类指针压缩空间）
 *   ==> Exception in thread "main" java.lang.OutOfMemoryError: Compressed class space
 */
class ClassOutOfMemory {
    private static class TestObject {}
    public void test() {
        while (true) {
            String[] args = new String[0]; //replace args from main method
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(TestObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> methodProxy.invoke(objects, args));
            enhancer.create();
        }
    }
}

/**
 * 本机直接内存溢出
 * （不会抛出很明显异常，如果输出了dump文件很小且程序直接或间接使用了NIO，可以考虑检查一下是不是这类方式导致的内存溢出）
 * VM options： -Xmx20m -XX:MaxDirectMemorySize=10m  （MaxDirectMemorySize如果不指定，则默认与Java堆的最大值 -Xmx 一样）
 *   ==> Exception in thread "main" java.lang.OutOfMemoryError
 */
class DirectOutOfMemory {
    public void test() {
        long _1MB = 1024 * 1024;
        Unsafe unsafe = UnsafeHandling.getInstance();
        if (unsafe == null) return;

        while (true) {
            long _address = unsafe.allocateMemory(_1MB); //参数单位是字节，每次向系统申请1MB的内存，_address是内存地址的起始地址
        }
    }
}





