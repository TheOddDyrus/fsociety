package com.thomax.letsgo.advanced.dynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 方法句柄：
 * 反射是重量级 -> 反射是Java代码层次的方法调用，是Java对象的全面映像，只能作用于Java语言
 * 方法句柄是轻量级 -> 方法句柄是对字节码的方法指令调用的模拟，可以服务于所有作用于JVM虚拟机之上的语言
 */
public class MethodHandling {

    static class ClassX {
        public void println(long value) {
            System.out.println(value);
        }
    }

    /*获得绑定以后的的方法句柄*/
    private static MethodHandle getPrintlnMH(Object obj) throws NoSuchMethodException, IllegalAccessException {
        MethodType methodType = MethodType.methodType(void.class, long.class); //1.返回值类型, 2.形参类型列表...
        return MethodHandles.lookup().findVirtual(obj.getClass(), "println", methodType).bindTo(obj); //模拟了invokevirtual这个虚拟机指令
    }

    public static void main(String[] args) throws Throwable {
        Object obj; //通过业务逻辑确认动态的方法是存在的即可
        obj = new ClassX();
        getPrintlnMH(obj).invoke(123);
        getPrintlnMH(obj).invokeExact(123L);
        obj = System.out;
        getPrintlnMH(obj).invoke(456);
        getPrintlnMH(obj).invokeExact(456L);
        /* 与invokeExact方法不同，invoke方法允许更加松散的调用方式。它会尝试在调用的时候进行返回值和参数类型的转换工作。
        这是通过MethodHandle类的asType方法来完成的，asType方法的作用是把当前方法句柄适配到新的MethodType上面，并产生一个新的方法句柄。
        当方法句柄在调用时的类型与其声明的类型完全一致的时候，调用invoke方法等于调用invokeExact方法；否则，invoke方法会先调用asType方法来尝试适配到调用时的类型。
        如果适配成功，则可以继续调用。否则会抛出相关的异常。这种灵活的适配机制，使invoke方法成为在绝大多数情况下都应该使用的方法句柄调用方式
            ==>进行类型匹配的基本规则是对比返回值类型和每个参数的类型是否都可以相互匹配。假设源类型为S，目标类型为T，则基本规则如下：
            1、可以通过java的类型转换来完成，一般从子类转成父类，比如从String到Object类型
            2、可以通过基本类型的转换来完成，只能将类型范围的扩大，比如从int切换到long
            3、可以通过基本类型的自动装箱和拆箱机制来完成，例如从int到Integer
            4、如果S有返回值类型，而T的返回值类型为void，则S的返回值会被丢弃
            5、如果S的返回值是void，而T的返回值是引用类型，T的返回值会是null
            6、如果S的返回值是void，而T的返回值是基本类型，T的返回值会是0
        */
    }

}
