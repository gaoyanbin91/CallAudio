package com.ybin.callaudio.activity.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ybin.callaudio.MyApp;
import com.ybin.callaudio.R;
import com.ybin.callaudio.activity.MainActivity;
import com.ybin.callaudio.utils.KeyBoardUtils;
import com.ybin.callaudio.utils.LogUtils;
import com.ybin.callaudio.utils.SystemBarTintManager;
import com.ybin.callaudio.utils.okhttp.callback.ResultCallback;
import com.ybin.callaudio.utils.okhttp.request.OkHttpRequest;
import com.ybin.callaudio.view.CustomProgressDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import butterknife.ButterKnife;
import okhttp3.Request;


/**
 * Created by gaoyanbin on 2018/3/26.
 * 描述:基础activity
 */

public abstract class BaseActivity extends AppCompatActivity {
    public Context baseContext;
    private static final String TAG = BaseActivity.class.getName();
    private final static String KEY_HAS_INTERSTITIAL_AD = "KEY_HAS_INTERSTITIAL_AD";
    protected boolean mHasInterstitialAd = false;
    private boolean systemBar = true;
    int statusBarHeight = 0;
    public static final int CUSTOM_DISMISS = 6;
    private static long mPreTime;
    public static List<Activity> mActivities = new LinkedList<>();
    private static Activity mCurrentActivity;// 对所有activity进行管理
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {


                    case 10003:


                        if (msg.obj == null) {
                            return;
                        }

                        aidHandleMessage(msg.what, msg.arg1, msg.obj);
                        break;
                    case 10004:
                        aidHandleMessage(msg.what, msg.arg1, msg.obj);
                        break;
                    case 10005:
                        aidHandleMessage(msg.what, msg.obj);
                        break;
                    case 100000:

                        break;
                    default:
                        aidHandleMessage(msg.what, msg.obj);
                }
            } catch (Throwable var3) {
                Log.e(BaseActivity.TAG, "handleMessage", var3);
            }

        }
    };

    public void aidHandleMessage(int what, Object obj) {
    }

    public void aidHandleMessage(int what, int type, Object obj) {
    }

    public void aidsendMessage(int what, Object obj) {
        Message msg = this.mHandler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        this.mHandler.sendMessage(msg);
    }

    public void aidsendMessage(int what, int arg1, Object obj) {
        Message msg = this.mHandler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.obj = obj;
        this.mHandler.sendMessage(msg);
    }

    public void setSystemBar(boolean systemBar) {
        this.systemBar = systemBar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getActivityHashMap().put(this.getClass().getName(), this);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_HAS_INTERSTITIAL_AD)) {
                mHasInterstitialAd = savedInstanceState.getBoolean(KEY_HAS_INTERSTITIAL_AD);
                savedInstanceState.remove(KEY_HAS_INTERSTITIAL_AD);
            }
        }
        baseContext = this;

        //初始化的时候将其添加到集合中
        synchronized (mActivities) {

            mActivities.add(this);
        }
        setContentView(provideContentViewId());
        ButterKnife.bind(this);
        initView();
        initData();

        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && systemBar) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            statusBarHeight = tintManager.getConfig().getStatusBarHeight();

            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            //tintManager.setTintColor(getResources().getColor(R.color.activity_login_orange));
            tintManager.setTintDrawable(getResources().getDrawable(R.drawable.status_background));

        }


    }

    public void initView() {
    }


    public void initData() {
    }

    protected abstract int provideContentViewId();


    /**
     * 退出应用的方法
     */
    public static void exitApp() {

        ListIterator<Activity> iterator = mActivities.listIterator();

        while (iterator.hasNext()) {
            Activity next = iterator.next();
            next.finish();
        }
    }


    public void sendHttpGet(String url, HashMap<String, String> map, final int refreshType) {

        try {
            HashMap<String, String> heard = new HashMap<>();
            Log.e("GET请求url==>" , url);

            (new OkHttpRequest.Builder()).url(url).params(map).headers(heard).get(new ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {


                    if (e instanceof UnknownHostException) {
                        aidsendMessage(10003, refreshType, "当前网络不可用，请检查网络再试");
                    } else if (e instanceof SocketTimeoutException) {
                        aidsendMessage(10003, refreshType, "请求超时，请稍后再试");
                    } else {
                        aidsendMessage(10003, refreshType, "网络异常，请稍后再试");
                    }
                }

                @Override
                public void onResponse(String response) {

                    aidsendMessage(10004, refreshType, response);
                    //    LogUtils.d("shuj",response.toString());
                }
            });
        } catch (Throwable var6) {
            Log.e(TAG, "sendHttp", var6);
        }
    }

    public void sendHttpPost(String url, HashMap<String, String> map, final int refreshType) {
        try {
            HashMap<String, String> heard = new HashMap<>();
            LogUtils.i("", "POST请求url==>" + url);
            (new OkHttpRequest.Builder()).url(url).params(map).headers(heard).post(new ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {

                    if (e instanceof UnknownHostException) {
                        aidsendMessage(10003, refreshType, "当前网络不可用，请检查网络再试");
                    } else if (e instanceof SocketTimeoutException) {
                        aidsendMessage(10003, refreshType, "请求超时，请稍后再试");
                    } else {
                        aidsendMessage(10003, refreshType, "网络异常，请稍后再试");
                    }
                    // LibraryActivity.this.aidsendMessage(10005, responseBean);
                }

                @Override
                public void onResponse(String response) {
                    aidsendMessage(10004, refreshType, response);
                }
            });
        } catch (Throwable var6) {
            Log.e(TAG, "sendHttp", var6);
        }

    }

    public static Activity getCurrentActivity() {
        return mCurrentActivity;

    }

    CustomProgressDialog customProgressDialog;

    public void showProgressDialog(String text) {
        if (customProgressDialog == null) {

            customProgressDialog = new CustomProgressDialog(this,text);
            customProgressDialog.setCanceledOnTouchOutside(false);
            //customProgressDialog.setCancelable(true);
        }
        customProgressDialog.show();
    }

    public void hideCustomProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
    }
    /**
     * 统一退出控制
     */
    @Override
    public void onBackPressed() {
        if (mCurrentActivity instanceof MainActivity) {
            //如果是主页面
            if (System.currentTimeMillis() - mPreTime > 2000) {// 两次点击间隔大于2秒
                Toast.makeText(baseContext,"再按一次，退出应用",Toast.LENGTH_SHORT).show();
                mPreTime = System.currentTimeMillis();
                return;
            }
        }
        super.onBackPressed();// finish()
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentActivity = null;
    }


    /**
     * 隐藏键盘
     *
     * @param v   焦点所在View
     * @param ids 输入框
     * @return true代表焦点在edit上
     */
    public boolean isFocusEditText(View v, int... ids) {
        if (v instanceof EditText) {
            EditText tmp_et = (EditText) v;
            for (int id : ids) {
                if (tmp_et.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    //是否触摸在指定view上面,对某个控件过滤
    public boolean isTouchView(View[] views, MotionEvent ev) {
        if (views == null || views.length == 0) {
            return false;
        }
        int[] location = new int[2];
        for (View view : views) {
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (ev.getX() > x && ev.getX() < (x + view.getWidth())
                    && ev.getY() > y && ev.getY() < (y + view.getHeight())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isTouchView(filterViewByIds(), ev)) {
                return super.dispatchTouchEvent(ev);
            }
            if (hideSoftByEditViewIds() == null || hideSoftByEditViewIds().length == 0) {
                return super.dispatchTouchEvent(ev);
            }
            View v = getCurrentFocus();
            if (isFocusEditText(v, hideSoftByEditViewIds())) {
                //隐藏键盘
                KeyBoardUtils.hideInputForce(this);
                clearViewFocus(v, hideSoftByEditViewIds());
            }
        }
        return super.dispatchTouchEvent(ev);

    }

    /**
     * 清除editText的焦点
     *
     * @param v   焦点所在View
     * @param ids 输入框
     */
    public void clearViewFocus(View v, int... ids) {
        if (null != v && null != ids && ids.length > 0) {
            for (int id : ids) {
                if (v.getId() == id) {
                    v.clearFocus();
                    break;
                }
            }
        }
    }

    /**
     * 传入EditText的Id
     * 没有传入的EditText不做处理
     *
     * @return id 数组
     */
    public int[] hideSoftByEditViewIds() {
        return null;
    }

    /**
     * 传入要过滤的View
     * 过滤之后点击将不会有隐藏软键盘的操作
     *
     * @return id 数组
     */
    public View[] filterViewByIds() {
        return null;
    }
}
