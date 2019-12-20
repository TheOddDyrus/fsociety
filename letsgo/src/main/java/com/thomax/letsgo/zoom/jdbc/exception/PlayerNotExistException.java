package com.thomax.letsgo.zoom.jdbc.exception;

public class PlayerNotExistException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 球员不存在异常
	 * @param msg 错误信息
	 */
	public PlayerNotExistException(String msg) {
		super(msg);
	}

}
