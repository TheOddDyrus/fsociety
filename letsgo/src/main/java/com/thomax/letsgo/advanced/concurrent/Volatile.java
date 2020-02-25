package com.thomax.letsgo.advanced.concurrent;

/**
 * volatile关键字的2个主要功能：
 * 1.屏蔽指令重排序（DLC）优化
 *   （指令重排序：指CPU采用了允许将多条指令不按程序规定的顺序分开发送给各个相应的电路单元处理，只有非依赖性指令之间才会重排，最终只要获得一致的结果即可。
 *                比如算数的多元运算可以重排；比如单例设计模式com.thomax.letsgo.design.pattern.create.Singleton中对象创建，当多线程访问时，
 *                如果不加volatile会出现new一个对象以后这个对象还未实例化完成但是对象!=null，致后面线程直接获得未实例化完成的对象）
 * 2.通过反汇编码中的lock可以看出锁了CPU的缓存行，利用缓存一致性协议让CPU多级缓存中的数据失效从而重新去内存里面加载数据
 *
 * =>发展：volatile是在synchronized性能低下的时候提出的。如今在JDK6的synchronized关键字的性能被大幅优化之后，更是几乎没有使用它的场景
 */
public class Volatile { }

/**
 * Java内存模型的有序性：
 *   如果在本地线程内观察，所有操作都是有序的（Within-Thread As-If-Serial Semantics）
 *   如果在一个线程观察另外一个线程，所有操作都是无序的，比如 "指令重排序" 和 "工作内存与主内存同步延迟"
 *
 * happens-before先行发生原则：
 *   这个是Java内存模型中定义的两项操作之间的偏序关系，如果说操作A先行发生于操作B，其实就是说在发生操作B之前，操作A产生"影响"能被操作B观察到，
 *   "影响"包括修改了内存中共享的值、发送了消息、调用了方法等。
 */
class Orderly { }
