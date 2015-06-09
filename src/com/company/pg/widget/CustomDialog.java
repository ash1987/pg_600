package com.company.pg.widget;

import com.company.pg.R;
import com.company.pg.utils.AndroidUtils;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDialog implements OnClickListener {
	//对话框的类型
	public static final int TYPE_APP_FORCE_UPDATE = 1;
	
	// 声明Dialog对象
	private Dialog dialog;
	// 上下文mContext
	private Context mContext;

	// dialog的标题
	private TextView title;
	// dialog的内容
	private TextView content;

	// 确定按钮
	private Button btn_ok;
	// 取消按钮
	private Button btn_cancel;

	private LinearLayout custom_dialog_ll;

	private TextView custom_dialog_btn_line;// 按钮分割线

	// 屏幕的宽度
	private int screenWidth = 0;

	private DialogInterface.OnClickListener ok_Listener;
	private DialogInterface.OnClickListener cancel_Listener;
	
	/**
	 * @param context
	 *            上下文对象
	 * @param layoutResID
	 *            布局文件
	 * @param btnCount
	 *            按钮个数
	 */
	public CustomDialog(Context context, int layoutResID, int btnCount) {
		this.mContext = context;
		dialog = new Dialog(context, R.style.CustomDialog);
		dialog.setContentView(layoutResID);
		initView(btnCount);
	}

	public CustomDialog(Context context, int layoutResID, int btnCount,
			int screenWidth) {
		this.mContext = context;
		dialog = new Dialog(context, R.style.CustomDialog);
		dialog.setContentView(layoutResID);
		this.screenWidth = screenWidth;
		initView(btnCount);
	}

	// 初始化控件
	private void initView(int btnCount) {
		title = (TextView) dialog.findViewById(R.id.custom_dialog_title);
		content = (TextView) dialog.findViewById(R.id.custom_dialog_content);
		btn_ok = (Button) dialog.findViewById(R.id.custom_dialog_btnOk);
		btn_ok.setOnClickListener(this);
		btn_cancel = (Button) dialog.findViewById(R.id.custom_dialog_btnCancel);
		btn_cancel.setOnClickListener(this);
		custom_dialog_btn_line = (TextView) dialog
				.findViewById(R.id.custom_dialog_btn_line);
		if (btnCount == 1) {
			btn_cancel.setVisibility(View.GONE);
			custom_dialog_btn_line.setVisibility(View.GONE);
		} 

		if (screenWidth != 0) {
			custom_dialog_ll = (LinearLayout) dialog
					.findViewById(R.id.custom_dialog_ll);
			LayoutParams lp = custom_dialog_ll.getLayoutParams();
			lp.width = screenWidth - (AndroidUtils.dip2px(mContext, 60));
		}

	}
	
	public void setAttribute(int dialogType) {
		content.setGravity(Gravity.LEFT);
		if(dialogType == TYPE_APP_FORCE_UPDATE) {
			this.setCancelable(false);
		}
	}
	
	public void setDialogGravity(int gravity) {
		content.setGravity(gravity);
	}

	/**
	 * @param title
	 *            Dialog的标题
	 * @param content
	 *            Dialog的内容
	 */
	public void setTitleContent(String title, String content) {
		this.title.setText(title);
		this.content.setText(content);
	}

	// 确定按钮设置
	public void setPositiveButton(String buttonOk,
			DialogInterface.OnClickListener onClickListener_ok) {
		this.btn_ok.setText(buttonOk);
		this.ok_Listener = onClickListener_ok;
	}

	// 取消按钮设置
	public void setNagetiveButton(String buttonCancel,
			DialogInterface.OnClickListener onClickListener_ok) {
		this.btn_cancel.setText(buttonCancel);
		this.cancel_Listener = onClickListener_ok;
	}

	/**
	 * @return dialog
	 */
	public Dialog getDialog() {
		return dialog;
	}

	/**
	 * 显示dialog
	 */
	public void show() {
		dialog.show();
	}

	/**
	 * 隐藏dialog
	 */
	public void dismiss() {
		dialog.dismiss();
		dialog.cancel();
	}

	public boolean isShowing() {
		return dialog.isShowing();

	}

	public void setCancelable(boolean flag) {
		dialog.setCancelable(flag);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_dialog_btnOk:// 确定按钮点击
			ok_Listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
			break;
		case R.id.custom_dialog_btnCancel:// 取消按钮点击
			cancel_Listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
			break;
		default:
			break;
		}
	}
}
