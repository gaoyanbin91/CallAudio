package com.ybin.callaudio.comm;

import android.net.Uri;
import android.os.Environment;


/**
 * Created by siberiawolf on 15/10/23.
 */
public class Global {
    public static String VERSION = "1.0.7";
    public static String IMIE;
    public static int LOGTYPE = 5;
    public static int LOGTOFILE = 0;
    public static Uri uritempFile;
    public static int mScreenWidth;
    public static int mScreenHeight;
    //全局文件保存名称
    public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String SDCARD_IMG_ROOT = SDCARD_ROOT + "/newequip/img";

    public static String openImageUrl = "";


    public static String openUDID;

    public static int totalStep = 0;

    //    public static RSAPublicKey RSA_PUBLIC_KEY;
    //
//    public static Key DES_KEY;

    public static boolean isProceed;

    public static boolean hasShow;

    public static String tokenId;


}
