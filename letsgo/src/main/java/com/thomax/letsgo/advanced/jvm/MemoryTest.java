package com.thomax.letsgo.advanced.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试前请配置VM启动参数
 */
public class MemoryTest {
    public static void main(String[] args) {
        StackSof example = new StackSof();
        example.test();
    }
}

/**
 * 无限创建对象导致堆内存溢出
 * VM options: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:/test.hprof
 */
class HeapOom {
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
 * VM options: -Xss128k
 */
class StackSof {
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