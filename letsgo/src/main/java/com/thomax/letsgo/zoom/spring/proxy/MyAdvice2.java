package com.thomax.letsgo.zoom.spring.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

//定义切面
@Aspect
@Component
public class MyAdvice2 {

	/**执行顺序：
	 *	前置通知(Before)：是调用方法之前，调用
	 *	环绕通知(Around):在调用方法的前后，都会执行
	 *	execute---
	 *	后置通知(After):无论是否出现异常都会调用
	 *	环绕通知(Around):在调用方法的前后，都会执行
	 *	异常通知(After-Throwing):在方法调用出现异常时，执行
	 *	后置通知(AfterReturning)：在调用方法之后，调用（出现异常不调用）
	 *	
	 */
	
	//下面利用一个空方法，将切入点抽取出来，后面调用时，直接使用Myadvice2.pc()即可调用切入点表达式
	@Pointcut("execution(* spring.dao.impl.*Dao2.*(..))")
	public void pc() {}
	
	@Before("MyAdvice2.pc()")
	public void beforeX() {
		System.out.println("前置通知(Before)");
	}
	
	@Around("MyAdvice2.pc()")
	public Object aroundX(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("环绕通知-前(Around)");
		Object proceed = pjp.proceed();
		System.out.println("环绕通知-后(Around)");
		return proceed;
	}
	
	@After("MyAdvice2.pc()")
	public void afterX() {
		System.out.println("后置通知(After)，无论是否出现异常都会调用");
	}
	
	@AfterThrowing("MyAdvice2.pc()")
	public void afterThrowingX() {
		System.out.println("异常通知(After-Throwing)");
	}
	
	@AfterReturning("MyAdvice2.pc()")
	public void afterReturningX() {
		System.out.println("后置通知(AfterReturning)，出现异常不调用");
	}
	
}
