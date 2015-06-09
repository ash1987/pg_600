package com.company.pg.preferences;

import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference基类
 */
public class BasePreferences {
	protected SharedPreferences preferenses;

	public boolean getBoolean(String key, boolean defaultValue) {
		return null != preferenses ? preferenses.getBoolean(key, defaultValue)
				: defaultValue;
	}

	public String getString(String key) {
		return null != preferenses ? preferenses.getString(key, "") : "";
	}

	public int getInt(String key) {
		return null != preferenses ? preferenses.getInt(key, 0) : 0;
	}

	public long getLong(String key) {
		return null != preferenses ? preferenses.getLong(key, 0) : 0;
	}

	public BasePreferences(Context context, String table) {
		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		preferenses = context.getSharedPreferences(table, Context.MODE_PRIVATE);
	}

	public boolean write(ContentValues values) {
		if (null == preferenses) {
			return false;
		}

		SharedPreferences.Editor editor = preferenses.edit();
		if (null == editor) {
			return false;
		}

		Set<Map.Entry<String, Object>> entries = values.valueSet();
		for (Map.Entry<String, Object> entry : entries) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				editor.putString(key, String.valueOf(value));
			} else if (value instanceof Integer || value instanceof Short
					|| value instanceof Byte) {
				editor.putInt(key, (Integer) value);
			} else if (value instanceof Long) {
				editor.putLong(key, (Long) value);
			} else if (value instanceof Float || value instanceof Double) {
				editor.putFloat(key, (Float) value);
			} else if (value instanceof Boolean) {
				editor.putBoolean(key, (Boolean) value);
			}
		}

		return editor.commit();
	}

	public ContentValues read() {
		if (null == preferenses) {
			return null;
		}

		Map map = preferenses.getAll();
		if (null == map) {
			return null;
		}

		ContentValues values = new ContentValues();
		Set<Map.Entry<String, ?>> entries = map.entrySet();
		for (Map.Entry<String, ?> entry : entries) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				values.put(key, String.valueOf(value));
			} else if (value instanceof Integer || value instanceof Short
					|| value instanceof Byte) {
				values.put(key, (Integer) value);
			} else if (value instanceof Long) {
				values.put(key, (Long) value);
			} else if (value instanceof Float || value instanceof Double) {
				values.put(key, (Float) value);
			} else if (value instanceof Boolean) {
				values.put(key, (Boolean) value);
			}
		}

		return values;
	}

	public void saveString(String Key, String value) {
		SharedPreferences.Editor editor = preferenses.edit();
		if (null != editor) {
			editor.putString(Key, value).commit();
		}
	}

	public void saveBoolean(String Key, boolean value) {
		SharedPreferences.Editor editor = preferenses.edit();
		if (null != editor) {
			editor.putBoolean(Key, value).commit();
		}
	}
}
