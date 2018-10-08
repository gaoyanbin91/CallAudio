/**
 *
 */
package com.ybin.callaudio.utils;

import android.os.Environment;
import android.util.Log;

import com.ybin.callaudio.comm.Global;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {

    public static int V = 5;
    public static int D = 4;
    public static int I = 3;
    public static int W = 2;
    public static int E = 1;

    private static final String DEF_FILEPATH = "/sdcard/zhengfangji.txt";

    private static String TAG = "zhengfangji";

    public static void v(String tag, Object msg){
        if(Global.LOGTYPE >= V){
            writeFileToSDLog(tag,msg);
            Log.v(tag, String.valueOf(msg));
        }
    }

    public static void d(String tag, Object msg){
        if(Global.LOGTYPE >= D){
            writeFileToSDLog(tag,msg);
            Log.d(tag, String.valueOf(msg));
        }
    }

    public static void i(String tag, Object msg){
        if(Global.LOGTYPE >= I){
            writeFileToSDLog(tag,msg);
            Log.i(tag, String.valueOf(msg));
        }
    }

    public static void w(String tag, Object msg){
        if(Global.LOGTYPE >= W){
            writeFileToSDLog(tag,msg);
            Log.w(tag, String.valueOf(msg));
        }
    }

    public static void e(String tag, Object msg){
        if(Global.LOGTYPE >= E){
            writeFileToSDLog(tag,msg);
            Log.e(tag, String.valueOf(msg));

        }
    }

    public static void e(String tag, Throwable e) {
        String msg = getTrace(e);
        if (Global.LOGTYPE >= E) {
            if (null != e) {
                Log.e(tag, msg);
                e.printStackTrace();
                writeFileToSDLog("aiderror"+tag, msg);
            }
        }


    }

    public static void info(String msg){
        e(TAG,msg);
    }



    public static void writeFileToSDLog(String key, Object value) {
        try{
            if (!isHasSDcard())
                return;

            if (1 == Global.LOGTOFILE) {
                File file = new File(DEF_FILEPATH);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileWriter fw = null;
                BufferedWriter bw = null;
                try {
                    fw = new FileWriter(DEF_FILEPATH, true);
                    bw = new BufferedWriter(fw);
                    bw.newLine();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String time = format.format(new Date());
                    bw.write(time + "##" + key + "####" + value);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (null != bw) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != fw) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }catch(Throwable e){
            e(TAG,e);
        }

    }

    public static String getTrace(Throwable t) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        Throwable cause = t.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        return result;
    }

    public static boolean isHasSDcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }



}
