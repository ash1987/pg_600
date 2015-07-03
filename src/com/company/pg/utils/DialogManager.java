/**
 * Project Name:XPGSdkV4AppBase
 * File Name:DialogManager.java
 * Package Name:com.gizwits.framework.utils
 * Date:2015-1-27 14:47:29
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.company.pg.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * 
 * ClassName: Class DialogManager. <br/>
 * 对话框管理 <br/>

 */
public class DialogManager {

	/**
	 * Show dialog.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param dialog
	 *            the dialog
	 */
	public static void showDialog(Context ctx, Dialog dialog) {
		if (dialog != null && !dialog.isShowing() && ctx != null
				&& !((Activity) ctx).isFinishing()) {
			dialog.show();
		}
	}

	/**
	 * 隐藏dialog，加了context生命判断，避免窗口句柄泄漏.
	 * 
	 * @param ctx
	 *            dialog依赖的activity
	 * @param dialog
	 *            欲隐藏的dialog
	 */
	public static void dismissDialog(Activity ctx, Dialog dialog) {
		if (dialog != null && dialog.isShowing() && ctx != null
				&& !ctx.isFinishing())
			dialog.dismiss();
	}

	/**
	 * wheel view dialog.
	 * 
	 * @see OnTimingChosenEvent
	 */

	public interface On2TimingChosenListener {

		/**
		 * Timing hour chosen.
		 * 
		 * @param HourTime
		 *            the hour time
		 * 
		 * @param HourTime
		 *            the hour time
		 */
		public void timingChosen(int HourTime, int MinTime);

	}
}
