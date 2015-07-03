/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zxing;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import zxing.camera.CameraManager;
import zxing.decoding.DecodeThread;
import zxing.utils.CaptureActivityHandler;
import zxing.utils.InactivityTimer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.company.pg.MyApplication;
import com.company.pg.R;
import com.company.pg.base.BaseActivity;
import com.company.pg.bean.ControlDevice;
import com.company.pg.utils.DialogManager;
import com.company.pg.utils.ToastUtils;
import com.google.zxing.Result;
import com.xtremeprog.xpgconnect.XPGWifiDevice;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends BaseActivity implements
		SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();
	
	/** 登陆设备超时时间 */
	private int LoginDeviceTimeOut = 60000;
	
	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private InactivityTimer inactivityTimer;

	private SurfaceView scanPreview = null;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;

	private String product_key, passcode, did;
	private Button btnCancel;
	private ImageView ivReturn;
	
	/** The progress dialog. */
	private ProgressDialog progressDialog;
	
	/** 当前操作的设备 */
	protected static XPGWifiDevice mXpgWifiDevice;
	
	/**
	 * ClassName: Enum handler_key. <br/>
	 * <br/>
	 * 
	 */
	private enum handler_key {

		START_BIND,

		SUCCESS,

		FAILED,

		FOUND,
		
		LOGIN_SUCCESS,
		
		LOGIN_TIMEOUT,
		
		LOGIN_FAIL,
	}

	/**
	 * The handler.
	 */
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {

			case START_BIND:
				startBind(passcode, did);
				break;

			case SUCCESS:
//				ToastUtils.showShort(CaptureActivity.this, "添加成功");
//				IntentUtils.getInstance().startActivity(CaptureActivity.this,
//						DeviceListActivity.class);
//				finish();
				
				getList();
				break;
			case FAILED:
				ToastUtils.showShort(CaptureActivity.this, "添加失败，请返回重试");
				finish();
				break;
			case FOUND:
				if(deviceslist != null && deviceslist.size() > 0) {
					XPGWifiDevice tempDevice = deviceslist.get(0);
					
					if (tempDevice == null) {
						return;
						
					}
					if (tempDevice.isLAN()) {
						if (tempDevice.isBind(setmanager.getUid())) {
							// TODO 登陆设备
							Log.i(TAG,
									"本地登陆设备:mac=" + tempDevice.getPasscode() + ";ip="
											+ tempDevice.getIPAddress() + ";did="
											+ tempDevice.getDid() + ";passcode="
											+ tempDevice.getPasscode());
							loginDevice(tempDevice);
						} else {
							// TODO 未设备
//							Log.i(TAG,
//									"绑定设备:mac=" + tempDevice.getMacAddress() + ";ip="
//											+ tempDevice.getIPAddress() + ";did="
//											+ tempDevice.getDid() + ";passcode="
//											+ tempDevice.getPasscode());
//							Intent intent = new Intent(CaptureActivity.this,
//									BindingDeviceActivity.class);
//							intent.putExtra("mac", tempDevice.getMacAddress());
//							intent.putExtra("did", tempDevice.getDid());
//							startActivity(intent);
						}
					} else {
						if (!tempDevice.isOnline()) {
							// TODO 离线
							Log.i(TAG,
									"离线设备:mac=" + tempDevice.getPasscode() + ";ip="
											+ tempDevice.getIPAddress() + ";did="
											+ tempDevice.getDid() + ";passcode="
											+ tempDevice.getPasscode());
						} else {
							// TODO 登陆设备
							Log.i(TAG,
									"远程登陆设备:mac=" + tempDevice.getPasscode() + ";ip="
											+ tempDevice.getIPAddress() + ";did="
											+ tempDevice.getDid() + ";passcode="
											+ tempDevice.getPasscode());
							loginDevice(tempDevice);
						}
					}
				}
				break;
			case LOGIN_SUCCESS:
				DialogManager.dismissDialog(CaptureActivity.this, progressDialog);
				MyApplication.controlDevice = new ControlDevice(deviceslist.get(0), deviceslist.get(0).isBind(setmanager.getUid()));
				
				ToastUtils.showShort(CaptureActivity.this, "连接成功");
				finish();
				break;
			case LOGIN_TIMEOUT:
				DialogManager.dismissDialog(CaptureActivity.this, progressDialog);
				ToastUtils.showShort(CaptureActivity.this, "连接失败");
				finish();
				break;
			case LOGIN_FAIL:
				DialogManager.dismissDialog(CaptureActivity.this, progressDialog);
				ToastUtils.showShort(CaptureActivity.this, "连接失败");
				finish();
				break;
			}
		}
	};

	private void startBind(final String passcode, final String did) {
		mCenter.cBindDevice(setmanager.getUid(), setmanager.getToken(), did,
				passcode, "");

	}

	private Rect mCropRect = null;

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	private boolean isHasSurface = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_capture);

		scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);

		inactivityTimer = new InactivityTimer(this);

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT,0.0f);
		animation.setDuration(4500);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		scanLine.startAnimation(animation);

		btnCancel = (Button) findViewById(R.id.btn_cancel);
		ivReturn = (ImageView) findViewById(R.id.iv_return);
		OnClickListener myClick = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CaptureActivity.this.finish();
			}
		};
		btnCancel.setOnClickListener(myClick);
		ivReturn.setOnClickListener(myClick);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("连接中，请稍候。");
		progressDialog.setCancelable(false);
	}

	@Override
	public void onResume() {
		super.onResume();

		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		handler = null;

		if (isHasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(scanPreview.getHolder());
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			scanPreview.getHolder().addCallback(this);
		}

		inactivityTimer.onResume();
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		cameraManager.closeDriver();
		if (!isHasSurface) {
			scanPreview.getHolder().removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!isHasSurface) {
			isHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isHasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * 
	 * @param bundle
	 *            The extras
	 */
	public void handleDecode(Result rawResult, Bundle bundle) {
		String text = rawResult.getText();
		Log.i("test", text);
		if (text.contains("product_key=") & text.contains("did=")
				&& text.contains("passcode=")) {

			inactivityTimer.onActivity();
			product_key = getParamFomeUrl(text, "product_key");
			did = getParamFomeUrl(text, "did");
			passcode = getParamFomeUrl(text, "passcode");
			Log.i("passcode product_key did", passcode + " " + product_key
					+ " " + did);
//			ToastUtils.showShort(this, "扫码成功");
			mHandler.sendEmptyMessage(handler_key.START_BIND.ordinal());

		} else {
			handler = new CaptureActivityHandler(this, cameraManager,
					DecodeThread.ALL_MODE);
		}
	}

	private String getParamFomeUrl(String url, String param) {
		String product_key = "";
		int startindex = url.indexOf(param + "=");
		startindex += (param.length() + 1);
		String subString = url.substring(startindex);
		int endindex = subString.indexOf("&");
		if (endindex == -1) {
			product_key = subString;
		} else {
			product_key = subString.substring(0, endindex);
		}
		return product_key;
	}

	@Override
	public void didBindDevice(int error, String errorMessage, String did) {
		Log.d("扫描结果", "error=" + error + ";errorMessage=" + errorMessage
				+ ";did=" + did);
		if (error == 0) {
			mHandler.sendEmptyMessage(handler_key.SUCCESS.ordinal());
		} else {
			Message message = new Message();
			message.what = handler_key.FAILED.ordinal();
			message.obj = errorMessage;
			mHandler.sendMessage(message);
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, cameraManager,
						DecodeThread.ALL_MODE);
			}

			initCrop();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		// camera error
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage("相机打开出错，请稍后重试");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public Rect getCropRect() {
		return mCropRect;
	}

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = cameraManager.getCameraResolution().y;
		int cameraHeight = cameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 处理获取设备列表动作
	 * 
	 * @return the list
	 */
	private void getList() {
		String uid = setmanager.getUid();
		String token = setmanager.getToken();
		mCenter.cGetBoundDevices(uid, token);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gizwits.framework.activity.BaseActivity#didDiscovered(int,
	 * java.util.List)
	 */
	@Override
	protected void didDiscovered(int error, List<XPGWifiDevice> deviceList) {
		deviceslist = deviceList;
		if(deviceslist != null && deviceslist.size() > 0) { 
			mHandler.sendEmptyMessage(handler_key.FOUND.ordinal());
		}
	}
	
	/**
	 * 登陆设备
	 * 
	 * @param xpgWifiDevice
	 *            the xpg wifi device
	 */
	private void loginDevice(XPGWifiDevice xpgWifiDevice) {
		DialogManager.showDialog(CaptureActivity.this, progressDialog);
		mXpgWifiDevice = xpgWifiDevice;
		mXpgWifiDevice.setListener(deviceListener);
		if(mXpgWifiDevice.isConnected()){
			mHandler.sendEmptyMessage(handler_key.LOGIN_SUCCESS.ordinal());
		}else{
//			mHandler.sendEmptyMessageDelayed(handler_key.LOGIN_TIMEOUT.ordinal(), LoginDeviceTimeOut);
			mXpgWifiDevice.login(setmanager.getUid(), setmanager.getToken());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gizwits.framework.activity.BaseActivity#didLogin(com.xtremeprog.
	 * xpgconnect.XPGWifiDevice, int)
	 */
	@Override
	public void didLogin(XPGWifiDevice device, int result) {
		handler.removeMessages(handler_key.LOGIN_TIMEOUT.ordinal());
		if (result == 0) {
			mXpgWifiDevice = device;
			mHandler.sendEmptyMessage(handler_key.LOGIN_SUCCESS.ordinal());
		} else {
			mHandler.sendEmptyMessage(handler_key.LOGIN_FAIL.ordinal());
		}

	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void setAttribute() {
		
	}
}