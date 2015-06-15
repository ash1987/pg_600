package com.company.pg;

import java.util.Date;
import android.util.Log;


/**
 * 
 * 描述：日志封装类
 */
public class ILog {
	
	public final static String TAG = ILog.class.getName();
	private static boolean debug = true;
	
	public static void LogD(Class classz, String str) {
		if (debug) {
			Log.d(TAG, classz.getCanonicalName() + "--->" + str);
		}
	}
	
	public static void LogI(Class classz, String str) {
		if (debug) {
			Log.i(TAG, classz.getCanonicalName() + "--->" + str);
		}
	}
	
	public static void LogE(Class classz, String str) {
		if (debug) {
			Log.e(TAG, classz.getCanonicalName() + "--->" + str);
		}
	}
	
	public static void LogV(Class classz, String str) {
		if (debug) {
			Log.v(TAG, classz.getCanonicalName() + "--->" + str);
		}
	}
	
	public static void logException(Class c, Throwable e) {
		try {
			if (debug) {
				StringBuilder exceptionInfo = new StringBuilder();
				if (e == null) {
					exceptionInfo.append(new Date().toGMTString() + "\n" + "Exception:" + "e is null,probably null pointer exception" + "\n");
				} else {
					e.printStackTrace();
					exceptionInfo.append(new Date().toGMTString() + "\n");
					exceptionInfo.append(e.getClass().getCanonicalName() + ":" + e.getMessage() + "\n");
					StackTraceElement[] stes = e.getStackTrace();
					for (StackTraceElement ste : stes) {
						exceptionInfo.append("at " + ste.getClassName() + "$" + ste.getMethodName() + "$" + ste.getFileName() + ":" + ste.getLineNumber() + "\n");
					}
				}
				
				LogE(c, exceptionInfo.toString());
			}
		} catch (Exception ex) {
			LogE(c, ex.getMessage());
		}
	}
}
