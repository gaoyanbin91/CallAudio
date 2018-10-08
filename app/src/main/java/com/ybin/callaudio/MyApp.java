package com.ybin.callaudio;

import android.app.Activity;
import android.app.Service;
import android.os.Handler;
import android.os.Vibrator;

import com.ybin.callaudio.service.LocationService;
import com.ybin.callaudio.utils.LogUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by gaoyanbin on 2018/1/24.
 * 描述:全局application
 */

public class MyApp extends BaseApp {
    private static final String TAG = MyApp.class.getSimpleName();
    public Vibrator mVibrator;
    public LocationService locationService;
    private static MyApp _context;
    public static MyApp mInstance = null;
    public static HashMap<String, Activity> activityHashMap;
    public Handler handler;
    public static String VIDEO_PATH = "/sdcard/zhengfangji/";
    @Override
    public void onCreate() {
        LogUtils.d( "","[ExampleApplication] onCreate");
        super.onCreate();
        _context = this;
        mInstance = this;

        handler = new Handler();

        VIDEO_PATH += String.valueOf(System.currentTimeMillis());
        File file = new File(VIDEO_PATH);
        if (!file.exists()) file.mkdirs();

        //**************************************相关第三方SDK的初始化等操作*************************************************
      /*  NeverCrash.init(new NeverCrash.CrashHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                //EventBus.getDefault().post(new Event(e, StaticData.type8));//错误信息
                System.out.println("crash problem" + Log.getStackTraceString(e));
            }
        });*/

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
       // SDKInitializer.initialize(getApplicationContext());
    }



    public static MyApp getInstance() {
        return _context;
    }
    public HashMap<String, Activity> getActivityHashMap() {
        if (activityHashMap == null) {
            activityHashMap = new HashMap<>();
        }
        return activityHashMap;
    }
}
