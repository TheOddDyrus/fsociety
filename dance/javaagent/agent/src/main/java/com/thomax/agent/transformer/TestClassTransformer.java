package com.thomax.agent.transformer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TestClassTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");
        if ("com.thomax.normal.TestClass".equals(className)) {
            try {
                CtClass ctClass = ClassPool.getDefault().get(className);
                CtMethod ctmethod = ctClass.getDeclaredMethod("testMethod");
                ctmethod.setBody("{System.out.println(\"已经修改方法内容成功！！\");}");

                ctClass.detach();

                //outputClass(ctClass);

                return ctClass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void outputClass(CtClass ctClass) {
        try {
            FileOutputStream output = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\TestAgent.class");
            output.write(ctClass.toBytecode());
            output.close();
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

}
