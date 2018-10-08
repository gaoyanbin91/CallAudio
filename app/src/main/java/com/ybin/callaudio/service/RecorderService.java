package com.ybin.callaudio.service;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.ybin.callaudio.api.ApiService;
import com.ybin.callaudio.utils.LogUtils;
import com.ybin.callaudio.utils.NetWorkUtils;
import com.ybin.callaudio.utils.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Yanbin on 2018/9/26.
 * 描述:
 */
public class RecorderService extends AccessibilityService {
    private static final String TAG = "RecorderService";
    private static final String TAG1 = "手机通话状态";
    /**
     * 音频录制
     */
    private MediaRecorder recorder;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("application/octet-stream");
    /**
     * 监听拨号广播，以便获取用户拨出的电话号码
     */
    private OutCallReceiver outCallReceiver;
    private IntentFilter intentFilter;
    /**
     * 网络状态改变广播，当网络畅通的状态下，把用户未上传的录音文件都上传掉
     */
    private NetworkConnectChangedReceiver networkConnectChangedReceiver;
    private IntentFilter intentFilter2;
    /**
     * 当前通话对象的电话号码
     */
    private String currentCallNum = "";
    /**
     * 区分来电和去电
     */
    private int previousStats = 0;
    /**
     * 当前正在录制的文件
     */
    private String currentFile = "";
    /**
     * 保存未上传的录音文件
     */
    private SharedPreferences unUploadFile;
    private String dirPath = "";
    private boolean isRecording = false;

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "onServiceConnected");
        Toast.makeText(getApplicationContext(), "自动录音服务已启动", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // TODO Auto-generated method stub
        Log.i(TAG, "eventType " + event.getEventType());
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onServiceConnected");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // 监听电话状态
        tm.listen(new MyListener(), PhoneStateListener.LISTEN_CALL_STATE);
        outCallReceiver = new OutCallReceiver();
        intentFilter = new IntentFilter();
        //设置拨号广播过滤
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(outCallReceiver, intentFilter);
        //注册拨号广播接收器
        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        intentFilter2 = new IntentFilter();
        //设置网络状态改变广播过滤
        intentFilter2.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter2.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter2.addAction("android.net.wifi.STATE_CHANGE");
        //注册网络状态改变广播接收器
        registerReceiver(networkConnectChangedReceiver, intentFilter2);
        unUploadFile = getSharedPreferences("un_upload_file", 0);
        unUploadFile.edit().putString("description", "未上传的录音文件存放路径").commit();
        dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.ct.phonerecorder/";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "进程被关闭，无法继续录音，请打开录音服务", Toast.LENGTH_LONG).show();
        if (outCallReceiver != null) {
            unregisterReceiver(outCallReceiver);
        }
        if (networkConnectChangedReceiver != null) {
            unregisterReceiver(networkConnectChangedReceiver);
        }
    }

    class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            Log.d(TAG1, "空闲状态" + incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG1, "空闲");
                    Toast.makeText(RecorderService.this, "通话空闲中", Toast.LENGTH_SHORT).show();
                    if (recorder != null && isRecording) {
                        recorder.stop();// 停止录音
                        recorder.release();
                        recorder = null;
                        Log.d("电话", "通话结束，停止录音");

                        Toast.makeText(RecorderService.this, "通过结束。", Toast.LENGTH_SHORT).show();
                        uploadFile(currentFile);
                    }
                    isRecording = false;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG1, "来电响铃" + incomingNumber);
                    // 进行初始化
                    Toast.makeText(RecorderService.this, "来电响铃", Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG1, "摘机" + (!incomingNumber.equals("") ? incomingNumber : currentCallNum));
                    initRecord(!incomingNumber.equals("") ? incomingNumber : currentCallNum);

                    if (recorder != null) {
                        // 开始录音
                        Toast.makeText(RecorderService.this, "开始录音", Toast.LENGTH_SHORT).show();
                        recorder.start();
                        isRecording = true;
                    }
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }

    }

    /**
     * 当录音结束后，自动上传录音文件。
     * ①网络可用：直接上传；
     * ②网络不可用：保存文件路径，待网络可用的时候再进行上传；
     * ③上传失败的文件，也保存文件路径，或者重新上传。
     */
    public void uploadFile(String file) {
        ZipUtils.zipFile(dirPath + file, dirPath + file + ".zip");
        if (NetWorkUtils.isNetworkConnected(getApplicationContext())) {
            //上传文件
//            OkHttpUtils.postFile()
            upload( dirPath+ file  );
//            upload(dirPath+ file + ".zip");
//            uploadUnUploadedFiles();
        } else {
            saveUnUploadFIles(  dirPath+file  );
//           、、 saveUnUploadFIles(dirPath + file + ".zip");
        }
    }

    /**
     * 保存未上传的录音文件
     *
     * @param file 未上传的录音文件路径
     */
    private void saveUnUploadFIles(String file) {
        String files = unUploadFile.getString("unUploadFile", "");
        if (files.equals("")) {
            files = file;
        } else {
            StringBuilder sb = new StringBuilder(files);
            files = sb.append(";").append(file).toString();
        }
        unUploadFile.edit().putString("unUploadFile", files).commit();
    }

    /**
     * 上传因为网络或者其他原因，暂未上传或者上传失败的文件，重新上传
     */
    public void uploadUnUploadedFiles() {
        //获取当前还未上传的文件，并把这些文件上传
        String files = unUploadFile.getString("unUploadFile", "");
        unUploadFile.edit().putString("unUploadFile", "").commit();
        if (files.equals("")) {
            return;
        }
        String[] fileArry = files.split(";");
        int len = fileArry.length;
        for (String file : fileArry) {
            upload(file);
        }
    }

    /**
     * 文件上传
     *
     * @param file 要上传的文件
     */
    public void upload(final String file) {
        File file1 = new File(file);
        Toast.makeText(this, "录音文件上传", Toast.LENGTH_SHORT).show();
        if (file1 == null || !file1.exists()) {
            //文件不存在
            Toast.makeText(this, "文件路径错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!NetWorkUtils.isNetworkConnected(getApplicationContext())) {
            saveUnUploadFIles(file);
            return;
        }


        uploadZip(ApiService.UPLOAD_MP3_SHISHI,file1);
//        OkHttpUtils.post()//
//                .addFile("mFile", file1.getName(), file1)//
//                .url(url)//
//                .params(map).build()//
//                .execute(new StringCallback() {
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Log.e(TAG, "成功 response=" + response);
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        Log.e(TAG, "失败 response=" + e.toString());
//                        saveUnUploadFIles(file);
//                    }
//                });
    }

    /**
     * 提交数据
     * @param reqUrl
     */
    private void uploadZip(String reqUrl , File files) {

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);

        if (files != null) {
            multipartBodyBuilder.addFormDataPart("file", files.getName(), RequestBody.create(MEDIA_TYPE_PNG, files));
        }

        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder RequestBuilder = new Request.Builder();
        // 添加URL地址
        RequestBuilder.url(reqUrl);
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        OkHttpClient mOkHttpClient = new OkHttpClient();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(RecorderService.this, "录音上传失败！", Toast.LENGTH_SHORT).show();
//                ToastUtils.showShort("提交失败");
            }

            @Override
            public void onResponse(Call call, Response response) {
                LogUtils.i("shihi上传", response);

                if (response.code() == 200) {
                    new Thread() {
                        public void run() {
                            Looper.prepare();
                            Toast.makeText(RecorderService.this, "录音上传成功！", Toast.LENGTH_SHORT).show();
//                            ToastUtils.showShort("提交成功");
                            Looper.loop();// 进入loop中的循环，查看消息队列
                        }

                    }.start();

                }
            }
        });
    }
    /**
     * 初始化录音机，并给录音文件重命名
     * @param incomingNumber 通话号码
     */
    private void initRecord(String incomingNumber) {
        previousStats = TelephonyManager.CALL_STATE_RINGING;
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// Microphone
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);// 设置输出3gp格式
        File out = new File(dirPath);
        if (!out.exists()) {
            out.mkdirs();
        }
        recorder.setOutputFile(dirPath
                + getFileName((previousStats == TelephonyManager.CALL_STATE_RINGING ? incomingNumber : currentCallNum))
        );
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 设置音频编码格式
        try {
            recorder.prepare();// 做好准备
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取录音文件的名称
     *
     * @param incomingNumber 通话号码
     * @return 获取录音文件的名称
     */
    private String getFileName(String incomingNumber) {
        Date date = new Date(System.currentTimeMillis());
        currentFile = incomingNumber + " " + dateFormat.format(date) + ".mp3";
        return currentFile;
    }

    /**
     * 拨号广播接收器，并获取拨号号码
     */
    public class OutCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG1, "当前手机拨打了电话：" + currentCallNum);
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                currentCallNum = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.d(TAG1, "当前手机拨打了电话：" + currentCallNum);
            } else {
                Log.d(TAG1, "有电话，快接听电话");
            }
        }
    }

    /**
     * 网络状态change广播接收器
     */
    public class NetworkConnectChangedReceiver extends BroadcastReceiver {
        private static final String TAG = "network status";

        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
             * 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
             * 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
             */
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                ConnectivityManager manager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                Log.i(TAG, "CONNECTIVITY_ACTION");

                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.isConnected()) {
                        //当前网络可用
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                            // connected to wifi
                            Log.e(TAG, "当前WiFi连接可用 ");
                        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                            // connected to the mobile provider's data plan
                            Log.e(TAG, "当前移动网络连接可用 ");
                        }
                        uploadUnUploadedFiles();
                    } else {
                        Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                    }

                } else {   // not connected to the internet
                    Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                }
            }
        }
    }

}
