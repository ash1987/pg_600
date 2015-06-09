package com.company.pg.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.company.pg.MyApplication;
import com.company.pg.R;
import com.company.pg.base.BaseActivity;
import com.company.pg.widget.CustomDialog;
import com.xtremeprog.xpgconnect.XPGWifiDevice;

/**
 * 描述：灯光设置
 */

public class LightsSettingActivity extends BaseActivity implements OnClickListener {
	/** 实体字段名，代表对应的项目. */
	private static final String KEY_ACTION = "entity0";

	/** 灯泡开与关 */
	private static final String KEY_LIGHT_ON_OFF = "LED_onoff";

	/** The Constant LOG. */
	protected static final int LOG = 1;

	/** The Constant RESP. */
	protected static final int RESP = 2;

	/** The Constant UPDATE_UI. */
	protected static final int UPDATE_UI = 3;

	/** The xpg wifi device. */
	XPGWifiDevice xpgWifiDevice;

	/** The device statu. */
	private HashMap<String, Object> deviceStatu;

	private RelativeLayout lightOnRl;
	private RelativeLayout lightOffRl;
	private Button cancleBt;
	private Button okBt;
	private CustomDialog customDialog;

	@Override
	protected void initData() {
		deviceStatu = new HashMap<String, Object>();
	}

	@Override
	protected void initView() {
		setContentView(R.layout.light_setting_layout);

		lightOnRl = (RelativeLayout) findViewById(R.id.lightOnRl);
		lightOffRl = (RelativeLayout) findViewById(R.id.lightOffRl);
		cancleBt = (Button) findViewById(R.id.cancleBt);
		okBt = (Button) findViewById(R.id.okBt);
	}

	@Override
	protected void setAttribute() {
		lightOnRl.setOnClickListener(this);
		lightOffRl.setOnClickListener(this);
		cancleBt.setOnClickListener(this);
		okBt.setOnClickListener(this);

		if(MyApplication.controlDevice != null) {
			xpgWifiDevice = BaseActivity.findDeviceByMac(MyApplication.controlDevice.getMac(),
					MyApplication.controlDevice.getDid());
		}
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 3];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 3] = hexArray[v >>> 4];
			hexChars[j * 3 + 1] = hexArray[v & 0x0F];
			hexChars[j * 3 + 2] = ' ';
		}
		return new String(hexChars);
	}

	/**
	 * 发送指令。格式为json。
	 * <p>
	 * 例如 {"entity0":{"attr2":74},"cmd":1}
	 * 其中entity0为gokit所代表的实体key，attr2代表led灯红色值，cmd为1时代表写入
	 * 。以上命令代表改变gokit的led灯红色值为74.
	 * 
	 * @param key
	 *            数据点对应的的json的key
	 * @param value
	 *            需要改变的值
	 * @throws JSONException
	 *             the JSON exception
	 */
	private void sendJson(String key, Object value) throws Exception {
		final JSONObject jsonsend = new JSONObject();
		JSONObject jsonparam = new JSONObject();
		jsonsend.put("cmd", 1);
		jsonparam.put(key, value);
		jsonsend.put(KEY_ACTION, jsonparam);
		Log.i("sendjson", jsonsend.toString());
		mCenter.cWrite(xpgWifiDevice, jsonsend);
	}

	@Override
	public boolean didReceiveData(XPGWifiDevice device, ConcurrentHashMap<String, Object> dataMap,int result){

		if (dataMap.get("data") != null) {
			Log.i("info", (String)dataMap.get("data"));
			Message msg = new Message();
			msg.obj = dataMap.get("data");
			msg.what = RESP;
			handler.sendMessage(msg);
		}

		if (dataMap.get("alters") != null) {
			Log.i("info", (String)dataMap.get("alters"));
			Message msg = new Message();
			msg.obj = dataMap.get("alters");
			msg.what = LOG;
			handler.sendMessage(msg);
		}

		if (dataMap.get("faults") != null) {
			Log.i("info", (String)dataMap.get("faults"));
			Message msg = new Message();
			msg.obj = dataMap.get("faults");
			msg.what = LOG;
			handler.sendMessage(msg);
		}

		if (dataMap.get("binary") != null) {
			Log.i("info", "Binary data:" + bytesToHex((byte[])dataMap.get("binary")));
		}

		return true;
	}

	/** The handler. */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case RESP:
				String data = msg.obj.toString();

				try {
					showDataInUI(data);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case LOG:
				StringBuilder sb = new StringBuilder();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject((String)msg.obj);
					for (int i = 0; i < jsonObject.length(); i++) {
						sb.append(jsonObject.names().getString(i) + " " + jsonObject.getInt(jsonObject.names().getString(i)) + "\r\n");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Toast.makeText(LightsSettingActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
				break;
			case UPDATE_UI:
				boolean result = (Boolean) deviceStatu.get(KEY_LIGHT_ON_OFF);
				break;
			}
		}
	};

	/**
	 * Show data in ui.
	 * 
	 * @param data
	 *            the data
	 * @throws JSONException
	 *             the JSON exception
	 */
	private void showDataInUI(String data) throws JSONException {
		Log.i("revjson", data);
		JSONObject receive = new JSONObject(data);
		@SuppressWarnings("rawtypes")
		Iterator actions = receive.keys();
		while (actions.hasNext()) {
			String action = actions.next().toString();
			// 忽略特殊部分
			if (action.equals("cmd") || action.equals("qos")
					|| action.equals("seq") || action.equals("version")) {
				continue;
			}
			JSONObject params = receive.getJSONObject(action);
			@SuppressWarnings("rawtypes")
			Iterator it_params = params.keys();
			while (it_params.hasNext()) {
				String param = it_params.next().toString();
				Object value = params.get(param);
				deviceStatu.put(param, value);
			}
		}
		Message msg = new Message();
		msg.obj = data;
		msg.what = UPDATE_UI;
		handler.sendMessage(msg);
	}

	private void showAlertDialog(String title, String content,
			String positive) {
		if (customDialog!=null && customDialog.isShowing()) {
			return;
		}

		DisplayMetrics dm = new DisplayMetrics();
		dm = this.getApplicationContext()
				.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		customDialog = new CustomDialog(this,
				R.layout.custom_dialog, 1, screenWidth);

		customDialog.setDialogGravity(Gravity.CENTER);
		customDialog.setTitleContent(title, content);
		customDialog.setPositiveButton(positive,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		customDialog.show();
	}

	@Override
	public void onClick(View v) {
		if(v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.lightOnRl:
			if (xpgWifiDevice ==null || !xpgWifiDevice.isOnline()) {
				showAlertDialog("警告", "设备不在线，不可以做控制!", "OK");
			} else {
				try {
					sendJson(KEY_LIGHT_ON_OFF, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.lightOffRl:
			if (xpgWifiDevice ==null || !xpgWifiDevice.isOnline()) {
				showAlertDialog("警告", "设备不在线，不可以做控制!", "OK");
			} else {
				try {
					sendJson(KEY_LIGHT_ON_OFF, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.cancleBt:
			this.finish();
			break;
		case R.id.okBt:
			this.finish();
			break;
		default:
			break;
		}
	}
}
