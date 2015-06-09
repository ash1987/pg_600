package com.company.pg.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;

import com.alibaba.fastjson.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

/**
 * 
 * 描述：用于异步请求接口，并提供回调式监听，来获取相关信息
 */
public abstract class AsyncRunner<T> {

	public final static String POST = "POST";
	public final static String GET = "GET";
	public final static String J_SON = "JSON";
	private RequestListener<T> requestListener;

	public AsyncRunner() {
	}

	public void request(Context context, String url, String method,
			RequestParams params, RequestListener<T> listener) {
		setManagerListener(listener);
		request(context, url, params, method);
		if (requestListener != null) {
			requestListener.OnStart();
		}
	}

	public void request(Context context, String action, JSONObject params,
			RequestListener<T> listener) {
		setManagerListener(listener);
		request(context, action, params);
		if (requestListener != null) {
			requestListener.OnStart();
		}
	}

	public void setManagerListener(RequestListener<T> onLoadListener) {
		requestListener = onLoadListener;
	}

	private class JSONPaserTaskOne extends AsyncTask<Void, Void, Integer> {
		private T t;
		private Context context;
		private String url;
		private RequestParams requestParams;
		private String httpMethod;

		public JSONPaserTaskOne(final Context context, final String url,
				final RequestParams requestParams, final String httpMethod) {
			this.context = context;
			this.url = url;
			this.requestParams = requestParams;
			this.httpMethod = httpMethod;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			String jsonString = "";
			try {
				InputStream inputStream = HttpManager.OpenUrl(context, url,
						httpMethod, requestParams,
						requestParams.getValue("pic"));
				jsonString = readInpuStream(inputStream);
				t = paserJSON(jsonString);
			} catch (MyHttpException e) {
				e.printStackTrace();
				if (requestListener != null)
					requestListener.onError(e);
				return -1;
			} catch (JSONException e) {// 捕获fastJson的解析异常
				e.printStackTrace();
				if (requestListener != null)
					requestListener.onError(e);
				return -1;
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 0 && requestListener != null)
				requestListener.OnPaserComplete(t);
			super.onPostExecute(result);

			this.cancel(true);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

	private class JSONPaserTaskTwo extends AsyncTask<Void, Void, Integer> {
		private T t;
		private Context context;
		private String action;
		private JSONObject requestParams;

		public JSONPaserTaskTwo(final Context context, final String action,
				final JSONObject requestParams) {
			this.context = context;
			this.action = action;
			this.requestParams = requestParams;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			String jsonString = "";
			try {
				InputStream inputStream = HttpManager.OpenUrl(context, action,
						requestParams, null);
				jsonString = readInpuStream(inputStream);
				t = paserJSON(parseXML(jsonString, action));
			} catch (MyHttpException e) {
				e.printStackTrace();
				if (requestListener != null)
					requestListener.onError(e);
				return -1;
			} catch (JSONException e) {// 捕获fastJson的解析异常
				e.printStackTrace();
				if (requestListener != null)
					requestListener.onError(e);
				return -1;
			} catch (Exception e) {// 捕获fastJson的解析异常
				e.printStackTrace();
				if (requestListener != null)
					requestListener.onError(e);
				return -1;
			}

			return 0;
		}
		
		private String parseXML(String result, String action) {
			if(TextUtils.isEmpty(result)) {
				return null;
			}
			
			String sqliteContent = action + "Result";
			int start = result.indexOf(sqliteContent) + sqliteContent.length() + 1;
			int end = result.indexOf("/" + sqliteContent) - 1;
			
			return result.substring(start, end);
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 0 && requestListener != null)
				requestListener.OnPaserComplete(t);
			super.onPostExecute(result);

			this.cancel(true);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

	/*
	 * protected void startJsonPaser(String json) { new
	 * JSONPaserTask().execute(json); }
	 */

	protected abstract T paserJSON(String json);

	/**
	 * 
	 * 请求接口数据，并在获取到数据后通过RequestListener将response回传给调用者
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            服务器地址
	 * @param params
	 *            存放参数的容器
	 * @param httpMethod
	 *            "GET"or “POST”
	 * @param listener
	 *            回调对象
	 */
	public void request(final Context context, final String url,
			final RequestParams params, final String httpMethod) {
		// 进行xml解析
		// new XMLPaserTask(context, url, params, httpMethod).execute();

		// 进行Json解析
		new JSONPaserTaskOne(context, url, params, httpMethod).execute();
	}

	public void request(final Context context, final String action,
			final JSONObject params) {
		// 进行xml解析
		// new XMLPaserTask(context, url, params, httpMethod).execute();

		// 进行Json解析
		new JSONPaserTaskTwo(context, action, params).execute();
	}

	private class XMLPaserTask extends AsyncTask<Void, Void, Integer> {
		private T t;
		private Context context;
		private String url;
		private RequestParams requestParams;
		private String httpMethod;

		public XMLPaserTask(final Context context, final String url,
				final RequestParams requestParams, final String httpMethod) {
			this.context = context;
			this.url = url;
			this.requestParams = requestParams;
			this.httpMethod = httpMethod;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			InputStream inputStream;
			try {
				inputStream = HttpManager.OpenUrl(context, url, httpMethod,
						requestParams, requestParams.getValue("pic"));
			} catch (MyHttpException e) {
				e.printStackTrace();
				if (requestListener != null)
					requestListener.onError(e);
				return -1;
			}
			t = paserXML(inputStream);
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 0 && requestListener != null) {
				requestListener.OnPaserComplete(t);
			}

			super.onPostExecute(result);
			this.cancel(true);

		}

	}

	protected abstract T paserXML(InputStream inputStream);

	/**
	 * 
	 * 读取HttpResponse数据
	 * 
	 * @param response
	 *            服务器返回对象
	 * @return String 服务器返回值
	 */
	private static String readInpuStream(InputStream inputStream) {
		String result = "";
		if (inputStream == null) {
			return result;
		}
		try {
			ByteArrayOutputStream content = new ByteArrayOutputStream();
			int readBytes = 0;
			byte[] sBuffer = new byte[512];
			while ((readBytes = inputStream.read(sBuffer)) != -1) {
				content.write(sBuffer, 0, readBytes);
			}
			result = new String(content.toByteArray());
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}