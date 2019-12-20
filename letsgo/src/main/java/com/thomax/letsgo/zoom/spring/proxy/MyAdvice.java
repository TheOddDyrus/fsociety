package com.thomax.letsgo.zoom.spring.proxy;

import org.aspectj.lang.ProceedingJoinPoint;

public class MyAdvice {

	/**执行顺序：
	 *	前置通知(Before)：是调用方法之前，调用
	 *	环绕通知(Around):在调用方法的前后，都会执行
	 *	execute---
	 *	后置通知(After):无论是否出现异常都会调用
	 *	环绕通知(Around):在调用方法的前后，都会执行
	 *	异常通知(After-Throwing):在方法调用出现异常时，执行
	 *	后置通知(AfterReturning)：在调用方法之后，调用（出现异常不调用）
	 */

	public void beforeX() {
		System.out.println("前置通知(Before)");
	}
	
	public Object aroundX(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("环绕通知-前(Around)");
		Object proceed = pjp.proceed();
		System.out.println("环绕通知-后(Around)");
		return proceed;
	}
	
	public void afterX() {
		System.out.println("后置通知(After)，无论是否出现异常都会调用");
	}
	
	public void afterThrowingX() {
		System.out.println("异常通知(After-Throwing)");
	}
	
	public void afterReturningX() {
		System.out.println("后置通知(AfterReturning)，出现异常不调用");
	}
	
}
