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

    private static MethodHandle getPrintlnMH(Object receiver) {
        MethodType methodType = MethodType.methodType(void.class, String.class);
        try {
            return MethodHandles.lookup().findVirtual(receiver.getClass(), "println", methodType);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
