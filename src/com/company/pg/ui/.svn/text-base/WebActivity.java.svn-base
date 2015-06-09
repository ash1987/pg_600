package com.company.pg.ui;

import java.lang.reflect.Field;
import com.company.pg.R;
import com.company.pg.base.BaseActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ZoomButtonsController;

public class WebActivity extends BaseActivity implements OnClickListener {
	private Handler mHandler = new Handler();
	private WebView webView;
	private String webTitle;
	private String url;
	private ImageView backIv;// 返回

	@Override
	public void onResume() {
		super.onResume();

		loadUrl();
	}

	private void loadUrl() {
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new MyWebChromeClient());

		webView.addJavascriptInterface(new JavaScriptInterface(), "androidWeb");
		// webView.loadUrl("file:///android_asset/recommend.html");
		webView.loadUrl(url);
		showProgress("", getString(R.string.loading));
	}

	private void hideZoomController(WebSettings websettings) {
		if (hideZoomByHoneycombApi(websettings)) {
			return;
		}

		hideZoomByReflection();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private boolean hideZoomByHoneycombApi(WebSettings websettings) {
		if (Build.VERSION.SDK_INT < 11) {
			return false;
		}

		websettings.setDisplayZoomControls(false);
		return true;
	}

	public void hideZoomByReflection() {
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

	final class MyWebViewClient extends WebViewClient {
		// 对网页中超链接按钮的响应
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.endsWith(".mp4")) {
				Intent it = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.parse(url);
				it.setDataAndType(uri, "video/*");
				startActivity(it);
			} else {
				view.loadUrl(url);
			}

			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			view.loadUrl("javascript:webMutual.activePlatform()");
			dismissProgress();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
	}

	final class MyWebChromeClient extends WebChromeClient {

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

		// 设置网页加载的进度条
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			WebActivity.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
					newProgress * 100);
			super.onProgressChanged(view, newProgress);
		}

		// 设置应用程序的标题title
		@Override
		public void onReceivedTitle(WebView view, String title) {
			WebActivity.this.setTitle(title);
			super.onReceivedTitle(view, title);
		}

		// 打印LOG
		@Override
		public boolean onConsoleMessage(ConsoleMessage cm) {
			return true;
		}
	}

	final class JavaScriptInterface {
		/*
		 * js调用本地方法,传参数in进来,in代表页面调用的接口
		 */
		public void execute(final String result) {
			mHandler.post(new Runnable() {

				public void run() {

				}
			});
		}

	}

	@Override
	protected void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			url = bundle.getString("url");
			webTitle = bundle.getString("webTitle");
		}
	}

	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.web_main);

		backIv = (ImageView) this.findViewById(R.id.backIv);
		webView = (WebView) findViewById(R.id.webview);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void setAttribute() {
		backIv.setVisibility(View.VISIBLE);
		backIv.setOnClickListener(this);

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
		// 默认使用缓存
		websettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		// 允许放大缩小
		websettings.setSupportZoom(true);
		// 显示放大缩小按钮
		websettings.setBuiltInZoomControls(true);
		hideZoomController(websettings);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
		} else if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backIv:
			this.finish();
			break;
		default:
			break;
		}
	}
}
