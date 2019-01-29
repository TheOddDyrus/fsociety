package com.thomax.letsgo.handling;

public class ExceptionHandling {
	public static void main(String[] args) throws Exception {
//		TestInput example = new TestInput();
//		example.main(args);
	}
}


/**class1-异常处理方法*/
class ExceptionDemo{
    public static void main(String[] args) {
        try {
            throw new Exception("My Exception");
        } catch (Exception e) {
        	System.out.println("Caught Exception");
            System.out.println("getMessage():" + e.getMessage());
            System.out.println("getLocalizedMessage():" + e.getLocalizedMessage());
            System.out.println("toString():" + e);
            System.out.println("printStackTrace():");
            System.out.println();
        	
            System.err.println("Caught Exception");
            System.err.println("getMessage():" + e.getMessage());
            System.err.println("getLocalizedMessage():" + e.getLocalizedMessage());
            System.err.println("toString():" + e);
            System.err.println("printStackTrace():");
            e.printStackTrace();
        }
    }
}


/**class2-多线程异常处理*/
class MyThreadStart {
    public static void main(String[] args){
        MyThread t = new MyThread();
        t.start();
        try{
            Thread.sleep(2000);
        }
        catch (Exception e){
        	System.out.println("Catch:  " + e);
            //e.printStackTrace();
        }
        System.out.println("Exiting main");
    }
}

class MyThread extends Thread {
    public void run(){
        System.out.println("Throwing in...");
        System.out.println();
        throw new RuntimeException();
    }
}
//Throwing in...
//
//Exception in thread "Thread-0" java.lang.RuntimeException
//	at examples.MyThread.run(ExceptionHandling.java:40)
//Exiting main


/**class3-重载方法异常处理*/
class OverloadException {
    static double method(int i) throws Exception {
        return i/0;
    }
    static double method(int i, double j) {
        return i + j;
    }
    
    public static void main(String[] args) {
        try{
        	System.out.println(method(10, 12.34d));
            System.out.println(method(10));
        }
        catch (Exception e){
            System.out.println("Catch:  " + e);
            System.out.println();
            e.printStackTrace();
        }
    }
}
//22.34
//Catch:  java.lang.ArithmeticException: / by zero
//
//java.lang.ArithmeticException: / by zero
//	at examples.OverloadException.method(ExceptionHandling.java:67)
//	at examples.OverloadException.main(ExceptionHandling.java:76)
//	at examples.ExceptionHandling.main(ExceptionHandling.java:6)


/**class4-链式异常*/
class ChainException {
	
    public static void main (String args[]) throws Exception {
        int n=20,result=0;
        try{
            result=n/0;
            System.out.println("结果为"+result);
        }
        catch(ArithmeticException ex){
            System.out.println("自动catch算术异常: "+ex);
            try {
                throw new NumberFormatException();
            }
            catch(NumberFormatException ex1) {
                System.out.println("手动throw链试异常 : "+ex1);
            }
        }
    }
}
//自动catch算术异常: java.lang.ArithmeticException: / by zero
//手动throw链试异常 : java.lang.NumberFormatException


/**class5-自定义异常*/
class CustomException extends Exception {
	
	private static final long serialVersionUID = -8565229005156968774L;

// 自定义的类
//	CustomException() {
//        super();
//    }
	CustomException(String s) {
        super(s);
    }
}

class Input {
    void method() throws CustomException {
//    	throw new CustomException();
        throw new CustomException("Wrong input"); // 抛出自定义的类
    }
}

class TestInput {
    public static void main(String[] args){
        try {
            new Input().method();
        }
        catch(CustomException ce) {
            System.out.println(ce.getMessage());
            System.out.println();
            ce.printStackTrace();
        }
    } 
}
//Wrong input
//
//examples.CustomException: Wrong input
//	at examples.Input.method(ExceptionHandling.java:126)
//	at examples.TestInput.main(ExceptionHandling.java:132)
//	at examples.ExceptionHandling.main(ExceptionHandling.java:6)






























