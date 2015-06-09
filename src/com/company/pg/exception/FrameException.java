package com.company.pg.exception;

/**
 * 
 * 描述：公共异常类
 */
public class FrameException extends Exception {
	//网络不可用异常
	public static final int NETWORK_OFF_EXCEPTION = 0x01;
	
	//服务器错误异常
	public static final int HOST_ERROR_EXCEPTION = 0x01;
	
	//连接超时异常
	public static final int CONNECT_TIMEOUT_EXCEPTION = 0x02;
	
	//Socket超时异常
	public static final int SOCKET_TIMEOUT_EXCEPTION = 0x03;
	
	private int statusCode = -1;

	public FrameException(String msg) {
		super(msg);
	}

	public FrameException(Exception cause) {
		super(cause);
	}

	public FrameException(String msg, int statusCode) {
		super(msg);
		this.statusCode = statusCode;
	}

	public FrameException(String msg, Exception cause) {
		super(msg, cause);
	}

	public FrameException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public FrameException() {
		super();
	}

	public FrameException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public FrameException(Throwable throwable) {
		super(throwable);
	}

	public FrameException(int statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
