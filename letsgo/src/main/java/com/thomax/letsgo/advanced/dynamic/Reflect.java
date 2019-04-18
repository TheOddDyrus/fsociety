package com.thomax.letsgo.advanced.dynamic;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射
 */
public class Reflect {

    public void reflectExample() throws Exception {
        //得到Class类的对象的三种方法：
        Class<?> clazz1 = Class.forName("letsgo.Frank");
        Class<?> clazz2 = (new Frank()).getClass();
        Class<?> clazz3 = Frank.class;

        //通过Frank类(clazz1)执行这个类的方法add(String str, int ... args)
        Object obj = clazz1.newInstance(); //一定要有无参构造方法
        Method method = clazz1.getMethod("add", String.class, int.class, int.class);
        Object returnValue = method.invoke(obj, "hello", 2, 3);

        //获得构造方法public Frank(String str, int ... args)
        Constructor<?> con1 = clazz1.getConstructor(String.class, int.class, int.class);
        //获得构造方法public Frank()
        Constructor<?> con2 = clazz1.getConstructor();
        Object obj2 = con2.newInstance("hello", 4, 5); //通过构造方法创建对象
        Object obj3 = con2.newInstance(); //通过构造方法创建对象

        //获取私有的方法add(String str)
        Method add = clazz1.getDeclaredMethod("add", String.class);
        //访问私有方法前要更改私有方法为可以访问
        add.setAccessible(true);
        add.invoke(obj2);

        //获取所有字段(包括私有字段)
        Field[] declaredFields = clazz1.getDeclaredFields();
        for (Field field : declaredFields) {
            int declare = field.getModifiers(); //以整数形式返回由此Field对象表示的字段的Java语言修饰符
            String name = field.getName();
        }

        //获取注解对象(只能获取自定义注解对象)
        Annotation[] annotation = clazz1.getAnnotations();
        for (Annotation ann : annotation) {
            System.out.println(ann);
        }
    }

    private class Frank {
        public String hi = "hi";
        private String hello = "hello";
        public Frank() {}
        public Frank(String str, int ... args) {}
        public void add(String str, int ... args) {}
        private void add(String str) {}
    }
}
