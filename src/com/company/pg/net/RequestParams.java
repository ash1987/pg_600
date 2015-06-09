package com.company.pg.net;

import java.util.ArrayList;

import android.text.TextUtils;

/**
 * 描述：存储http请求键值对
 */

public class RequestParams {
	
	private ArrayList<String> mKeys = new ArrayList<String>();
	private ArrayList<String> mValues = new ArrayList<String>();
	
	public RequestParams() {
		
	}
	
	public ArrayList<String> getmKeys() {
		return mKeys;
	}
	
	public ArrayList<String> getmValues() {
		return mValues;
	}
	
	public void add(String key, String value) {
		if (!TextUtils.isEmpty(key)) {
			this.mKeys.add(key);
			mValues.add(value);
		}
	}
	
	public void add(String key, int value) {
		this.mKeys.add(key);
		this.mValues.add(String.valueOf(value));
	}
	
	public void add(String key, long value) {
		this.mKeys.add(key);
		this.mValues.add(String.valueOf(value));
	}
	
	public void remove(String key) {
		int firstIndex = mKeys.indexOf(key);
		if (firstIndex >= 0) {
			this.mKeys.remove(firstIndex);
			this.mValues.remove(firstIndex);
		}
		
	}
	
	public void remove(int i) {
		if (i < mKeys.size()) {
			mKeys.remove(i);
			this.mValues.remove(i);
		}
	}
	
	private int getLocation(String key) {
		if (this.mKeys.contains(key)) {
			return this.mKeys.indexOf(key);
		}
		return -1;
	}
	
	public String getKey(int location) {
		if (location >= 0 && location < this.mKeys.size()) {
			return this.mKeys.get(location);
		}
		return "";
	}
	
	public String getValue(String key) {
		int index = getLocation(key);
		if (index >= 0 && index < this.mKeys.size()) {
			return this.mValues.get(index);
		} else {
			return null;
		}
	}
	
	public String getValue(int location) {
		if (location >= 0 && location < this.mKeys.size()) {
			String rlt = this.mValues.get(location);
			return rlt;
		} else {
			return null;
		}
	}
	
	public int size() {
		return mKeys.size();
	}
	
	public void addAll(RequestParams parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			this.add(parameters.getKey(i), parameters.getValue(i));
		}
	}
	
	public void clear() {
		this.mKeys.clear();
		this.mValues.clear();
	}
}
