package com.company.pg.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 
 * 描述：系统工具类
 */
public class AndroidUtils {
	/* 获取屏幕的像素密度 */
	public static float getDeviceDisplayDensity(Context cx) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		return dm.density;
	}

	/* 获取屏幕的像素密度 */
	public static float getDeviceScaledDensity(Context cx) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		return dm.scaledDensity;
	}

	/* 获取屏幕的宽度 */
	public static float getDeviceWidth(Context cx) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/* 获取屏幕的高度 */
	public static float getDeviceHight(Context cx) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
	
	

	// 判断网络是否可用
	public static boolean isNetworkAvailable(Context mContext) {
		boolean isAvailable = false;
		final ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != cm) {
			final NetworkInfo[] netinfo = cm.getAllNetworkInfo();
			if (null != netinfo) {
				for (int i = 0; i < netinfo.length; i++) {
					if (netinfo[i].isConnected()) {
						isAvailable = true;
					}
				}
			}
		}
		return isAvailable;
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context cx, float spValue) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		return (int) (spValue * dm.scaledDensity + 0.5f);
	}

	/**
	 * 
	 * 〈功能简述〉
	 * 
	 * @param [参数1] [参数1说明]
	 * @param [参数2] [参数2说明]
	 * @return [返回类型说明]
	 * @exception/throws [违例类型] [违例说明]
	 * 
	 */
	public static int dip2px(Context cx, float spValue) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		return (int) (spValue * dm.density + 0.5f);
	}

	// ScrollView中嵌套Gridview， 代码中的columns，代表gridview 有columns列
	public static void setGridViewHeightBasedOnChildren(GridView gridView,
			int columns, int vSpace, int padding) {
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		int hang = 0;
		if ((gridView.getCount() % columns) == 0) {

			hang = gridView.getCount() / columns;
		} else {
			hang = gridView.getCount() / columns + 1;
		}
		for (int i = 0; i < hang; i++) {
			View listItem = listAdapter.getView(i, null, gridView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		if (hang > 1) {
			totalHeight += vSpace * (hang - 1);
		}

		totalHeight += padding;
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;
		gridView.setLayoutParams(params);
	}

	// 在scrollView中嵌套ListView
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	// 判断是否为邮箱地址
	public static boolean isEmail(String strEmail) {
		boolean isExist = false;
		Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
		Matcher m = p.matcher(strEmail);
		boolean b = m.matches();
		if (b) {
			isExist = true;
		}
		return isExist;
	}

	/**
	 * 检测手机号码是否合法 正则判断
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[^4,\\D]))\\d{8}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	/**
	 * 
	 * 获取安装包信息
	 * 
	 * @param context
	 *            上下文
	 * @param packageName
	 *            包名
	 */
	public static PackageInfo getPackageInfo(Context context, String packageName) {
		List<PackageInfo> installedPackageList = context.getPackageManager()
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		if (installedPackageList == null) {
			return null;
		}

		for (PackageInfo packageInfo : installedPackageList) {
			if ((null != packageInfo) && (null != packageInfo.packageName)
					&& packageInfo.packageName.equals(packageName)) {
				return packageInfo;
			}
		}

		return null;
	}

	/**
	 * 根据手机分辨率密度判断所需图片的类型
	 * 
	 * @param [参数1]-[参数1说明] <br/>
	 * @param [参数2]-[参数2说明] <br/>
	 */
	public static String getDencityType(Context context) {
		String[] types = { "M", "H", "LH", "XLH" };
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();

		if (dm.heightPixels == 1920 && dm.widthPixels == 1080) {
			return types[3];
		}

		switch (dm.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
		case DisplayMetrics.DENSITY_MEDIUM:
			return types[0];
		case DisplayMetrics.DENSITY_HIGH:
			return types[1];
		case DisplayMetrics.DENSITY_XHIGH:
		case DisplayMetrics.DENSITY_TV:
			return types[2];
		default:
			return types[1];
		}
	}

	/**
	 * 将字符串转换为时间戳
	 */
	public static long getStringToDate(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date.getTime();
	}
	
	/**
	 * 
	 * 将时间戳转换为字符串
	 */
	public static String getDateToString(String time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		if (time == null) {
			return null;
		}
		
		long mTime = Long.valueOf(time);
		return sdf.format(new Date(mTime));
	}
	
	/**
	 * 时间戳转日期时间
	 * 
	 * @param unixDate
	 * @return
	 */
	public static String getDateStr(String timestamp) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(timestamp);
		re_StrTime = sdf.format(new Date(lcc_time));
		return re_StrTime;
	}
	
	/**
	 * 
	 * 将Drawable转换为Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		return AndroidUtils.getPackageInfo(context, context.getPackageName()).versionCode;
	}
	
	/**
	 * 获取软件版本名
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		return AndroidUtils.getPackageInfo(context, context.getPackageName()).versionName;
	}
}
