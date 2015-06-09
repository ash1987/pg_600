package com.company.pg.base;

import java.lang.reflect.Field;
import org.json.JSONException;
import org.json.JSONObject;

import com.company.pg.R;
import com.company.pg.utils.AndroidUtils;
import com.company.pg.widget.CustomToast;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

/**
 * 描述：〈描述〉
 */

@SuppressLint("NewApi")
public abstract class WebViewBaseActivity extends Activity {
	public boolean isRun = false;// 用于处理viewpager循环播放
	public boolean isDown = false;// 用于处理viewpager循环播放
	private Dialog mProgressDialog;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initData();
		initView();
		setAttribute();
	}
	/**
	 * 
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 
	 * 初始化布局
	 */
	protected abstract void initView();

	/**
	 * 
	 * 初始化参数
	 */
	protected abstract void setAttribute();
	
	/**
	 * 
	 * 页面加载完成
	 */
	protected void jsInterface(int model, String handler, String jsonData,
			String callbackId) {
	};

	@SuppressLint("JavascriptInterface")
	public void initWebView(WebView webView) {
		WebSettings websettings = webView.getSettings();

		if (Build.VERSION.SDK_INT >= 11) {
			if (webView.isHardwareAccelerated()) {
				// 如果开启硬件加速
				webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
			} else {
				webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
		}

		// 开启javascript设置
		websettings.setJavaScriptEnabled(true);

		// 应用可以有缓存
		websettings.setAppCacheEnabled(true);
		// 缓存最多可以有8M
		websettings.setAppCacheMaxSize(1024 * 1024 * 8);
		String appCacheDir = this.getApplicationContext()
				.getDir("cache", Context.MODE_PRIVATE).getPath();
		websettings.setAppCachePath(appCacheDir);

		// 应用可以有数据库
		websettings.setDatabaseEnabled(true);
		String dbPath = this.getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		websettings.setDatabasePath(dbPath);

		// 启用地理定位
		websettings.setGeolocationEnabled(true);
		// 设置定位的数据库路径
		websettings.setGeolocationDatabasePath(dbPath);

		// 设置可以使用localStorage
		websettings.setDomStorageEnabled(true);
		// 可以读取文件缓存(需要服务器配置manifest生效)
		websettings.setAllowFileAccess(true);
		// 使用缓存
		websettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 允许放大缩小
		websettings.setSupportZoom(true);

		// 提高渲染的优先级
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		// 图片加载渲染在加载完成后执行
		// webView.getSettings().setBlockNetworkImage(false);
		// 显示放大缩小按钮
		websettings.setBuiltInZoomControls(false);
		hideZoomController(websettings, webView);
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.addJavascriptInterface(new JavaScriptInterface(), "androidWeb");
	}

	// 与js进行交互的接口
	final class JavaScriptInterface {
		/*
		 * js调用本地方法,传参数in进来,in代表页面调用的接口
		 */
		public void execute(final String result) {
			mHandler.post(new Runnable() {

				public void run() {
					JSONObject object;
					try {
						object = new JSONObject(result);
						int model = -1;
						String handler = "";
						String jsonData = "";
						String callbackId = "";
						if (object.has("handler")) {
							handler = object.getString("handler");
						}
						if (object.has("jsonData")) {
							jsonData = object.getString("jsonData");
						}
						if (object.has("callbackId")) {
							callbackId = object.getString("callbackId");
						}
						model = Integer.parseInt(object.getString("model"));
						jsInterface(model, handler, jsonData, callbackId);
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			});
		}

	}

	// 加载网页
	protected void loadUrl(String url, WebView webView) {
		// 判断网络连接情况
		if (!AndroidUtils.isNetworkAvailable(this)) {
			CustomToast.getToast(this, getString(R.string.networkoff),
					Toast.LENGTH_SHORT).show();
			webView.setVisibility(View.GONE);
			return;
		}

		if (TextUtils.isEmpty(url)) {
			return;
		}
		webView.loadUrl(url);
		showProgress(null, null);
	}

	final class MyWebViewClient extends WebViewClient {
		// 对网页中超链接按钮的响应
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			dismissProgress();
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

	}

	final class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				Callback callback) {
			super.onGeolocationPermissionsShowPrompt(origin, callback);
			callback.invoke(origin, true, false);
		}

		// 扩充缓存的容量
		@Override
		public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
				QuotaUpdater quotaUpdater) {
			quotaUpdater.updateQuota(requiredStorage * 2);
		}

		// 扩充数据库的容量
		@Override
		public void onExceededDatabaseQuota(String url,
				String databaseIdentifier, long quota,
				long estimatedDatabaseSize, long totalQuota,
				QuotaUpdater quotaUpdater) {
			quotaUpdater.updateQuota(estimatedDatabaseSize * 2);
		}
	}

	private void hideZoomController(WebSettings websettings, WebView webView) {
		if (hideZoomByHoneycombApi(websettings)) {
			return;
		}

		hideZoomByReflection(webView);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private boolean hideZoomByHoneycombApi(WebSettings websettings) {
		if (Build.VERSION.SDK_INT < 11) {
			return false;
		}

		websettings.setDisplayZoomControls(false);
		return true;
	}

	public void hideZoomByReflection(WebView webView) {
		Class<?> classType;
		Field field;
		try {
			classType = WebView.class;
			field = classType.getDeclaredField("mZoomButtonsController");
			field.setAccessible(true);
			ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
					webView);
			mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
			try {
				field.set(webView, mZoomButtonsController);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
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

}
