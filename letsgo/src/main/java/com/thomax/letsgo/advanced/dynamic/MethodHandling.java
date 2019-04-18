package com.thomax.letsgo.advanced.dynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 方法句柄
 */
public class MethodHandling {

    static class ClassX {
        public void println(String s) {
            System.out.println(s);
        }
    }

    private static MethodHandle getPrintlnMH(Object receiver) throws NoSuchMethodException, IllegalAccessException {
        MethodType methodType = MethodType.methodType(void.class, String.class); //1.返回值类型, 2.方法中参数类型...
        return MethodHandles.lookup().findVirtual(receiver.getClass(), "println", methodType);
    }

    public static void main(String[] args) throws Throwable {
        Object obj1 = new ClassX();
        getPrintlnMH(obj1).invokeExact("ClassX");
        Object obj2 = System.out;
        getPrintlnMH(obj2).invokeExact("System.out");
    }

}
