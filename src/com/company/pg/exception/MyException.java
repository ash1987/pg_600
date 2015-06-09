package com.company.pg.exception;

import org.apache.http.HttpResponse;

/**
 * 
 * 描述：异常类
 */
public class MyException extends Exception {
	
	private static final String TAG = MyException.class.getSimpleName();
	protected String errorString;
	protected int exceptionCode = -1;
	private HttpResponse response;
	
	public MyException(int paramInt) {
		this.exceptionCode = paramInt;
	}
	
	public MyException(int paramInt, String paramString) {
		super(paramString);
		this.exceptionCode = paramInt;
		this.errorString = paramString;
	}
	
	public MyException(int paramInt, Throwable paramThrowable) {
		super(paramThrowable);
		this.exceptionCode = paramInt;
	}
	
	public MyException(HttpResponse paramHttpResponse, String paramString) {
		this.response = paramHttpResponse;
		this.errorString = paramString;
	}
	
	public String getErrorString() {
		return this.errorString;
	}
	
	public int getExceptionCode() {
		return this.exceptionCode;
	}
	
	public HttpResponse getHttpResponse() {
		return this.response;
	}
	
	public void setExceptionCode(int paramInt) {
		this.exceptionCode = paramInt;
	}
	
	public String toString() {
		return super.toString() + " ExceptionCode:" + this.exceptionCode + " ErrorMessage:" + this.errorString;
	}
}
