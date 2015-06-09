package com.company.pg.net;

/**
 * 描述：网络异常类
 */

public class MyHttpException extends Exception {
	/**
	 * serialVersionUID : <功能描述)>
	*/
	private static final long serialVersionUID = -1591059863880316560L;

	// 网络不可用异常
	public static final int NETWORK_OFF_EXCEPTION = 0x01;

	// 服务器错误异常
	public static final int HOST_ERROR_EXCEPTION = 0x02;

	// 连接超时异常
	public static final int CONNECT_TIMEOUT_EXCEPTION = 0x03;

	// Socket超时异常
	public static final int SOCKET_TIMEOUT_EXCEPTION = 0x04;

	private int statusCode = -1;

	public MyHttpException(String msg) {
		super(msg);
	}

	public MyHttpException(Exception cause) {
		super(cause);
	}

	public MyHttpException(String msg, int statusCode) {
		super(msg);
		this.statusCode = statusCode;
	}

	public MyHttpException(String msg, Exception cause) {
		super(msg, cause);
	}

	public MyHttpException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public MyHttpException() {
		super();
	}

	public MyHttpException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public MyHttpException(Throwable throwable) {
		super(throwable);
	}

	public MyHttpException(int statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
