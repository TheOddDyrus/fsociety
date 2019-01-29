package com.thomax.letsgo.jdbc.exception;

public class SalaryException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 球员账户低于1€发生此异常
	 * @param msg 错误信息
	 */
	public SalaryException(String msg) {
		super(msg);
	}

}
