package com.thomax.agent.transformer;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class TestClassTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        className = className.replaceAll("/", ".");

        if (className.equals("com.thomax.normal.TestAgent")) {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(loader)); // 将要修改的类的classpath加入到ClassPool中，否则找不到该类
            try {
                CtClass ctClass = classPool.get(className);
                for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
                    if (ctMethod.getName().equals("test")) {
                        // 修改字节码
                        ctMethod.insertAfter("System.out.println(\"12321-thomax\");");
                    }
                }
                ctClass.detach();
                return ctClass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return classfileBuffer;
    }

}
