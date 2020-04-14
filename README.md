fsociety
=======
The name `fsociety` refers to **'free society'**

[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
[![Badge](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu/#/zh_CN)

    fsociety
        ├─bootspring SpringBoot案例
            ├─ example
            └─ test
        ├─cloudspring SpringCloud案例
            ├─ config
            ├─ eureka
            ├─ feign
            ├─ gateway
            ├─ provider1
            ├─ provider2
            ├─ ribbon
            └─ zuul
        └─letsgo
            └─ src.man.java.com.thomax.letsgo
                ├─advanced 高级
                    ├─arithmetic 算法
                        ├─ SortArithmetic.png  十大排序算法的性能分析
                        └─ ArithmeticTop10.java  十大经典算法
                    ├─cache 缓存
                        ├─ ZookeeperDistributedLock.java  Zookeeper实现的分布式锁
                        ├─ RedisDistributedLock.java  Redis实现的分布式锁
                        ├─ RedisReadWriteLock.java  Redisson实现的分布式读写锁
                        └─ CacheAnalysis  各大缓存优劣性分析
                    ├─concurrent 并发
                        ├─ AQSHandling.java  同步器原理与三个实现：闭锁、信号量、栅栏
                        ├─ CASHandling.java  CAS操作
                        ├─ ForkJoinHanding.java  并行操作
                        ├─ FutureHandling.java  异步操作
                        ├─ LockHanding.java  锁与synchronized的基本操作
                        ├─ Volatile.java  volatile、Within-Thread As-If-Serial Semantics、happens-before
                        └─ Overview  锁的总览
                    ├─dynamic 动态
                        ├─ Reflect.java  反射
                        ├─ ASM.java  字节码生成器
                        ├─ DynamicProxy.java  动态代理
                        └─ MethodHandling  方法句柄
                    ├─jvm 虚拟机
                        ├─ ClassLoaderTest.java.java  类加载测试
                        ├─ MemoryOverflowTest.java  内存溢出测试
                        ├─ MemoryRecoveryTest.java  内存回收测试
                        ├─ HotSpot  JDK默认虚拟机结构
                        └─ JVM  虚拟机分析
                    ├─mq 中间件
                        └─ MqAnalysis  各大中间件优劣势分析
                    ├─network 网络
                        ├─ EchoClientHandler.java  netty客户端
                        ├─ EchoServerHandler.java  netty服务端
                        ├─ NioServer.java  原生nio服务
                        ├─ NettyTelnet.java  netty案例演示
                        └─ Overview  netty结构总览
                    └─thread 多线程
                        ├─ ThreadPoolHandling  线程池的操作
                        ├─ ExecutorHandling.java  执行任务的操作
                        ├─ ThreadHandling.java  线程的操作
                        ├─ ThreadLocalHandling.java  线程封闭的操作
                        └─ ThreadPoolOptimize  线程池调优
                └─design 设计
                    ├─pattern 模式
                        ├─behavior 行为型
                            ├─ ChainOfResponsibility.java  责任链模式
                            ├─ Command.java  命令模式
                            ├─ Interpreter.java  解释器模式
                            ├─ IteratorPattern.java  迭代器模式
                            ├─ Mediator.java  中介者模式
                            ├─ Memento.java  备忘录模式
                            ├─ Observer.java  观察者模式
                            ├─ State.java  状态模式
                            ├─ Strategy.java  策略模式
                            ├─ TemplateMethod.java  模板方法模式
                            ├─ Visitor.java  访问者模式
                            └─ behavior.png 11种行为型设计模式之间的关系
                        ├─create 创建型
                            ├─ AbstractFactory.java  抽象工厂模式
                            ├─ Builder.java  建造者模式
                            ├─ FactoryMethod.java  工厂方法模式
                            ├─ Prototype.java  原型模式
                            └─ Singleton.java  单例模式
                        ├─structure 结构型
                            ├─ Adapter.java  适配器模式
                            ├─ Bridge.java  桥接模式
                            ├─ Composite.java  组合模式
                            ├─ Decorator.java  装饰模式
                            ├─ Facade.java  外观模式
                            ├─ Flyweight.java  享元模式
                            ├─ Proxy.java  代理模式
                            └─ structure.png 7种结构型设计模式之间的关系
                        └─ designPattern.png 23种设计模式之间的关系
                    └─principle 原则
                        ├─ CompositeReuse.java  合成复用原则
                        ├─ Demeter.java  迪米特法则
                        ├─ DependenceInversion.java  依赖倒置原则
                        ├─ InterfaceSegregation.java  接口隔离原则
                        ├─ LiskovSubstitution.java  里氏代换原则
                        └─ OpenClosed.java  开闭原则








           ttt          hhhhh 
        ttt::t          h:::h 
        t::::t          h:::h 
        t::::t          h:::h 
    tttttt::::ttttt     h::h hhhh           oooooooo        mmmmmm   mmmmmm       aaaaaaaaaaa     xxxxxx      xxxxxx
    t:::::::::::::t     h::hh::::hhh      oo::::::::oo    summarize by thomax     a::::::::::a     x::::x    x::::x
    t:::::::::::::t     h::::::::::hh    o::::::::::::o  good luck have fun!!!    aaaaaaaa::::a     x::::x  x::::x
    ttttt::::::tttt     h:::::hhh::::h   o::::oooo::::o  m:::::::::::::::::::m            a:::a      x::::xx::::x
        t::::t          h::::h  h:::::h  o:::o    o:::o  m:::::::::::::::::::m      aaaaaa::::a       x::::::::x
        t::::t          h:::h    h::::h  o:::o    o:::o  m:::m   m:::m   m:::m    aa::::::::::a        x::::::x
        t::::t          h:::h    h::::h  o:::o    o:::o  m:::m   m:::m   m:::m   a::::aaa:::::a        x::::::x
        t::::t   ttttt  h:::h    h::::h  o:::o    o:::o  m:::m   m:::m   m:::m  a::::a   a::::a       x::::::::x
        t:::::ttt::::t  h:::h    h::::h  o::::oooo::::o  m:::m   m:::m   m:::m  a::::a   a::::a      x::::xx::::x
        tt:::::::::::t  h:::h    h::::h  o::::::::::::o  m:::m   m:::m   m:::m  a:::::aaa:::::a     x::::x  x::::x
          tt::::::::tt  h:::h    h::::h   oo:::::::::oo  m:::m   m:::m   m:::m   a:::::::::aa::a   x::::x    x::::x
            ttttttttt    hhhh    hhhhhh    oooooooooo    mmmmm   mmmmm   mmmmm    aaaaaaaaa  aaa  xxxxxx      xxxxxx
