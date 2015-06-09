package com.company.pg.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.company.pg.R;
import com.company.pg.utils.AndroidUtils;
import com.company.pg.utils.LogUtil;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 描述：Http管理类
 */

public class HttpManager {
	private static final String TAG = "pg_600";
	
	public static final String HTTPMETHOD_POST = "POST";
	public static final String HTTPMETHOD_GET = "GET";
	public static final String HTTPMETHOD_JSON = "JSON";
	public static final String HTTPMETHOD_XML = "XML";
	private static final String BOUNDARY = getBoundry();
	private static final String MP_BOUNDARY = "--" + BOUNDARY;
	private static final String END_MP_BOUNDARY = "--" + BOUNDARY + "--";
	private static final String MULTIPART_FORM_DATA = "multipart/form-data";
	private static final int SET_CONNECTION_TIMEOUT = 6 * 1000;
	private static final int SET_SOCKET_TIMEOUT = 20 * 1000;

	/**
	 * 
	 * 请求网络
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            服务器地址
	 * @param method
	 *            "GET" or “POST” or "JSON"
	 * @param params
	 *            存放参数的容器
	 * @param files
	 *            文件路径,当有多个文件时，以#分隔
	 * @return 响应结果
	 * @throws HttpException
	 *             自定义异常
	 */
	public static InputStream OpenUrl(Context context, String url,
			String method, RequestParams params, String files)
			throws MyHttpException {
		LogUtil.i(TAG, "请求地址url == " + url);
		InputStream inputStream = null;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if (info == null) {
				throw new MyHttpException(
						context.getString(R.string.networkoff),
						MyHttpException.NETWORK_OFF_EXCEPTION);
			} else {
				HttpClient client = getNewHttpClient(context);
				HttpUriRequest request = null;
				ByteArrayOutputStream bos = null;
				if (method.equals(HTTPMETHOD_GET)) {
					url = url + "?" + HttpUtils.encodeUrl(params);
					HttpGet get = new HttpGet(url);
					request = get;
				} else if (method.equals(HTTPMETHOD_POST)) {
					HttpPost post = new HttpPost(url);
					request = post;
					byte[] data = null;
					String _contentType = params.getValue("content-type");

					bos = new ByteArrayOutputStream();

					if (!TextUtils.isEmpty(files)) {
						paramToUpload(bos, params);
						post.setHeader("Content-Type", MULTIPART_FORM_DATA
								+ "; boundary=" + BOUNDARY);
						HttpUtils.UploadImageUtils.revitionPostImageSize(files);
						String[] fileArray = files.split("#");
						for (int i = 0; i < fileArray.length; i++) {
							imageContentToUpload(bos, fileArray[i]);
						}
					} else {
						if (_contentType != null) {
							params.remove("content-type");
							post.setHeader("Content-Type", _contentType);
						} else {
							post.setHeader("Content-Type",
									"application/x-www-form-urlencoded");
						}

						String postParam = assembleJson(context, params)/*
																		 * HttpUtils
																		 * .
																		 * encodeParameters
																		 * (
																		 * params
																		 * )
																		 */;

						data = postParam.getBytes("UTF-8");
						bos.write(data);
					}

					data = bos.toByteArray();
					bos.close();
					ByteArrayEntity formEntity = new ByteArrayEntity(data);
					post.setEntity(formEntity);
				} else if (method.equals(HTTPMETHOD_JSON)) {
					HttpPost post = new HttpPost(url);
					request = post;
					byte[] data = null;
					String _contentType = params.getValue("content-type");

					bos = new ByteArrayOutputStream();
					LogUtil.d("com.njhn.weetmall", "files = " + files);
					if (!TextUtils.isEmpty(files)) {
						File file = new File(files); // DEBUG
						MultipartEntity mpEntity = new MultipartEntity();
						ContentBody cbFile = new FileBody(file, "image/jpeg");
						ContentBody cbMessage = new StringBody(assembleJson(
								context, params), Charset.forName("UTF-8"));

						mpEntity.addPart("file", cbFile);// 上传文件
						mpEntity.addPart("mdata", cbMessage);// 上传信息

						LogUtil.e("com.njhn.weetmall", "请求参数postParam = "
								+ assembleJson(context, params));
						post.setEntity(mpEntity);
					} else {
						if (_contentType != null) {
							params.remove("content-type");
							post.setHeader("Content-Type", _contentType);
						} else {
							post.setHeader("Content-Type",
									"application/x-www-form-urlencoded");
						}

						// RequestParams mParams = new RequestParams();
						// mParams.add("dataJson", assembleJson(context,
						// params));

						String postParam = assembleJson(context, params)/*
																		 * HttpUtils
																		 * .
																		 * encodeParameters
																		 * (
																		 * params
																		 * )
																		 */;
						LogUtil.e("com.njhn.weetmall", "请求参数postParam = "
								+ postParam);
						data = postParam.getBytes("UTF-8");
						bos.write(data);

						data = bos.toByteArray();
						bos.close();
						ByteArrayEntity formEntity = new ByteArrayEntity(data);
						post.setEntity(formEntity);
					}

				} else if (method.equals(HTTPMETHOD_XML)) {
					HttpPost post = new HttpPost(url);
					request = post;
					StringEntity s = new StringEntity(buildXmlString(params),
							HTTP.UTF_8);
					s.setContentType("text/xml charset=utf-8");
					post.setEntity(s);

				} else if (method.equals("DELETE")) {
					request = new HttpDelete(url);
				}

				HttpResponse response = client.execute(request);
				StatusLine status = response.getStatusLine();
				int statusCode = status.getStatusCode();
				if (statusCode == 200) {
					inputStream = getInputStream(response);
				} else {
					// throw new MyHttpException(result, statusCode);
				}
			}
		} catch (MyHttpException e) {
			e.printStackTrace();
			throw e;
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			throw new MyHttpException(
					context.getString(R.string.networktimeout),
					MyHttpException.HOST_ERROR_EXCEPTION);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			throw new MyHttpException(
					context.getString(R.string.networktimeout),
					MyHttpException.CONNECT_TIMEOUT_EXCEPTION);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw new MyHttpException(
					context.getString(R.string.networktimeout),
					MyHttpException.SOCKET_TIMEOUT_EXCEPTION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyHttpException(e);
		}
		return inputStream;
	}

	/**
	 * 
	 * 请求网络
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            服务器地址
	 * @param method
	 *            "GET" or “POST” or "JSON"
	 * @param params
	 *            存放参数的容器
	 * @param files
	 *            文件路径,当有多个文件时，以#分隔
	 * @return 响应结果
	 * @throws HttpException
	 *             自定义异常
	 */
	public static InputStream OpenUrl(Context context, String action,
			JSONObject params, String files) throws MyHttpException {
		InputStream inputStream = null;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if (info == null) {
				throw new MyHttpException(
						context.getString(R.string.networkoff),
						MyHttpException.NETWORK_OFF_EXCEPTION);
			} else {
				HttpClient client = getNewHttpClient(context);
				HttpPost post = new HttpPost(HttpUrlManager.HTTP_URL);

				if (!TextUtils.isEmpty(files)) {
					// File file = new File(files); // DEBUG
					// MultipartEntity mpEntity = new MultipartEntity();
					// ContentBody cbFile = new FileBody(file, "image/jpeg");
					// ContentBody cbMessage = new StringBody(
					// jsonObject.toString(), Charset.forName("UTF-8"));
					//
					// mpEntity.addPart("file", cbFile);// 上传文件
					// mpEntity.addPart("mdata", cbMessage);// 上传信息
					// post.setEntity(mpEntity);
				} else {
					post.setHeader("SOAPAction",
							"http://tempuri.org/IService1/" + action);
					post.setHeader("Content-Type", "text/xml");

					String xml = "<?xml version= '1.0' encoding='gb2312'?><weighData><weighTime>2012-12-2 12:23:12</weighTime><cardNum>2</cardNum><cfid>123</cfid></weighData>";

					String parseContentHeader1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><SOAP-ENV:Envelope xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">";

					String parseContentHeader2 = "<SOAP-ENV:Body><" + action
							+ " xmlns=\"http://tempuri.org/\">";

					JSONObject requestParams = new JSONObject();

					org.json.JSONObject jo = new org.json.JSONObject();

					try {
						jo.put("Version", "1");
						jo.put("MachineType", "0");
						jo.put("UserCode", "awc");
						jo.put("Password", "123456");
					} catch (JSONException e) {
						e.printStackTrace();
					}

					try {
						requestParams.put("Head", jo);
						requestParams.put("Body", params);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					String Injson = "<inJson>" + requestParams + "</inJson>";

					String parseContentEnd = "</" + action
							+ "></SOAP-ENV:Body></SOAP-ENV:Envelope>";

					String combine = new StringBuilder()
							.append(parseContentHeader1)
							.append(parseContentHeader2).append(Injson)
							.append(parseContentEnd).toString().trim();
					
					LogUtil.e("com.company.ilunch", "请求参数postParam = "
							+ combine);
					
					StringEntity se = new StringEntity(combine, HTTP.UTF_8);
					post.setEntity(se);
				}

				HttpResponse response = client.execute(post);
				StatusLine status = response.getStatusLine();
				int statusCode = status.getStatusCode();
				if (statusCode == 200) {
					inputStream = getInputStream(response);
				} else {
					// throw new MyHttpException(result, statusCode);
				}
			}
		} catch (MyHttpException e) {
			e.printStackTrace();
			throw e;
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			throw new MyHttpException(
					context.getString(R.string.networktimeout),
					MyHttpException.HOST_ERROR_EXCEPTION);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			throw new MyHttpException(
					context.getString(R.string.networktimeout),
					MyHttpException.CONNECT_TIMEOUT_EXCEPTION);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw new MyHttpException(
					context.getString(R.string.networktimeout),
					MyHttpException.SOCKET_TIMEOUT_EXCEPTION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyHttpException(e);
		}
		return inputStream;
	}

	private static HttpClient getNewHttpClient(Context context) {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			HttpConnectionParams.setConnectionTimeout(params,
					SET_CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);
			HttpClient client = new DefaultHttpClient(ccm, params);

			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			if (!wifiManager.isWifiEnabled()) {
				// 获取当前正在使用的APN接入点
				Uri uri = Uri.parse("content://telephony/carriers/preferapn");
				Cursor mCursor = context.getContentResolver().query(uri, null,
						null, null, null);
				if (mCursor != null && mCursor.moveToFirst()) {
					// 游标移至第一条记录，当然也只有一条
					String proxyStr = mCursor.getString(mCursor
							.getColumnIndex("proxy"));
					if (proxyStr != null && proxyStr.trim().length() > 0) {
						HttpHost proxy = new HttpHost(proxyStr, 80);
						client.getParams().setParameter(
								ConnRoutePNames.DEFAULT_PROXY, proxy);
					}
					mCursor.close();
				}
			}

			return client;
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	/**
	 * 产生11位的boundary
	 * 
	 * @return String 11位的boundary
	 */
	static String getBoundry() {
		StringBuffer _sb = new StringBuffer();
		for (int t = 1; t < 12; t++) {
			long time = System.currentTimeMillis() + t;
			if (time % 3 == 0) {
				_sb.append((char) time % 9);
			} else if (time % 3 == 1) {
				_sb.append((char) (65 + time % 26));
			} else {
				_sb.append((char) (97 + time % 26));
			}
		}
		return _sb.toString();
	}

	private static void paramToUpload(OutputStream baos, RequestParams params)
			throws MyHttpException {
		String key = "";
		for (int loc = 0; loc < params.size(); loc++) {
			key = params.getKey(loc);
			StringBuilder temp = new StringBuilder(10);
			temp.setLength(0);
			temp.append(MP_BOUNDARY).append("\r\n");
			temp.append("content-disposition: form-data; name=\"").append(key)
					.append("\"\r\n\r\n");
			temp.append(params.getValue(key)).append("\r\n");
			byte[] res = temp.toString().getBytes();
			try {
				baos.write(res);
			} catch (IOException e) {
				throw new MyHttpException(e);
			}
		}
	}

	private static void imageContentToUpload(OutputStream out, String imgpath)
			throws MyHttpException {
		if (imgpath == null) {
			return;
		}
		StringBuilder temp = new StringBuilder();

		temp.append(MP_BOUNDARY).append("\r\n");
		temp.append(
				"Content-Disposition: form-data; name=\"fileData\"; filename=\"")
				.append("news_image").append("\"\r\n");
		String filetype = "image/jpg";
		temp.append("Content-Type: ").append(filetype).append("\r\n\r\n");
		byte[] res = temp.toString().getBytes();
		FileInputStream input = null;
		try {
			out.write(res);
			input = new FileInputStream(imgpath);
			byte[] buffer = new byte[1024 * 50];
			while (true) {
				int count = input.read(buffer);
				if (count == -1) {
					break;
				}
				out.write(buffer, 0, count);
			}
			out.write("\r\n".getBytes());
			out.write(("\r\n" + END_MP_BOUNDARY).getBytes());
		} catch (IOException e) {
			throw new MyHttpException(e);
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					throw new MyHttpException(e);
				}
			}
		}
	}

	/**
	 * 
	 * 组装JSON串
	 * 
	 * @throws JSONException
	 */
	private static String assembleJson(Context context, RequestParams params)
			throws JSONException {

		// 添加每个请求的head
		// JSONObject head = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("system", "Android");
		body.put("api_version", "1.0");
		body.put(
				"app_version",
				AndroidUtils.getPackageInfo(context, context.getPackageName()).versionName);
		body.put("device_name", android.os.Build.MODEL);
		body.put("system_version", android.os.Build.VERSION.RELEASE);

		if (params == null) {
			return null;
		}
		for (int i = 0; i < params.size(); i++) {
			body.put(params.getKey(i), params.getValue(i));
		}

		// body.put("head", head);
		// body.put("mac", "");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mdata", new JSONObject().put("body", body));
		return jsonObject.toString();
	}

	private static class MySSLSocketFactory extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	// 获取Http返回的流
	private static InputStream getInputStream(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		InputStream inputStream = null;
		try {
			if (entity == null) {
				return inputStream;
			}
			inputStream = entity.getContent();
			Header header = response.getFirstHeader("Content-Encoding");
			if (header != null
					&& header.getValue().toLowerCase().indexOf("gzip") > -1) {
				inputStream = new GZIPInputStream(inputStream);
			}
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}

		return inputStream;
	}

	// 拼接xml字符窜
	private static String buildXmlString(RequestParams params) {
		StringWriter stringWriter = new StringWriter();
		try {
			// 获取XmlSerializer对象
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlSerializer serializer = factory.newSerializer();
			// 设置输出流对象
			serializer.setOutput(stringWriter);
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "request");

			for (int i = 0; i < params.size(); i++) {
				serializer.startTag(null, params.getKey(i));
				serializer.text(params.getValue(i));
				serializer.endTag(null, params.getKey(i));
			}

			serializer.endTag(null, "request");
			serializer.endDocument();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		Log.d("x", "stringWriter.toString() = " + stringWriter.toString());
		return stringWriter.toString();
	}
}