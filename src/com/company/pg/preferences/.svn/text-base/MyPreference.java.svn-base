package com.company.pg.preferences;

import android.content.ContentValues;
import android.content.Context;

/**
 * 系统信息 保存
 */
public class MyPreference extends BasePreferences {

	// 文件名
	private final static String PERFERENCE_NAME = "pg_perferences";
	// 设备是否已经绑定
	private final static String IS_DEVICE_BIND = "idDeviceBind";

	public MyPreference(Context context) {
		super(context, PERFERENCE_NAME);
	}

	public void setDeviceBind(Context context, boolean idDeviceBind) {
		if (null == preferenses) {
			throw new NullPointerException("SharedPreferences is null!");
		}

		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		ContentValues values = new ContentValues();
		values.put(IS_DEVICE_BIND, idDeviceBind);
		try {
			write(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isDeviceBind() {
		return getBoolean(IS_DEVICE_BIND, false);
	}
}