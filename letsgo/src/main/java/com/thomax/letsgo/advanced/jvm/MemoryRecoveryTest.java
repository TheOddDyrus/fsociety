package com.thomax.letsgo.advanced.jvm;

import java.lang.ref.WeakReference;

/**
 * 内存回收四种引用判定：
 * 1.强引用：类似Object obj = new Object(); 只要obj存在对象永远不会被回收
 * 2.软引用：JDK1.2以后提供了SoftReference类，继承此类的对象在发生内存溢出时，会被统一进行一次回收，如果还是内存溢出则抛出内存溢出异常
 * 3.弱引用：JDK1.2以后提供了WeakReference类，继承此类的对象只能生存到下一次内存回收，无论内存是否足够这种对象都会被回收成功
 * 4.虚引用：JDK1.2以后提供了PhantomReference类，继承此类的对象跟普通对象的生命周期没有任何区别，唯一的目的就是被回收时收到一个系统通知
 */
public class MemoryRecoveryTest {
    public static void main(String[] args) {
        SoltRecovery example = new SoltRecovery();
        example.test();
    }
}

/**
 * 使用引用的例子
 */
class ReferenceExample<T> extends WeakReference {
    public ReferenceExample(T t) {
        super(t); //第一种：将T设为软引用（ThreadLocalMap中的Entry实例化的时候也是这样操作）
    }

    /*第二种：直接创建WeakReference对象来get软引用对象*/
    public void test() {
        WeakReference<Hello> helloHandle = new WeakReference<>(new Hello());
        Hello hello = helloHandle.get();
    }
    private class Hello {}
}

/**
 * finalize()不是C/C++的析构函数，而是Java刚诞生时为了使C/C++程序员更容易接受它的一种妥协，它的运行代价高昂，不确定性大，无法保证各个对象的调用顺序。
 * 所以使用finalize()来做的工作，可以用try-finally来做的更好，直接就废弃finalize()吧
 */
class Finalize4GC {
    private static Finalize4GC hook = null;
    /*如果这个对象被判定有必要执行finalize()时会放在F-Queue队列中，然后经过一个低优先级的Finalizer线程去执行。第一次标记不会被回收，第二次标记的时候会从F-Queue中移除，最终被回收*/
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize()被执行");
        Finalize4GC.hook = this; /*虽然之前hook的引用不存在了但是这个对象还是存在的（重新建立GC Roots引用链，GC Roots如下：1.虚拟机栈（帧栈中的本地变量表）中引用的对象
                                                                                                                      2.方法区中类静态属性引用的对象
                                                                                                                      3.方法区中常中常量引用的对象
                                                                                                                      4.本地方法栈中JNI（native方法）引用的对象）*/
    }
    public void test() {
        try {
            hook = new Finalize4GC();
            hook = null;
            System.gc();
            Thread.sleep(500); //finalize()的优先级很低，所以等待一下下finalize()一定执行成功
            System.out.println("hook第一次是否被回收：" +  (hook == null)); //false

            hook = null;
            System.gc();
            System.out.println("hook第二次是否被回收：" +  (hook == null)); //true
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}

/**
 * 帧栈空间的局部变量表中的Slot的复用：尽可能在帧栈长时间不能回收时，将占内存较大的且下游不使用的对象设为null
 * JVM options: -verbose:gc （查看gc详情）
 */
class SoltRecovery {
    public void test() {
        {
            byte[] placeHolder = new byte[64 * 1204 * 1024];
        }
        int a = 0; //placeHolder所占用的Slot会被这个变量a复用，所以会成功回收placeHolder的内存（如果没有这句placeHolder的内存回收不会成功），同理直接将placeHolder设为null也是一样的
        System.gc();
    }
}
