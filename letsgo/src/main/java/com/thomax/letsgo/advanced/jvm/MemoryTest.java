package com.thomax.letsgo.advanced.jvm;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 测试前请配置相对应的VM启动参数
 */
public class MemoryTest {
    public static void main(String[] args) {
        AA example = new AA();
        example.test();
    }
}

/**
 * 无限创建对象导致堆内存溢出
 * VM options： -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:/test.hprof
 */
class HeapOutOfMemory {
    private static class OomObject {}
    public void test() {
        List<OomObject> list = new ArrayList<>();
        while (true) {
            list.add(new OomObject());
        }
    }
}

/**
 * 无限递归导致栈溢出
 * VM options： -Xss128k
 */
class StackOverFlow {
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
 * unable to create new native thread（测试此案例可能导致电脑死机）
 * VM options： -Xss2m
 */
class ThreadOutOfMemory {
    private void dontStop() {
        while (true) { }
    }
    public void test() {
        while (true) {
            Thread thread = new Thread(() -> dontStop());
            thread.start();
        }
    }
}

/**
 * 无限创建方法区内存导致内存溢出
 * JDK1.7 VM options： -XX:PermSize=10m -XX:MaxPermSize=10m
 * JDK1.8 VM options： -XX:PermSize=10m -XX:MaxPermSize=10m
 */
class AA {
    private List<String> list = new ArrayList<>();
    private int i = 0;
    /*第一种：常量池*/
    public void test() {
        while (true) {
            list.add(String.valueOf(i).intern());
        }
    }
    /**/
    public void test2() {
        try {
            Set<Class<?>> classes = new HashSet<>();
            URL url = new File("").toURI().toURL();
            URL[] urls = new URL[]{url};
            while (true) {
                ClassLoader loader = new URLClassLoader(urls);
                Class<?> loadClass = loader.loadClass(Object.class.getName());
                classes.add(loadClass);
            }
        } catch (ClassNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}





