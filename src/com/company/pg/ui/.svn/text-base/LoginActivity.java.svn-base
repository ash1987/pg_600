package com.company.pg.ui;

import com.company.pg.R;
import com.company.pg.base.BaseActivity;
import com.company.pg.widget.CustomToast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 描述：登录页面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private final static int MSG_USER_OR_PWD_NULL = 0x01;
	private final static int MSG_LOGIN_SUCCESS = 0x02;
	private final static int MSG_LOGIN_FAILED = 0x03;
	private final static int MSG_USER_FORMAT_ERR = 0x04;

	private EditText loginUsernameEt;
	private EditText loginPasswordEt;
	private Button loginBt;
	private CheckBox rememberPwdCb;
	private CheckBox autoLoginCb;
	private TextView forgetPwdTv;
	private TextView registerUserTv;

	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {
		setContentView(R.layout.login);

		loginUsernameEt = (EditText) findViewById(R.id.login_username_et);
		loginPasswordEt = (EditText) findViewById(R.id.login_userpwd_et);
		loginBt = (Button) findViewById(R.id.loginBt);
		rememberPwdCb = (CheckBox) findViewById(R.id.rememberPwdCb);
		autoLoginCb = (CheckBox) findViewById(R.id.autoLoginCb);
		forgetPwdTv = (TextView) findViewById(R.id.forgetPwdTv);
		registerUserTv = (TextView) findViewById(R.id.registerUserTv);
	}

	@Override
	protected void setAttribute() {
		loginBt.setOnClickListener(this);
		forgetPwdTv.setOnClickListener(this);
		registerUserTv.setOnClickListener(this);

		//登录名长度限制
		loginUsernameEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 25) {
					Toast.makeText(LoginActivity.this, R.string.username_lage, Toast.LENGTH_SHORT).show();
					loginUsernameEt.setText(s.toString().substring(0, 25));
					loginUsernameEt.setSelection(25);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		loginPasswordEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				if (s.length() > 20) {
					Toast.makeText(LoginActivity.this, getString(R.string.passwork_num_lage), Toast.LENGTH_SHORT).show();
					loginPasswordEt.setText(s.toString().substring(0, 20));
					loginPasswordEt.setSelection(20);
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
		
		if(setmanager.isRememberPwd() && !TextUtils.isEmpty(setmanager.getUserName())&& !TextUtils.isEmpty(setmanager.getPassword())) {
			loginUsernameEt.setText(setmanager.getUserName());
			loginPasswordEt.setText(setmanager.getPassword());
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg == null) {
				return;
			}

			Object obj = msg.obj;

			switch (msg.what) {
			case MSG_USER_OR_PWD_NULL:
				dismissProgress();
				Toast.makeText(LoginActivity.this, String.valueOf(obj), Toast.LENGTH_SHORT).show();				
				break;
			case MSG_LOGIN_FAILED:
				dismissProgress();
				Toast.makeText(LoginActivity.this, String.valueOf(obj), Toast.LENGTH_SHORT).show();				
				break;
			case MSG_USER_FORMAT_ERR:
				dismissProgress();
				Toast.makeText(LoginActivity.this, String.valueOf(obj), Toast.LENGTH_SHORT).show();				
				break;
			case MSG_LOGIN_SUCCESS:
				dismissProgress();
				
				if(rememberPwdCb.isChecked()) {
					setmanager.setRememberPwd(true);
					
					setmanager.setUserName(loginUsernameEt.getText().toString().trim());
					setmanager.setPassword(loginPasswordEt.getText().toString().trim());
				} else {
					setmanager.setRememberPwd(false);
				}
				
				if(autoLoginCb.isChecked()) {
					setmanager.setAutoLogin(true);
				} else {
					setmanager.setAutoLogin(false);
				}
				
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				LoginActivity.this.finish();
				break;
			default:
				break;
			}
		};
	};


	private void doLogin() {
		showProgress("", getString(R.string.loading));

		String username = loginUsernameEt.getText().toString().trim();
		String password = loginPasswordEt.getText().toString().trim();

		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			dismissProgress();
			if(TextUtils.isEmpty(username)){
				mHandler.obtainMessage(MSG_USER_OR_PWD_NULL, getString(R.string.please_input_username)).sendToTarget();
			} else {
				mHandler.obtainMessage(MSG_USER_OR_PWD_NULL, getString(R.string.input_new_password)).sendToTarget();
			}

		} else if(password.length() < 6){
			mHandler.obtainMessage(MSG_USER_FORMAT_ERR, getString(R.string.passwork_lenth_err)).sendToTarget();
		} else {
			mCenter.cLogin(username, password);
		}
	}

	/* 
	 * 登录成功回调
	 */
	@SuppressLint("NewApi")
	@Override
	protected void didUserLogin(int error, String errorMessage, String uid, String token) {
		if (!uid.isEmpty()&&!token.isEmpty()) {//登陆成功
			setmanager.setUid(uid);
			setmanager.setToken(token);
			Message msg = new Message();
			msg.what = MSG_LOGIN_SUCCESS;
			msg.obj = "登录成功";
			mHandler.sendMessage(msg);
		} else {//登陆失败
			Message msg = new Message();
			msg.what = MSG_LOGIN_FAILED;
			msg.obj = errorMessage;
			mHandler.sendMessage(msg);
		}
	};
	
	private long touchTime = 0;// 按第一次返回键的时间
	private long waitTime = 2000;// 两次返回键的有效时间

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 实现按两次“返回键”按退出程序
			long currentTime = System.currentTimeMillis();
			if (currentTime - touchTime > waitTime) {
				// Toast提示再次点击
				CustomToast.getToast(LoginActivity.this,
						getResources().getString(R.string.click_again_exit),
						Toast.LENGTH_SHORT).show();
				touchTime = currentTime;// 记录第一次点击的时间
			} else {
				// 退出程序，关闭Activity并结束进程
				this.finish();
			}
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if(v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.loginBt:
			doLogin();
			break;
		case R.id.registerUserTv:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		case R.id.forgetPwdTv:
			startActivity(new Intent(LoginActivity.this, FindUserPwdActivity.class));
			break;
		default:
			break;
		}
	}
}
