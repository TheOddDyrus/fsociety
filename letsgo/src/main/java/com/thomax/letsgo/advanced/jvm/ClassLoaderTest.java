package com.thomax.letsgo.advanced.jvm;

import java.io.IOException;
import java.io.InputStream;

/**
 * 类加载过程：加载、验证、准备、解析、初始化
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        CustomClassLoader example = new CustomClassLoader();
        example.test();
    }
}

/**
 * 类的初始化
 * （CLass对象存放在方法区里面，这个对象将作为程序访问方法区中的这些类型数据的外部接口）
 */
class SuperClass {
    static {
        System.out.println("super class");
    }
    public static int value = 123;
    public static final String hello = "hello";
}
class SubClass extends SuperClass {
    static {
        System.out.println("sub class");
    }
}
class NotInit {
    public void test1() {
        System.out.println(SubClass.value); //输出结果：super class/n 123 （对于静态字段，只有直接定义这个字段的类才会被初始化，单例模式中使用内部类来创建对象也是这个道理）
    }
    public void test2() {
        SuperClass[] sc = new SuperClass[10]; //输出结果：无 （通过new创建对象是会触发类的初始化的，而java中的数组创建不会触发类的初始化，数组不会经过类加载器而是直接由JVM直接创建）
    }
    public void test3() {
        System.out.println(SubClass.hello); //输出结果：hello （声明为final在编译阶段就会经过常量传播优化，调用时也不会触发类的初始化）
    }
}

/**
 * 类加载器：
 * 类加载器之间的关系为双亲委派模型，如下面注释。除了顶层的启动类加载器，其它都有自己的父类加载器（一般不会以继承，而是由组合来复用父加载器的代码）。
 * 当一个类加载器收到了类加载请求，它会先委托父类加载器去完成，当父类加载器无法完成时才会由子类加载器自己去加载。一个显而易见的例子就是rt.jar中的Object类，
 * 如果这个类可以被各种类加载器所加载的话那java类型体系最基础的行为就无法被保证了（不同类加载器加载出来的相同内容的类在内存中是不同类）。
 */
class CustomClassLoader {

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
    |        Bootstrap ClassLoader          启动类加载器：负责将存放在<JAVA_HOME>\lib目录中，或者被-Xbootclasspath参数所指定路径，并且是虚拟机可以                   |
    |                  ↑                                 识别的（比如:rt.jar）类加载到虚拟机内存。这个类加载器无法被Java程序直接引用。                               |
    |        Extension ClassLoader          扩展类加载器：这个类加载器由sun.misc.Launcher$ExtClassLoader实现，负责将存放在<JAVA_HOME>\lib\ext目录中，               |
    |                  ↑                                 或者被java.ext.dirs系统变量指定的路径中的所有类加载到虚拟机内存。Java程序可以直接使用这个类加载器。          |
    |       Application ClassLoader         应用程序类加载器：这个类加载器由sun.misc.Launcher$AppClassLoader实现，它也是getSystemClassLoader()的返回值所以也叫作     |
    |           ↑            ↑                                系统类加载器，负责将用户类路径(classpath)上的类库加载到虚拟机内存。一般没有自定义过这个是默认的类加载器  |
    |User1 ClassLoader   User2 ClassLoader  自定义类加载器：将加载类的逻辑放在java.lang.ClassLoader.findClass()的自定义类加载器                                     |
    ============================================================================================================================================================*/

    /**
     * 双亲委派第一次被破坏：
     * 由于双亲委派模型在JDK1.2之后才被引入，在此之前重写loadClass()方法即可破坏委派模式，但是这个类加载器通过defineClass()方法无法加载像java.lang中的类，
     * 加载的时候会接收到JVM抛出的异常。JDK1.2以后java.lang.ClassLoader新增了一个findClass()方法让用户去重写而不会破坏委派模式。
     */
    public ClassLoader classLoader = new ClassLoader() {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
            InputStream is = getClass().getResourceAsStream(fileName);
            if (is == null) {
                return super.loadClass(name); //此处会加载java中所有类的父类java.lang.Object
            }
            try {
                int available = is.available(); //获取读的文件字节数
                byte[] b = new byte[available];
                int readLength = is.read(b); //获取读到byte[]中的字节数
                return defineClass(name, b, 0, readLength);
            } catch (IOException e) {
                throw new ClassNotFoundException(name);
            }
        }
    };

    /**
     * 双亲委派第二次被破坏：
     * 双亲委派模式很好地解决了基础类的统一问题，但是基础类如果又要调回用户代码，比如：JNDI、JDBC、JCE、JAXB、JBI (SPI接口提供者的代码)。
     * 为了解决这个问题可以使用线程上下文类加载器(Thread Content ClassLoader)，这个类加载器属于Thread类的属性，如果创建线程时未调用setContextClassLoader()方法则默认应用程序类加载器
     */
    public void setContextClassLoader() {
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    /**
     * 双亲委派第三次被破坏：
     * 是由于用户对程序动态性的追求导致的，比如：HotSwap代码热替换、Hot Deployment模块热部署等。
     * 目前OSGI已经成为业界的Java模块化标准，它的关键是每一个程序模块(OSGI中称为Bundle)都有一个自己的类加载器，当需要热部署时将Bundle连同类加载器一起替换掉。
     * 在OSGI环境下类加载器不再是双亲委派模型中的树状结构，而是更为复杂的网状结构，当收到类加载请求时将按照下面的顺序执行：
     *  1.将以java.*开头的类委派给父类加载器
     *  2.否则，将委派列表名单内的类委派给父类加载器
     *  3.否则，将Import列表中的类委派给Export这个类的Bundle的类加载器
     *  4.否则，查找当前Bundle的ClassPath，使用自己的类加载器
     *  5.否则，查找类是否在自己的Fragment Bundle中，如果在，则委派给Fragment Bundle的类加载器
     *  6.否则，查找Dynamic Import列表的Bundle，委派给对应Bundle的类加载器
     *  7.否则，类查找失败
     *     => 上面顺序中只有1.和2.仍然符合双亲委派规则，其余的都是在平级的类加载器中进行的
     */


    public void test() {
        Object obj = null;
        try {
            obj = classLoader.loadClass("com.thomax.letsgo.advanced.jvm.SubClass");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(obj); //输出结果：class com.thomax.letsgo.advanced.jvm.SubClass
        System.out.println(obj instanceof com.thomax.letsgo.advanced.jvm.SubClass); //输出结果：false （不同的类加载器无法使用instanceof判断所属关系）
    }
}