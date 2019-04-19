package com.thomax.letsgo.advanced.dynamic;

import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;

/**
 * ASM是一个强大的框架，利用它我们可以做到：
 *   1、获得class文件的详细信息，包括类名、父类名、接口、成员名、方法名、方法参数名、局部变量名、元数据等
 *   2、对class文件进行动态修改，如增加、删除、修改类方法、在某个方法中添加指令等
 *   3、CGLIB（动态代理）是对ASM的封装，简化了ASM的操作，降低了ASM的使用门槛
 */
public class ASM {

    @Deprecated
    public static void main(String args[]) throws Exception {
        ClassWriter classWriter = new ClassWriter(0);
        String className = "com/thomax/asm/HelloWorld";
        classWriter.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, className, null,
                "java/lang/Object", null);


        MethodVisitor initVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>",
                "()V", null, null);
        initVisitor.visitCode();
        initVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        initVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>",
                "V()");
        initVisitor.visitInsn(Opcodes.RETURN);
        initVisitor.visitMaxs(1, 1);
        initVisitor.visitEnd();

        MethodVisitor helloVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "sayHello",
                "()V;", null, null);
        helloVisitor.visitCode();
        helloVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
                "Ljava/io/PrintStream;");
        helloVisitor.visitLdcInsn("hello world!");
        helloVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
                "println", "(Ljava/lang/String;)V");
        helloVisitor.visitInsn(Opcodes.RETURN);
        helloVisitor.visitMaxs(1, 1);
        helloVisitor.visitEnd();

        classWriter.visitEnd();
        byte[] code = classWriter.toByteArray();
        File file = new File("D:\\HelloWorld.class");
        FileOutputStream output = new FileOutputStream(file);
        output.write(code);
        output.close();
    }

}
