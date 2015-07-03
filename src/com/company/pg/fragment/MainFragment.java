package com.company.pg.fragment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;
import com.company.pg.Define;
import com.company.pg.MyApplication;
import com.company.pg.R;
import com.company.pg.base.BaseActivity;
import com.company.pg.widget.CustomDialog;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiErrorCode;
import com.xtremeprog.xpgconnect.XPGWifiSDK;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 主菜单界面
 */
public class MainFragment extends BaseFragment  implements OnClickListener{
	public static final String FRAGMENT_TAG = MainFragment.class
			.getSimpleName();

	/** 实体字段名，代表对应的项目. */
	private static final String KEY_ACTION = "entity0";

	/** 布防 */
	private static final String KEY_BUFAN_OFF = "bufan_Off";

	/** 撤防 */
	private static final String KEY_CHEFANG_OFF = "chefang_off";

	/** 在家布防 */
	private static final String KEY_ZBFAN_OFF = "zbfan_off";

	/** 紧急求救 */
	private static final String KEY_JINJBJ_OFF = "jinjbj_off";

	/** 声光报警 */
	private static final String KEY_SGBJ = "sgbj";

	/** 信息查询 */
	private static final String KEY_XXSC = "xxsc";

	/** 灯泡开与关 */
	private static final String KEY_LED_ONOFF = "LED_onoff";

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

	private CustomDialog customDialog;

	private ImageButton lockBtn,unlockBtn,main_talkback_btn,main_sethome_btn,main_sos_btn,main_alarm_btn,main_system_state_btn,main_siren_on_btn,main_siren_off_btn;

	//实例化监听器 
	XPGWifiSDKListener xpgWifiSDKListener = new XPGWifiSDKListener(){ 
		//注册用户回调
		public void didRegisterUser(int result, String errorMessage, String uid,String token) {
			if (result==XPGWifiErrorCode.XPGWifiError_NONE) 
			{ 
				//注册成功，处理注册成功的逻辑
				Log.e("====", "注册成功");
			}else{ 
				//注册失败，处理注册失败的逻辑 
				Log.e("====", "注册失败");
			} 
		}; 

		@Override
		public void didDiscovered(int error, List<XPGWifiDevice> devicesList) {
			// TODO Auto-generated method stub
			super.didDiscovered(error, devicesList);
			Log.e("====", devicesList.toString());
		}
	}; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//注册监听器 
		XPGWifiSDK.sharedInstance().setListener(xpgWifiSDKListener); 
		//调用SDK注册接口
		XPGWifiSDK.sharedInstance().registerUser(Define.plantUserName, Define.plantUserPwd); 
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment_layout, null);
		initView(view);
		setAttribute();
		return view;
	}

	// 初始化数据
	private void initData() {
		deviceStatu = new HashMap<String, Object>();
	}

	// 初始化控件
	private void initView(View view) {
		lockBtn = (ImageButton) view.findViewById(R.id.main_lock_btn);
		unlockBtn = (ImageButton) view.findViewById(R.id.main_unlock_btn);
		main_talkback_btn = (ImageButton) view.findViewById(R.id.main_talkback_btn);
		main_sethome_btn = (ImageButton) view.findViewById(R.id.main_sethome_btn);
		main_sos_btn = (ImageButton) view.findViewById(R.id.main_sos_btn);
		main_alarm_btn = (ImageButton) view.findViewById(R.id.main_alarm_btn);
		main_system_state_btn = (ImageButton) view.findViewById(R.id.main_system_state_btn);
		main_siren_on_btn = (ImageButton) view.findViewById(R.id.main_siren_on_btn);
		main_siren_off_btn = (ImageButton) view.findViewById(R.id.main_siren_off_btn);
	}

	// 设置控件属性
	private void setAttribute() {
		lockBtn.setOnClickListener(this);
		unlockBtn.setOnClickListener(this);
		main_talkback_btn.setOnClickListener(this);
		main_sethome_btn.setOnClickListener(this);
		main_sos_btn.setOnClickListener(this);
		main_alarm_btn.setOnClickListener(this);
		main_system_state_btn.setOnClickListener(this);
		main_siren_on_btn.setOnClickListener(this);
		main_siren_off_btn.setOnClickListener(this);

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
				Toast.makeText(MainFragment.this.getActivity(), sb.toString(), Toast.LENGTH_SHORT).show();
				break;
			case UPDATE_UI:
				//				boolean result = (Boolean) deviceStatu.get(KEY_LIGHT_ON_OFF);
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
		dm = MainFragment.this.getActivity().getApplicationContext()
				.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		customDialog = new CustomDialog(MainFragment.this.getActivity(),
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
		switch (v.getId()) {
		case R.id.main_lock_btn:
			if (xpgWifiDevice ==null || !xpgWifiDevice.isOnline()) {
				showAlertDialog("警告", "设备不在线，请在'智能配置'中设置!", "OK");
			} else {
				try {
					sendJson(KEY_BUFAN_OFF, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.main_unlock_btn:
			if (xpgWifiDevice ==null || !xpgWifiDevice.isOnline()) {
				showAlertDialog("警告", "设备不在线，请在'智能配置'中设置!", "OK");
			} else {
				try {
					sendJson(KEY_CHEFANG_OFF, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.main_talkback_btn:

			break;
		case R.id.main_sethome_btn:
			if (xpgWifiDevice ==null || !xpgWifiDevice.isOnline()) {
				showAlertDialog("警告", "设备不在线，请在'智能配置'中设置!", "OK");
			} else {
				try {
					sendJson(KEY_ZBFAN_OFF, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.main_sos_btn:
			if (xpgWifiDevice ==null || !xpgWifiDevice.isOnline()) {
				showAlertDialog("警告", "设备不在线，请在'智能配置'中设置!", "OK");
			} else {
				try {
					sendJson(KEY_JINJBJ_OFF, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.main_alarm_btn:
			if (xpgWifiDevice ==null || !xpgWifiDevice.isOnline()) {
				showAlertDialog("警告", "设备不在线，请在'智能配置'中设置!", "OK");
			} else {
				try {
					sendJson(KEY_SGBJ, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.main_system_state_btn:
			if (xpgWifiDevice ==null || !xpgWifiDevice.isOnline()) {
				showAlertDialog("警告", "设备不在线，请在'智能配置'中设置!", "OK");
			} else {
				try {
					sendJson(KEY_CHEFANG_OFF, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.main_siren_on_btn:
			if (xpgWifiDevice ==null || !xpgWifiDevice.isOnline()) {
				showAlertDialog("警告", "设备不在线，请在'智能配置'中设置!", "OK");
			} else {
				try {
					sendJson(KEY_LED_ONOFF, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.main_siren_off_btn:
			if (xpgWifiDevice ==null || !xpgWifiDevice.isOnline()) {
				showAlertDialog("警告", "设备不在线，请在'智能配置'中设置!", "OK");
			} else {
				try {
					sendJson(KEY_LED_ONOFF, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void lazyLoad() {

	}
}
