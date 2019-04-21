package com.thomax.letsgo.advanced.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理：使用Proxy类
 */
public class JDKProxy {

    interface Hello { void sayHello(); }

    static class HelloImpl implements Hello {
        @Override
        public void sayHello() {
            System.out.println("say hello...");
        }
    }

    static class DynamicProxy implements InvocationHandler {
        Object originalObj;
        private Object bind(Object obj) {
            this.originalObj = obj;
            return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this); //返回代理后的对象
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("proxy this"); //代理操作
            return method.invoke(originalObj, args); //执行被代理的对象实际需要被执行的方法
        }
    }

    public static void main(String[] args) {
        Hello hello = (Hello) new DynamicProxy().bind(new HelloImpl()); //绑定需要被代理的对象
        hello.sayHello();
    }

}
