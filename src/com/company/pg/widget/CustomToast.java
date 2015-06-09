package com.company.pg.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class CustomToast {

	private static Toast toast = null;

	/**
	 * 
	 * @param context
	 *            使用时的上下文
	 * 
	 * @param hint
	 *            在提示框中需要显示的文本
	 * 
	 * @return 返回一个不会重复显示的toast
	 * 
	 * */

	public static Toast getToast(Context context, String hint, int duration) {
		if (/* ToastFactory.context == context */toast != null) {
			// toast.cancel();
			toast.setText(hint);
		} else {
			// ToastFactory.context = context;
			toast = Toast.makeText(context, hint, duration);
		}
		return toast;
	}
	
	public static void showToast(Context context, String hint, int duration) {
		getToast(context, hint, Toast.LENGTH_SHORT).show();
		if (handler != null && handler.hasMessages(0)) {
			handler.removeMessages(0);
		}
		handler.sendEmptyMessageDelayed(0, duration);
	}
	
	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (toast != null) {
					toast.cancel();
				}
				break;
			case 1:
				
				break;
			default:
				break;
			}
		}
		
	};
}
