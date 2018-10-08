package com.ybin.callaudio.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.ybin.callaudio.MyApp;
import com.ybin.callaudio.R;
import com.ybin.callaudio.activity.base.BaseActivity;
import com.ybin.callaudio.adapter.ImageAdapter;
import com.ybin.callaudio.api.ApiService;
import com.ybin.callaudio.comm.Global;
import com.ybin.callaudio.model.FileReaultBean;
import com.ybin.callaudio.model.ReaultBean;
import com.ybin.callaudio.service.LocationService;
import com.ybin.callaudio.utils.AppConfig;
import com.ybin.callaudio.utils.FileUtil;
import com.ybin.callaudio.utils.LogUtils;
import com.ybin.callaudio.utils.Utils;
import com.ybin.callaudio.view.MyGridView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
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
 * 描述:详情页面
 */
public class DeatilActivity extends BaseActivity implements PopupWindow.OnDismissListener {

    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private String provider;
    private PopupWindow popupWindow;

    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;

    //调用照相机返回图片文件
    private File tempFile;

    private List<String> mBitmaps = new ArrayList<>();
    @BindView(R.id.gv_images)
    MyGridView mGridView;
    ImageAdapter mImageAdapter;
    private String flag;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd  HH:mm:ss");
    private List<File> imgFiles = new ArrayList<>();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_PNG2 = MediaType.parse("application/octet-stream");
    @BindView(R.id.edt_content)
    EditText edt_content;

    private String picID = "";
    private String audioID = "";

    private double x, y;
    private LocationService locationService;
    private String country,city,district,street,addr;
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_deatil;
    }

    @Override
    public void initData() {
        flag = AppConfig.getInstance().getString("name", "") + "    ";


        locationService = ((MyApp) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }

        locationService.start();// 定位SDK

    }

    @OnClick({R.id.text_pic, R.id.iv_back, R.id.text_mp3, R.id.btn_updata})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.text_mp3:
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */

                intent.setType("audio/*"); //选择音频
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 11);
                break;
            case R.id.text_pic:
                openPopupWindow(view);

                break;
            case R.id.btn_updata:
                if (!TextUtils.isEmpty(edt_content.getText().toString())) {
                    HashMap<String, String> param = new HashMap<>();
                    showProgressDialog("加载中..");
                    param.put("pId", picID);
                    param.put("caseId", getIntent().getStringExtra("caseId"));
                    param.put("sId", audioID);
                    param.put("country", country);
                    param.put("city", city);
                    param.put("district", district);
                    param.put("street", street);
                    param.put("addr", addr);
                    param.put("note", edt_content.getText().toString());
                    param.put("positionX",x + "");//纬度
                    param.put("positionY", y+ "");//经度

                    LogUtils.e("提交数据", "pId:" + picID + "sId:" + audioID +
                            "positionX:" + x+ "positionY:" + y + "");
                    sendHttpGet(ApiService.UPLOAD_DATA, param, 13011);
                } else {
                  //  Toast.makeText(baseContext, "请输入内容！", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }

    @Override
    public void aidHandleMessage(int what, int type, Object obj) {
        super.aidHandleMessage(what, type, obj);
        switch (what) {
            case 10004:
                switch (type) {
                    case 13011:
                        hideCustomProgressDialog();
                        LogUtils.d("提交数据", obj.toString());
                        ReaultBean reaultBean = JSON.parseObject(obj.toString(), ReaultBean.class);
                        if (reaultBean.getStatus() == 0) {
                            Toast.makeText(baseContext, "提交成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        break;
                }

                break;
            case 10003:
                hideCustomProgressDialog();
                Toast.makeText(baseContext, obj.toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(baseContext, "com.ybin.callaudio", tempFile);
                        if (contentUri != null) {
                            Bitmap bitmapFormUri = null;
                            try {
                                bitmapFormUri = Utils.getBitmapFormUri(this, contentUri);
                                ExifInterface exifInterface = new ExifInterface(Utils.getRealFilePath(baseContext, contentUri));
                                String FDateTime = exifInterface
                                        .getAttribute(ExifInterface.TAG_DATETIME);
                                final Bitmap photo = Utils.createBitmap(bitmapFormUri, flag + formatter.format(new Date(System.currentTimeMillis())));

                                showImage(photo, FDateTime);//显示图片
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //  createBitmap(bitmapFormUri, "wode fsf");
                        }
                    } else {
                        try {
                            Bitmap bitmapFormUri = null;
                            bitmapFormUri = Utils.getBitmapFormUri(this, Uri.fromFile(tempFile));
                            ExifInterface exifInterface = new ExifInterface(Utils.getRealFilePath(baseContext, Uri.fromFile(tempFile)));

                            String FDateTime = exifInterface
                                    .getAttribute(ExifInterface.TAG_DATETIME);

                            final Bitmap photo = Utils.createBitmap(bitmapFormUri, flag + formatter.format(new Date(System.currentTimeMillis())));
                            showImage(photo, FDateTime);//显示图片
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    if (uri != null) {
                        Bitmap bitmapFormUri = null;
                        try {
                            bitmapFormUri = Utils.getBitmapFormUri(this, uri);
                            ExifInterface exifInterface = new ExifInterface(Utils.getRealFilePath(baseContext, uri));
                            String FDateTime = exifInterface
                                    .getAttribute(ExifInterface.TAG_DATETIME);


                            final Bitmap photo = Utils.createBitmap(bitmapFormUri, flag + formatter.format(new Date(System.currentTimeMillis())));

                            showImage(photo, FDateTime);//显示图片


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 11:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    String path = getRealFilePath(baseContext, uri);
                    LogUtils.e("v_name=", path);
                    LogUtils.e("v_name=", uri);
                    if (path != null) {
                        uploadPic(ApiService.UPLOAD_AUDIO, new HashMap<String, String>(), new File(path), 2);
                    } else {
                        Toast.makeText(baseContext, "文件路径错误", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void showImage(Bitmap photo, String time) {
        // mImageView.setImageBitmap(photo);
        final String imgName = String.valueOf(System.currentTimeMillis());
        FileUtil.saveSDcardImage(photo, Global.SDCARD_IMG_ROOT, imgName);
        String filepath = Global.SDCARD_IMG_ROOT + "/" + imgName + ".png";
        mBitmaps.add(filepath);
        imgFiles.add(new File(filepath));
        mImageAdapter = new ImageAdapter(baseContext, mBitmaps);
        mGridView.setAdapter(mImageAdapter);
        Map<String, String> params = new HashMap<>();
        params.put("shootingPlace", "");
        if (!TextUtils.isEmpty(time)) {
            params.put("shootingTime", time);
        }
        uploadPic(ApiService.UPLOAD_PIC, params, new File(filepath), 1);


//     、、   Toast.makeText(baseContext, mBitmaps.size() + "", Toast.LENGTH_SHORT).show();
    }


    /**
     * 提交数据
     *
     * @param reqUrl
     * @param params
     * @param files
     */
    private void uploadPic(String reqUrl, Map<String, String> params, final File files, final int flag) {
        showProgressDialog("正在提交...");

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        if (params != null) {
            for (String key : params.keySet()) {
                multipartBodyBuilder.addFormDataPart(key, params.get(key));
            }
        }

        if (files != null && flag == 1) {
            multipartBodyBuilder.addFormDataPart("file", files.getName(), RequestBody.create(MEDIA_TYPE_PNG, files));
        }
        if (files != null && flag == 2) {
            multipartBodyBuilder.addFormDataPart("file", files.getName(), RequestBody.create(MEDIA_TYPE_PNG2, files));
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
                hideCustomProgressDialog();
//                ToastUtils.showShort("提交失败");
            }

            @Override
            public void onResponse(Call call, final Response response) {


                hideCustomProgressDialog();
                if (response.code() == 200) {
                    new Thread() {
                        public void run() {
                            Looper.prepare();
                            try {

                                String json = response.body().string();
                                LogUtils.e("提交", json);
                                FileReaultBean reaultBean = JSON.parseObject(json, FileReaultBean.class);

                                if (reaultBean.getStatus() == 0 && flag == 1) {
                                    picID = picID + reaultBean.getData().getId() + ",";
                                    Toast.makeText(DeatilActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();

                                } else {
                                    audioID = audioID + reaultBean.getData().getId() + ",";
                                    Toast.makeText(DeatilActivity.this, "录音上传成功", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Looper.loop();// 进入loop中的循环，查看消息队列
                        }
                    }.start();

                }

            }
        });
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(this).inflate(R.layout.view_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 10);
        //设置消失监听
        popupWindow.setOnDismissListener(this);
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view) {
        TextView tv_pick_phone, tv_pick_zone, tv_cancel;
        tv_pick_phone = (TextView) view.findViewById(R.id.tv_pick_phone);
        tv_pick_zone = (TextView) view.findViewById(R.id.tv_pick_zone);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        tv_pick_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicFromAlbm();
                popupWindow.dismiss();
            }
        });
        tv_pick_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicFromCamera();
                popupWindow.dismiss();
            }
        });
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(DeatilActivity.this, "com.ybin.callaudio", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            Log.e("dasd", contentUri.toString());
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                y= location.getLatitude();
                x = location.getLongitude();//weidu
                country = location.getCountry();
                city = location.getCity();
                district = location.getDistrict();
                street = location.getStreet();
                addr = location.getAddrStr();

//                Toast.makeText(DeatilActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                LogUtils.d("位置", sb.toString());
                // logMsg(sb.toString());
            }
        }

    };
}
