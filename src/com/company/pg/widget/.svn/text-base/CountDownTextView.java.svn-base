package com.company.pg.widget;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 倒计时控件
 */
public class CountDownTextView extends TextView {

	private long currentSecond = 0l;// 当前剩余时间
	private ScheduledExecutorService executorService;

	public CountDownTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CountDownTextView(Context context) {
		super(context);
	}

	// 设置时间并开始倒计时
	public void startCountDown(long currentSecond) {
		this.currentSecond = currentSecond;
		if (currentSecond <= 0) {
			return;
		}
		setTime(currentSecond);
		// 停止倒计时
		if (executorService != null && !executorService.isShutdown()) {
			executorService.shutdown();
			executorService = null;
		}

		executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleWithFixedDelay(new CountDownTask(), 1, 1,
				TimeUnit.SECONDS);
	}

	// 定时切换图片的Task
	private class CountDownTask implements Runnable {

		@Override
		public void run() {
			mHandler.sendEmptyMessage(0);
		}

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (currentSecond <= 0) {
				// 停止倒计时
				if (executorService != null && !executorService.isShutdown()) {
					executorService.shutdown();
				}
				CountDownTextView.this.setText("已结束");
			} else {
				setTime(currentSecond);
				currentSecond--;
			}
		}

	};

	// 设置初始时间
	private void setTime(long ms) {
		StringBuffer sb = new StringBuffer();
		long date = ms / 3600l / 24l;
		long h = ms / 3600l % 24l;
		long m = ms / 60l % 60l;
		long s = ms % 60l % 60l;

		// 设置天
		if (date < 10) {
			sb.append("0" + date + "天");
		} else {
			sb.append("" + date + "天");
		}

		// 设置小时
		if (h < 10) {
			sb.append("0" + h + "时");
		} else {
			sb.append("" + h + "时");
		}

		// 设置分钟
		if (m < 10) {
			sb.append("0" + m + "分");
		} else {
			sb.append("" + m + "分");
		}

		// 设置秒
		if (s < 10) {
			sb.append("0" + s + "秒");
		} else {
			sb.append("" + s + "秒");
		}
		
		this.setText("剩余时间：" + sb.toString());
	}
}
