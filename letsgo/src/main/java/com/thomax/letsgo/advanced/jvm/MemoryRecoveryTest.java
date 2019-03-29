package com.thomax.letsgo.advanced.jvm;

public class MemoryRecoveryTest {
    public static void main(String[] args) {
        Finalize4GC example = new Finalize4GC();
        example.test();

    }
}

class Finalize4GC {
    private static Finalize4GC hook = null;
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize()被执行");
        Finalize4GC.hook = this;
    }
    public void test() {
        try {
            hook = new Finalize4GC();
            hook = null;
            System.gc();
            hook = new Finalize4GC();
            Thread.sleep(500);
            System.out.println("hook第一次是否被回收：" +  (hook == null));

            hook = null;
            System.gc();
            System.out.println("hook第二次是否被回收：" +  (hook == null));
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
