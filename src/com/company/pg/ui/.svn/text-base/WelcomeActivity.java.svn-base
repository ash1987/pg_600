package com.company.pg.ui;

import com.company.pg.R;
import com.company.pg.base.BaseActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * 描述：欢迎界面
 */

public class WelcomeActivity extends BaseActivity {
	public final static String LOG_TAG = "pg_600";
	public final static int STATE = 0x03;
	private final static long stateTime = 2000l;// 欢迎页停留时间

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler != null && mHandler.hasMessages(STATE)) {
			mHandler.removeMessages(STATE);
		}
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {
		setContentView(R.layout.welcome);
	}

	@Override
	protected void setAttribute() {
		mHandler.sendEmptyMessageDelayed(STATE, stateTime);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case STATE :
					if (setmanager.isAutoLogin() && !TextUtils.isEmpty(setmanager.getUid())&& !TextUtils.isEmpty(setmanager.getToken())) {
						Intent intent = new Intent(WelcomeActivity.this,
								MainActivity.class);
						intent.putExtra("from", WelcomeActivity.class.getName());
						startActivity(intent);
						WelcomeActivity.this.finish();
					} else {
						Intent loginIntent = new Intent(WelcomeActivity.this,
								LoginActivity.class);
						loginIntent.putExtra("from", WelcomeActivity.class.getName());
						startActivity(loginIntent);
						WelcomeActivity.this.finish();
					}
					break;
				
				default :
					break;
			}
		}
	};
}
