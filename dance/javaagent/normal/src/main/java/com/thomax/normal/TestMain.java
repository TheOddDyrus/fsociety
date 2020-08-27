package com.thomax.normal;


public class TestMain {

    /**
     * 使用此案例前需要加启动命令：-javaagent:C:\Users\Administrator\Desktop\agent.jar
     *
     * @param args
     */
    public static void main(String[] args)  {
        TestClass testClass = new TestClass();
        testClass.testMethod();
    }

}
