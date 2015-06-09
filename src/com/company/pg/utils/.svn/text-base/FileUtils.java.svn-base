package com.company.pg.utils;

import java.io.File;

import android.os.Environment;

/**
 * 描述：SD卡文件管理类
 */

public class FileUtils {
	// SD卡文件根目录名称
	public final static String ROOT_DIRECTORY = "Pg_600";
	// 图片文件夹名称
	public final static String IMAGECACHE = "imagecache";
	private static java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");

	// 图片的路径
	public static String getImagePath() {
		return File.separator + ROOT_DIRECTORY + File.separator + IMAGECACHE;
	}

	// 获取缓存文件夹的大小
	public static String getImageCacheSize() {
		String sizeString = "0M";
		long dirSize = 0;
		String path = Environment.getExternalStorageDirectory()
				+ File.separator + ROOT_DIRECTORY + File.separator + IMAGECACHE;
		File dir = new File(path);
		if (dir.exists()) {
			if (!dir.isDirectory()) {
				return sizeString;
			}
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					dirSize += file.length();
				} else if (file.isDirectory()) {
					dirSize += file.length();
				}
			}
		}
		return df.format(dirSize/1024f/1024f) + "M";
	}
}
