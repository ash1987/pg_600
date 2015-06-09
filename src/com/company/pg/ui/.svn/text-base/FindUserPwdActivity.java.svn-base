package com.company.pg.ui;

import java.util.Timer;
import java.util.TimerTask;
import com.company.pg.R;
import com.company.pg.base.BaseActivity;
import com.company.pg.utils.AndroidUtils;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 描述：注册页面
 */
public class FindUserPwdActivity extends BaseActivity implements OnClickListener {
	private final static int MSG_USERNAME_NULL = 0x01;
	private final static int MSG_GET_VERIFY_SUCCESS = 0x02;
	private final static int MSG_GET_VERIFY_FAILED = 0x03;
	private final static int MSG_USERNAME_OR_VERIFY_NULL = 0x04;
	private final static int MSG_VERIFY_FAILED = 0x06;
	private final static int MSG_USER_FORMAT_ERR = 0x08;
	private final static int MSG_TIME = 0x09;
	private final static int TIME_OVER = 0x0a;
	private final static int MSG_PASSWORD_NULL = 0x0b;
	private final static int MSG_MODIFY_PASS = 0x0c;
	private final static int MSG_MODIFY_FAILSE = 0x0d;

	private int mytime=0;
	private Timer timer;

	private TextView backTv;
	private EditText userEt;
	private EditText verifyEt;
	private Button verifyBt;
	private Button ensureBt;
	private EditText pwdEt;
	private EditText pwd2Et;

	@Override
	public void onClick(View v) {
		if(v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.backTv:
			this.finish();
			break;
		case R.id.verifyBt:
			doGetVerify();
			break;
		case R.id.ensureBt:
			doModifyPwd();
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		setContentView(R.layout.find_pwd_layout);

		backTv = (TextView) findViewById(R.id.backTv);
		userEt = (EditText) findViewById(R.id.userEt);
		verifyEt = (EditText) findViewById(R.id.verifyEt);
		verifyBt = (Button) findViewById(R.id.verifyBt);
		ensureBt = (Button) findViewById(R.id.ensureBt);
		pwdEt = (EditText) findViewById(R.id.pwdEt);
		pwd2Et = (EditText) findViewById(R.id.pwd2Et);
	}

	@Override
	protected void setAttribute() {
		backTv.setOnClickListener(this);
		verifyBt.setOnClickListener(this);
		ensureBt.setOnClickListener(this);

		userEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 11) {
					Toast.makeText(FindUserPwdActivity.this, R.string.tele_num_lage, Toast.LENGTH_SHORT).show();
					userEt.setText(s.toString().substring(0, 11));
					userEt.setSelection(11);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		pwdEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				if (s.length() > 20) {
					Toast.makeText(FindUserPwdActivity.this, getString(R.string.passwork_num_lage), Toast.LENGTH_SHORT).show();
					pwdEt.setText(s.toString().substring(0, 20));
					pwdEt.setSelection(20);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		pwd2Et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				if (s.length() > 20) {
					Toast.makeText(FindUserPwdActivity.this, getString(R.string.passwork_num_lage), Toast.LENGTH_SHORT).show();
					pwdEt.setText(s.toString().substring(0, 20));
					pwdEt.setSelection(20);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		verifyEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 6) {
					Toast.makeText(FindUserPwdActivity.this, R.string.verify_lage, Toast.LENGTH_SHORT).show();
					verifyEt.setText(s.toString().substring(0, 6));
					verifyEt.setSelection(6);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void doModifyPwd() {
		showProgress("", "");

		String username = userEt.getText().toString().trim();
		String pwd1 = pwdEt.getText().toString().trim();
		String pwd2 = pwd2Et.getText().toString().trim();
		String verify = verifyEt.getText().toString().trim();

		if (TextUtils.isEmpty(username)) {
			mHandler.obtainMessage(MSG_USERNAME_OR_VERIFY_NULL, getString(R.string.phone_num)).sendToTarget();
		} else if(TextUtils.isEmpty(pwd1)){
			mHandler.obtainMessage(MSG_PASSWORD_NULL, getString(R.string.password_null)).sendToTarget();
		} else if(pwd1.length()<6){
			dismissProgress();
			Toast.makeText(FindUserPwdActivity.this, getString(R.string.password_lenth), Toast.LENGTH_SHORT).show();	
		} else if(TextUtils.isEmpty(pwd2)){
			mHandler.obtainMessage(MSG_PASSWORD_NULL, getString(R.string.password_null)).sendToTarget();
		} else if(pwd2.length()<6){
			dismissProgress();
			Toast.makeText(FindUserPwdActivity.this, getString(R.string.password_lenth), Toast.LENGTH_SHORT).show();	
		} else if(!pwd2.equals(pwd1)){
			dismissProgress();
			Toast.makeText(FindUserPwdActivity.this, getString(R.string.password_not_same_string), Toast.LENGTH_SHORT).show();	
		} else if(TextUtils.isEmpty(verify)) {
			mHandler.obtainMessage(MSG_USERNAME_OR_VERIFY_NULL, getString(R.string.verify)).sendToTarget();
		} else if(!AndroidUtils.isMobileNO(username)) {
			mHandler.obtainMessage(MSG_USER_FORMAT_ERR, getString(R.string.user_format_err)).sendToTarget();
		} else if(verify.length()!=6){
			mHandler.obtainMessage(MSG_USER_FORMAT_ERR, getString(R.string.verify_mini)).sendToTarget();
		} else {
			mCenter.cChangeUserPasswordWithCode(username, verify, pwd1);
		}
	}

	private void doGetVerify() {
		showProgress("", "");

		String username = userEt.getText().toString().trim();

		if (TextUtils.isEmpty(username)) {
			mHandler.obtainMessage(MSG_USERNAME_OR_VERIFY_NULL, getString(R.string.phone_num)).sendToTarget();
		} else if(!AndroidUtils.isMobileNO(username)) {
			mHandler.obtainMessage(MSG_USER_FORMAT_ERR, getString(R.string.user_format_err)).sendToTarget();
		} else {
			mCenter.cRequestSendVerifyCode(username);
		}
	}

	@Override
	protected void didRequestSendVerifyCode(int error, String errorMessage) {
		Log.i("error message ", error + " " + errorMessage);
		if (error == 0) {// 发送成功
			Message msg = new Message();
			msg.what = MSG_GET_VERIFY_SUCCESS;
			msg.obj = "发送成功";
			mHandler.sendMessage(msg);
		} else {// 发送失败
			Message msg = new Message();
			msg.what = MSG_GET_VERIFY_FAILED;
			msg.obj = errorMessage;
			mHandler.sendMessage(msg);
		}
	};

	@Override
	protected void didChangeUserPassword(int error, String errorMessage) {
		if (error == 0) {// 修改成功
			Message msg = new Message();
			msg.what = MSG_MODIFY_PASS;
			msg.obj = "修改成功";
			mHandler.sendMessage(msg);
		} else {// 发送失败
			Message msg = new Message();
			msg.what = MSG_MODIFY_FAILSE;
			msg.obj = errorMessage;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);

			if (msg == null) {
				return;
			}

			Object obj = msg.obj;

			switch (msg.what) {
			case MSG_TIME:
				verifyBt.setText(String.format(getString(R.string.retry_send_verify_code), mytime));
				break;
			case MSG_PASSWORD_NULL:
			case MSG_MODIFY_FAILSE:
			case MSG_USERNAME_NULL:
			case MSG_USERNAME_OR_VERIFY_NULL:
			case MSG_VERIFY_FAILED:
			case MSG_USER_FORMAT_ERR:
				dismissProgress();
				Toast.makeText(FindUserPwdActivity.this, String.valueOf(obj), Toast.LENGTH_SHORT).show();							
				break;
			case MSG_GET_VERIFY_FAILED:
				dismissProgress();
				verifyBt.setClickable(true);
				Toast.makeText(FindUserPwdActivity.this, String.valueOf(obj), Toast.LENGTH_SHORT).show();				
				break;
			case MSG_GET_VERIFY_SUCCESS:
				dismissProgress();
				Toast.makeText(FindUserPwdActivity.this, String.valueOf(obj), Toast.LENGTH_SHORT).show();

				verifyBt.setClickable(false);
				timer = new Timer();
				mytime = 60;
				TimerTask timetask = new TimerTask() {

					public void run() {
						mytime--;

						Message message = new Message();


						if (mytime <= 0) {
							message.what = TIME_OVER;
							mytime=0;
						}else{
							message.what = MSG_TIME;
						}
						mHandler.sendMessage(message);

					}
				};
				timer.schedule(timetask, 10, 1000);
				break;
			case TIME_OVER:
				verifyBt.setClickable(true);
				verifyBt.setText(R.string.request_verify_code_string);
				if(timer != null) {//先判空
					timer.cancel();
				timer=null;
				}
				break;
			case MSG_MODIFY_PASS:
				dismissProgress();
				Toast.makeText(FindUserPwdActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				FindUserPwdActivity.this.finish();
				break;
			default:
				break;
			}
		};
	};
}
