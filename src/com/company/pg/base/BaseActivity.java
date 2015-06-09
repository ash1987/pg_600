package com.company.pg.base;

import java.util.ArrayList;
import java.util.List;

import com.company.pg.R;
import com.company.pg.sdk.MessageCenter;
import com.company.pg.setting.SettingManager;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiDeviceListener;
import com.xtremeprog.xpgconnect.XPGWifiSDK;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Activity基类，用于统一各个界面的弹出框等
 */
public abstract class BaseActivity extends Activity {
	private Dialog mProgressDialog;

	/** 指令管理器 */
	protected MessageCenter mCenter;

	/** SharePreference处理类 */
	protected SettingManager setmanager;

	/** 设备列表 */
	protected static List<XPGWifiDevice> deviceslist = new ArrayList<XPGWifiDevice>();

	//注册监听器 
	private XPGWifiSDKListener sdkListener = new XPGWifiSDKListener(){ 
		@Override 
		public void didRegisterUser(int error, String errorMessage, String uid,String token)
		{
			BaseActivity.this.didRegisterUser(error, errorMessage, uid, token); 
		}

		@Override
		public void didBindDevice(int error, String errorMessage, String did) {
			BaseActivity.this.didBindDevice(error, errorMessage, did);
		};

		@Override
		public void didUnbindDevice(int error, String errorMessage, String did) {
			BaseActivity.this.didUnbindDevice(error, errorMessage, did);
		};

		@Override
		public void didChangeUserPassword(int error, String errorMessage) {
			BaseActivity.this.didChangeUserPassword(error, errorMessage);
		};

		@Override
		public void didRequestSendVerifyCode(int error, String errorMessage) {
			BaseActivity.this.didRequestSendVerifyCode(error, errorMessage);
		};

		@Override
		public void didDiscovered(int error, java.util.List<XPGWifiDevice> devicesList) {
			BaseActivity.this.didDiscovered(error, devicesList);
		};

		@Override
		public void didUserLogin(int error, String errorMessage, String uid, String token) {
			BaseActivity.this.didUserLogin(error, errorMessage, uid, token);
		};

		public void didUserLogout(int error, String errorMessage) {
			BaseActivity.this.didUserLogout(error, errorMessage);
		};

		@Override
		public void didSetDeviceWifi(int error, XPGWifiDevice device) {
			BaseActivity.this.didSetDeviceWifi(error, device);
		};

		@Override
		public void didGetSSIDList(int error, java.util.List<com.xtremeprog.xpgconnect.XPGWifiSSID> ssidInfoList) {
			BaseActivity.this.didGetSSIDList(error, ssidInfoList);
		};
	}; 

	/**
	 * XPGWifiDeviceListener
	 * <P>
	 * 设备属性监听器。 设备连接断开、获取绑定参数、获取设备信息、控制和接受设备信息相关.
	 */
	protected XPGWifiDeviceListener deviceListener = new XPGWifiDeviceListener() {

		@Override
		public void didLogin(XPGWifiDevice device, int result) {
			BaseActivity.this.didLogin(device, result);
		};

		@Override
		public void didDeviceOnline(XPGWifiDevice device, boolean isOnline) {
			BaseActivity.this.didDeviceOnline(device, isOnline);
		};

		@Override
		public void didDisconnected(XPGWifiDevice device) {
			BaseActivity.this.didDisconnected(device);
		};

		@Override
		public void didReceiveData(XPGWifiDevice device, java.util.concurrent.ConcurrentHashMap<String,Object> dataMap, int result) {
			BaseActivity.this.didReceiveData(device, dataMap, result);
		};

		@Override
		public void didQueryHardwareInfo(XPGWifiDevice device, int result, java.util.concurrent.ConcurrentHashMap<String,String> hardwareInfo) {
			BaseActivity.this.didQueryHardwareInfo(device, result, hardwareInfo);
		};
	};

	/**
	 * 通过did和mac在列表寻找对应的device.
	 * 
	 * @param mac
	 *            the mac
	 * @param did
	 *            the did
	 * @return the XPG wifi device
	 */
	public static XPGWifiDevice findDeviceByMac(String mac, String did) {
		XPGWifiDevice xpgdevice = null;
		Log.i("count", BaseActivity.deviceslist.size() + "");
		for (int i = 0; i < BaseActivity.deviceslist.size(); i++) {
			XPGWifiDevice device = deviceslist.get(i);
			if (device != null) {
				Log.i("deviceMac", device.getMacAddress());
				if (device != null && device.getMacAddress().equals(mac)
						&& device.getDid().equals(did)) {
					xpgdevice = device;
					break;
				}
			}

		}

		return xpgdevice;
	}

	/**
	 * 注册用户结果回调接口.
	 *  
	 * @param error
	 *            结果代码
	 * @param errorMessage
	 *            错误信息 
	 * @param uid
	 *            the 用户id
	 * @param token 
	 *            the 授权令牌
	 */ 
	protected void didRegisterUser(int error, String errorMessage, String uid, String token) {
		//实现逻辑
	}

	/**
	 * 回调接口，返回修改用户密码结果（对应changeUserPassword、changeUserPasswordByCode）
	 * @param error
	 * 0为成功，其他失败
	 * @param errorMessag
	 * 错误信息（无错误则为空或null）
	 * @see 触发函数：XPGWifiSDK changeUserPassword、changeUserPasswordByCode、changeUserPasswordByEmail
	 */
	protected void didChangeUserPassword(int error, String errorMessage) {
	};

	/**
	 * 回调接口，返回请求向指定手机发送验证码的结果
	 * @param error
	 * 0为成功，其他失败
	 * @param errorMessag
	 * 错误信息（无错误则为空或null）
	 * @see 触发函数：XPGWifiSDK requestSendVerifyCode
	 */
	protected void didRequestSendVerifyCode(int error, String errorMessage) {
	};

	/**
	 * 回调接口，返回发现设备的结果
	 * @param error
	 * 0为成功，其他失败
	 * @param deviceList
	 * 发现的设备，为 XPGWifiDevice 的集合
	 * @see 触发函数：XPGWifiSDK getBoundDevices
	 */
	protected void didDiscovered(int error, java.util.List<XPGWifiDevice> devicesList) {

	}

	/**
	 * 回调接口，返回用户登录的结果
	 * @param error
	 * 0为成功，其他失败
	 * @param errorMessag
	 * 错误信息（无错误则为空或null）
	 * @param uid
	 * 登录成功后得到的uid（不成功则为空或null）
	 * @param token
	 * 登录成功后得到的token（不成功则为空或null）
	 * @see 触发函数：XPGWifiSDK userLoginAnonymous userLoginWithUserName userLoginWithThirdAccountType
	 */
	protected void didUserLogin(int error, String errorMessage, String uid, String token) {
	};

	/**
	 * 回调接口，返回用户注销的结果
	 * @param error
	 * 0为成功，其他失败
	 * @param errorMessag
	 * 错误信息（无错误则为空或null）
	 * @see 触发函数：XPGWifiSDK userLogout
	 */
	protected void didUserLogout(int error, String errorMessage) {
	};


	/**
	 * 回调接口，返回设备配置的结果
	 * @param error
	 * 配置结果 成功或失败 如果配置失败，device为null
	 * @param device
	 * 已配置成功的设备
	 * @see 触发函数：XPGWifiSDK setDeviceWifi
	 */
	protected void didSetDeviceWifi(int error, XPGWifiDevice device) {
	}

	/**
	 * 回调接口，返回设备 Soft AP 模式下的 SSID 列表
	 * @param error
	 * 获取结果 成功或失败 如果获取失败，ssidList为空或null
	 * @param ssidList
	 * 为设备能搜索到的 XPGWifiSSID 对象的集合
	 * @see 触发函数：XPGWifiSDK getSSIDList
	 */
	protected void didGetSSIDList(int error, java.util.List<com.xtremeprog.xpgconnect.XPGWifiSSID> ssidInfoList) {
	};

	/**
	 * 回调接口，返回绑定结果
	 * @param did
	 * 设备 did
	 * @param error
	 * 0为成功，其他失败
	 * @param errorMessage
	 * 错误信息（无错误则为空或null）
	 * @see 触发函数：XPGWifiSDK bindDevice
	 */
	public void didBindDevice(int error, String errorMessage, String did) {
	};

	/**
	 * 回调接口，返回解绑结果
	 * @param error
	 * 0为成功，其他失败
	 * @param did
	 * 设备 did
	 * @param errorMessage
	 * 错误信息（无错误则为空或null）
	 * @see 触发函数：XPGWifiSDK unBindDevice
	 */
	public void didUnbindDevice(int error, String errorMessage, String did) {
	};

	/**
	 * 回调接口，返回设备登录结果
	 * @param device
	 * 设备对象
	 * @param result
	 * 0为成功，非0失败
	 * @see 触发函数：XPGWifiDevice login
	 */
	public void  didLogin(XPGWifiDevice device, int result) {
	};

	/**
	 * 回调接口，返回获取到的硬件信息
	 * @param device
	 * 设备对象
	 * @param result
	 * 0为成功，非0失败
	 * @param hardwareInfo
	 * XPGWifiHardwareInfo对象（内含硬件信息，result非0时为空或null）
	 */
	public void didQueryHardwareInfo(XPGWifiDevice device, int result, java.util.concurrent.ConcurrentHashMap<String,String> hardwareInfo) {
	};

	/**
	 * 回调接口，当设备在线状态发生变化时会被触发
	 * @param device
	 * 设备对象
	 * @param isOnline
	 * true=在线，false=不在线
	 */
	public void didDeviceOnline(XPGWifiDevice device, boolean isOnline) {
	};

	/**
	 * 回调接口，触发时机为设备主动或被动断开以及异常断开
	 * @see 触发函数：XPGWifiDevice disconnect
	 */
	public void didDisconnected(XPGWifiDevice device) {
	}

	/**
	 * 回调接口，返回设备上发的数据内容，包括设备控制命令的应答、设备运行状态的上报、设备报警、设备故障信息
	 * @param device
	 * 设备对象
	 * @param dataMap
	 * dataMap包含data、binary、faults、alerts四个key，data、faults、alerts对应Value的类型为String（JSON格式），其格式参考 write 方法，binary对应Value的类型为byte[]
	 * @param result
	 * 0为成功，非0失败
	 * @see 触发函数：XPGWifiDevice write
	 */
	public boolean didReceiveData(XPGWifiDevice device, java.util.concurrent.ConcurrentHashMap<String,Object> dataMap, int result) {
		return true;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//每次启动activity都要注册一次sdk监听器，保证sdk状态能正确回调 
		XPGWifiSDK.sharedInstance().setListener(sdkListener);

		mCenter = MessageCenter.getInstance(this.getApplicationContext());
		setmanager = new SettingManager(this);

		initData();
		initView();
		setAttribute();
	}

	// 初始化数据
	protected abstract void initData();

	// 初始化UI控件
	protected abstract void initView();

	// 初始化UI控件
	protected abstract void setAttribute();

	public void onResume() {
		super.onResume();
		mCenter.getXPGWifiSDK().setListener(sdkListener);
	}

	/**
	 * 显示等待框
	 * 
	 * @param title
	 * @param message
	 */
	public void showProgress(String title, String message) {
		try {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				return;
			}
			mProgressDialog = new Dialog(this, R.style.CustomDialog);
			mProgressDialog.setContentView(R.layout.dialog_progress);
			TextView textView = (TextView) mProgressDialog
					.findViewById(R.id.progress_msg);
			textView.setText(message);
			mProgressDialog.show();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消等待框
	 */
	public void dismissProgress() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			try {
				mProgressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
