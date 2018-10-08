package com.ybin.callaudio.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.ybin.callaudio.MyApp;

import java.io.File;
import java.io.FileOutputStream;

;
;

/**
 * 维护 所有缓存的路径
 * 
 * @author wxj
 * 
 */
public class FileUtil {
	/** 缓存数据的目录 */
	private static final String CACHE = "cache";
	private static final String ERROR = "cache"+ File.separator+".error";
	/**缓存图片的目录*/
	private static final String ICON = "icon";
	private static final String ICON_CLIP = "icon"+ File.separator+"clip";
	/**下载目录**/
	private static final String Down = "down";
	/** 缓存路径的根目录**/
	private static final String ROOT = "freightcar";

	public static File getDir(String dir) {
		StringBuilder path = new StringBuilder();
		// sd卡可用
		if (isSDAvailable()) {
			path.append(Environment.getExternalStorageDirectory()
					.getAbsolutePath());// /mnt/sdcard
			// linux /
			// windows \
			path.append(File.separator); // /mnt/sdcard/
			path.append(ROOT);// /mnt/sdcard/googleplayz12
			path.append(File.separator);
			path.append(dir);// /mnt/sdcard/googleplayz12/cache
		} else {
				//  /data/data/包名/cache/cache
			path.append(MyApp.getInstance().getCacheDir().getAbsolutePath());///data/data/包名/cache
			path.append(File.separator);
			path.append(dir); //  /data/data/包名/cache/cache
		}
		File file = new File(path.toString());
		if (!file.exists() || !file.isDirectory()) {
			// 创建文件夹
			file.mkdirs();
		}
		return file;
	}

	/*private static boolean isSDAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}*/
	/**
	 * SD卡是否可用.
	 */
	public static boolean isSDAvailable() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File sd = new File(Environment.getExternalStorageDirectory().getPath());
			return sd.canWrite();
		} else {
			return false;
		}
	}

	/**
	 * 得到SD卡根目录.
	 */
	public static File getRootPath() {
		File path = null;
		if (FileUtil.isSDAvailable()) {
			path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		} else {
			path = Environment.getDataDirectory();
		}
		return path;
	}
	/**
	 * 得到SD卡根目录.
	 */
	public static File getRootPath1(Context context) {
		if (FileUtil.sdCardIsAvailable()) {
			return Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		} else {
			return context.getFilesDir();
		}
	}
	/**
	 * SD卡是否可用.
	 */
	public static boolean sdCardIsAvailable() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File sd = new File(Environment.getExternalStorageDirectory().getPath());
			return sd.canWrite();
		} else {
			return false;
		}
	}
	/**
	 * 文件或者文件夹是否存在.
	 */
	public static boolean fileExists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	public static void deleleFile(String path) {
		deleteFile(new File(path));
	}

	public static void deleteFile(File file) {
		if (file != null && file.exists()){
			file.delete();}
	}
	/**
	 * 删除指定文件夹下所有文件, 保留文件夹.
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (file.isFile()) {
			file.delete();
			return true;
		}
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File exeFile = files[i];
			if (exeFile.isDirectory()) {
				delAllFile(exeFile.getAbsolutePath());
			} else {
				exeFile.delete();
			}
		}
		return flag;
	}
	// 是否存在sd卡
	public static boolean isHasSDcard(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/** 获取数据缓存的目录 */
	public static File getCache() {
		return getDir(CACHE);
	}
	public static File getCacheError() {
		return getDir(ERROR);
	}
	/*** 缓存图标位置**/
	public static File getIcon(){
		return getDir(ICON);//  /mnt/sdcard/ylb/icon
	}
	public static File getIconClip() {
		return getDir(ICON_CLIP);
	}
	/**下载文件位置**/
	public static File getDown(){
		return getDir(Down);//  /mnt/sdcard/googleplayz12/icon
	}
	// 将图片保存到SD卡中
	public static void saveSDcardImage(Bitmap mBitmap, String path, String filename){
		//LogUtils.info("将图片保存到SD卡中===>filename===>"+filename);
		try{
			if(!isHasSDcard()){
				return ;}
			///如果图片路径没有创建，则创建
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			}

			if(filename == null){
				return ;}

			//LogUtils.info("将图片保存到SD卡中===>path===>"+path);
			File imageFile = new File(file,filename+".png");
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
			fos.flush();
			fos.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
