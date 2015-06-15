package com.company.pg.ui;

import android.app.ProgressDialog;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.pg.R;
import com.company.pg.base.BaseActivity;
import com.xtremeprog.xpgconnect.XPGWifiDevice;

/*
 * 智能配置
 */
public class IntelligentConfigureActivity extends BaseActivity implements OnClickListener {
	private TextView page_title;
	private Button cancleBt;
	private Button okBt;
	private TextView wifiname;
	private EditText wifipwd;
	private Button bangdevice;

	@Override
	public void onClick(View v) {
		if(v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.cancleBt:
			this.finish();
			break;
		case R.id.okBt:
			this.finish();
			break;
		case R.id.bangdevice:
	
				dialog.show();
				String password = wifipwd.getText().toString().trim();
				// 发送airlink广播，把需要连接的wifi的ssid和password发给模块。
				mCenter.cSetAirLink(connnectedWifiName, password);
				
			break;
		default:
			break;
		}
	}


	/** 配置成功. */
	protected static final int SUCCESS = 0;

	/** 配置失败. */
	protected static final int FAIL = 1;

	/** 配置超时. */
	protected static final int TIEMOUT = 2;
	/** The dialog. */
	private ProgressDialog dialog;

	/** The handler. */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FAIL:
				dialog.cancel();
				Toast.makeText(IntelligentConfigureActivity.this, "配置失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case SUCCESS:
				dialog.cancel();
				Toast.makeText(IntelligentConfigureActivity.this, "配置成功", Toast.LENGTH_SHORT)
						.show();
				finish();
				break;

			case TIEMOUT:
				dialog.cancel();
				Toast.makeText(IntelligentConfigureActivity.this, "配置超时", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};

	
    String connnectedWifiName;
    
	@Override
	protected void initData() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);  
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
		Log.d("wifiInfo", wifiInfo.toString());  
		if (wifiInfo!=null) {
			connnectedWifiName= wifiInfo.getSSID();
			Log.d("SSID",wifiInfo.getSSID());  
		}else
			connnectedWifiName="";
		

	}

	@Override
	protected void initView() {
		setContentView(R.layout.intelligent_config_layout);

		page_title = (TextView) findViewById(R.id.page_title);
		cancleBt = (Button) findViewById(R.id.cancleBt);
		okBt = (Button) findViewById(R.id.okBt);
		wifiname = (TextView) findViewById(R.id.wifi_name);
		wifipwd = (EditText)findViewById(R.id.wifipwd);
		bangdevice = (Button) findViewById(R.id.bangdevice);
	}

	@Override
	protected void setAttribute() {
		cancleBt.setOnClickListener(this);
		okBt.setOnClickListener(this);
		bangdevice.setOnClickListener(this);
		wifiname.setText(connnectedWifiName);
		page_title.setText(R.string.smart_config_settings);
		dialog = new ProgressDialog(this);
		dialog.setMessage("配置中，请稍候...");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (dialog!=null&&dialog.isShowing()) {
			dialog.dismiss();
			dialog=null;
		}
		super.onDestroy();
	}
	
	/*
	 * 配置成功回调
	 * 
	 * @see
	 * com.xpg.gokit.activity.BaseActivity#didSetDeviceWifi(int error, XPGWifiDevice device)
	 */
	protected void didSetDeviceWifi(int error, XPGWifiDevice device) {
		// 通过airlink配置模块成功连上路由器后，回调该函数。
		Log.i("air link device", "" + device.getMacAddress() + " ");
		handler.sendEmptyMessage(SUCCESS);
	}
}
