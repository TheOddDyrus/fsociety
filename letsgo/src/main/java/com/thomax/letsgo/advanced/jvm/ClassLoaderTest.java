package com.thomax.letsgo.advanced.jvm;

import java.io.IOException;
import java.io.InputStream;

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
    static { System.out.println("super class"); }
    public static int value = 123;
    public static final String hello = "hello";
}
class SubClass extends SuperClass {
    static { System.out.println("sub class"); }
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
 * 类加载器
 */
class CustomClassLoader {
    /*自定义类加载器*/
    private ClassLoader classLoader = new ClassLoader() {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
            InputStream is = getClass().getResourceAsStream(fileName);
            if (is == null) {
                return super.loadClass(name);
            }
            int available;
            try {
                available = is.available(); //获取读的文件所有的字节个数
                byte[] b = new byte[available];
                is.read(b);
                return defineClass(name, b, 0, b.length);
            } catch (IOException e) {
                throw new ClassNotFoundException(name);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

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