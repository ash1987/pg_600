package com.company.pg;

import java.io.File;
import java.io.IOException;

import com.company.pg.bean.ControlDevice;
import com.company.pg.utils.AssertsUtils;
import com.company.pg.utils.FileUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xtremeprog.xpgconnect.XPGWifiSDK;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * 
 * 描述：应用程序类
 */
public class MyApplication extends Application {
	
	/** The control device. */
	public static ControlDevice controlDevice = null;
	
	@Override
	public void onCreate() {
		try {
			//复制assert文件夹中的json文件到设备安装目录。json文件是解析数据点必备的文件，sdk根据该文件，把二进制数据转换为json字段并返回。

			AssertsUtils.copyAllAssertToCacheFolder(this.getApplicationContext());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 启动SDK
		XPGWifiSDK.sharedInstance().startWithAppID(getApplicationContext(), "673d8dda9a6143848bdca0c1294667ed");
//				"6f3074fe43894547a4f1314bd7e3ae0b");
		// 设定日志打印级别
		XPGWifiSDK.sharedInstance().setLogLevel(XPGWifiSDK.XPGWifiLogLevel.XPGWifiLogLevelAll, "GoKitDemo.log", true);

		initImageLoader(this);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		ILog.LogI(MyApplication.class, "onTerminate---------------------------------------------");
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		ILog.LogI(MyApplication.class, "onLowMemory---------------------------------------------");
	}
	
	/**
	 * 初始化ImageLoader <br/>
	 * 
	 * @param [参数1]-[参数1说明] <br/>
	 * @param [参数2]-[参数2说明] <br/>
	 */
	public static void initImageLoader(Context context) {
		// 获取本地缓存的目录，该目录在SDCard的根目录下
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				FileUtils.getImagePath());

		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				context);
		// 设置线程数量
		// 设定线程等级比普通低一点
		builder.threadPriority(Thread.NORM_PRIORITY - 1);
		// 设定内存缓存为弱缓存
		// builder.memoryCache(new WeakMemoryCache());
		// 设定内存图片缓存大小限制，不设置默认为屏幕的宽高
		// builder.memoryCacheExtraOptions(480, 800);
		// 缓存到内存的最大数据
		// builder.memoryCacheSize(8 * 1024 * 1024);
		// 缓存到文件的最大数据
		builder.discCacheSize(50 * 1024 * 1024);
		// 文件数量
		builder.discCacheFileCount(1000);
		// 设定只保存同一尺寸的图片在内存
		builder.denyCacheImageMultipleSizesInMemory();
		// 设定缓存的SDcard目录，UnlimitDiscCache速度最快
		builder.discCache(new UnlimitedDiscCache(cacheDir));
		// 设定缓存到SDCard目录的文件命名
		builder.discCacheFileNameGenerator(new HashCodeFileNameGenerator());
		// 设定网络连接超时 timeout: 10s 读取网络连接超时read timeout: 60s
		builder.imageDownloader(new BaseImageDownloader(context, 8000, 20000));
		// 设置ImageLoader的配置参数
		builder.defaultDisplayImageOptions(initDisplayOptions(true,R.drawable.img_loading));

		// 初始化ImageLoader
		ImageLoader.getInstance().init(builder.build());
	}
	
	/**
	 * 返回默认的参数配置
	 * 
	 * @param isDefaultShow
	 *            true：显示默认的加载图片 false：不显示默认的加载图片
	 * @return
	 */
	public static DisplayImageOptions initDisplayOptions(boolean isShowDefault,int defaultIcon) {
		DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
		// 设置图片缩放方式
		// EXACTLY: 图像将完全按比例缩小的目标大小
		// EXACTLY_STRETCHED: 图片会缩放到目标大小
		// IN_SAMPLE_INT: 图像将被二次采样的整数倍
		// IN_SAMPLE_POWER_OF_2: 图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
		// NONE: 图片不会调整
		displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
		if (isShowDefault) {
			// 默认显示的图片
			displayImageOptionsBuilder.showImageOnLoading(defaultIcon);
			// 地址为空的默认显示图片
			displayImageOptionsBuilder
					.showImageForEmptyUri(defaultIcon);
			// 加载失败的显示图片
			displayImageOptionsBuilder.showImageOnFail(defaultIcon);
		}
		// 开启内存缓存
		displayImageOptionsBuilder.cacheInMemory(true);
		displayImageOptionsBuilder.postProcessor(new BitmapProcessor() {
			
			@Override
			public Bitmap process(Bitmap bitmap) {
				return bitmap;
			}
		});
		// 开启SDCard缓存
		displayImageOptionsBuilder.cacheOnDisc(true);
		// 设置图片的编码格式为RGB_565，此格式比ARGB_8888快
		displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
		displayImageOptionsBuilder.considerExifParams(true);
		// 设置圆角，不需要请删除
		// displayImageOptionsBuilder.displayer(new RoundedBitmapDisplayer(5));

		return displayImageOptionsBuilder.build();
	}
}
