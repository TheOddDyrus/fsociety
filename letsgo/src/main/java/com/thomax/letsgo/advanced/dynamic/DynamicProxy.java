package com.thomax.letsgo.advanced.dynamic;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理是通过接口中的方法名，在动态生成的代理类中调用业务实现类的同名方法，需要用接口声明来创建代理实例；
 * CGlib动态代理是通过继承业务类，生成的动态代理类相对来说是业务类的子类，通过重写业务方法进行代理，接口/实体类型声明都可以实现动态代理
 */
public class DynamicProxy {
    public static void main(String[] args) {
        try {
            Hello hello = (Hello) new JDKProxyObject().bind(new HelloImpl());
            hello.sayHello();
        } catch (Exception e) {
            System.out.println("JDK动态代理用接口来声明代理对象时异常：" + e.getMessage());
        }
        try {
            HelloImpl hello2 = (HelloImpl) new JDKProxyObject().bind(new HelloImpl());
            hello2.sayHello();
        } catch (Exception e) {
            System.out.println("JDK动态代理用实现类来声明代理对象时异常：" + e.getMessage());
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
        try {
            Hello hello4 = (Hello) new CGLibProxyObject().getInstance(new HelloImpl());
            hello4.sayHello();
        } catch (Exception e) {
            System.out.println("CGLIB动态代理用接口来声明代理对象时代理异常：" + e.getMessage());
        }
        try {
            HelloImpl hello3 = (HelloImpl) new CGLibProxyObject().getInstance(new HelloImpl());
            hello3.sayHello();
        } catch (Exception e) {
            System.out.println("CGLIB动态代理用实现类来声明代理对象时异常：" + e.getMessage());
        }
    }
}

/**
 * JDK动态代理
 */
class JDKProxyObject implements InvocationHandler {
    private Object target;
    public Object bind(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this); //返回代理后的对象
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("jdk proxy!!"); //代理操作
        return method.invoke(target, args); //执行被代理的对象实际需要被执行的方法
    }
}

/**
 * CGLIB动态代理
 */
class CGLibProxyObject implements MethodInterceptor {
    public Object getInstance(Object target) {
        Enhancer enhancer = new Enhancer(); //创建加强器，用来创建动态代理类
        enhancer.setSuperclass(target.getClass());  //为加强器指定要代理的业务类（即：为下面生成的代理类指定父类）
        enhancer.setCallback(this); //设置回调：对于代理类上所有方法的调用，都会调用CallBack触发拦截，需要在intercept()方法中实现代理细节
        return enhancer.create();
    }
    @Override
    public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib proxy!!"); //代理操作
        return methodProxy.invokeSuper(obj, objects); //调用业务类（父类中）的方法
    }
}

interface Hello { void sayHello(); }

class HelloImpl implements Hello {
    @Override
    public void sayHello() {
        System.out.println("say hello...");
    }
}
