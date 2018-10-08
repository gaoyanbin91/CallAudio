package com.ybin.callaudio.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ybin.callaudio.R;
import com.ybin.callaudio.activity.base.BaseActivity;
import com.ybin.callaudio.adapter.ListBankAdapter;
import com.ybin.callaudio.api.ApiService;
import com.ybin.callaudio.model.AreaBean;
import com.ybin.callaudio.model.CompanyBean;
import com.ybin.callaudio.model.ListDatasBean;
import com.ybin.callaudio.model.ReaultBean;
import com.ybin.callaudio.runtimepermissions.PermissionsManager;
import com.ybin.callaudio.runtimepermissions.PermissionsResultAction;
import com.ybin.callaudio.utils.AppConfig;
import com.ybin.callaudio.utils.LogUtils;
import com.ybin.callaudio.view.pulltorefresh.CustomRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tex_area)
    TextView texArea;//地区
    @BindView(R.id.re_lists)
    CustomRefreshView reNews;//
    @BindView(R.id.edt_name)
    EditText mEditText;
    private int page = 1;
    private List<AreaBean.DataBean.ListBean> areas = new ArrayList<>();
    String areaArray[];
    private List<ListDatasBean.DataBean.ListBean> mLists = new ArrayList<>();
    ListBankAdapter mAdapter;
    String DEVICE_ID;
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void aidHandleMessage(int what, int type, Object obj) {
        super.aidHandleMessage(what, type, obj);
        switch (what) {
            case 10004:
                switch (type) {
                    case 11011:
                        hideCustomProgressDialog();
                        LogUtils.d("公司信息", obj);
                        CompanyBean companyBean = JSON.parseObject(obj.toString(), CompanyBean.class);
                        AppConfig.getInstance().putString("name", companyBean.getData().getWatermark());

                        Glide.with(baseContext)
                                .load(companyBean.getData().getLogo())
                                .placeholder(R.mipmap.icon_loading)
                                .error(R.mipmap.icon_load_fail)
                                .into(ivLogo);

                        break;
                    case 11012:
                        LogUtils.d("公司信息1", obj);
                        hideCustomProgressDialog();
                        AreaBean areaBean = JSON.parseObject(obj.toString(), AreaBean.class);
                        if (areaBean.getData() != null) {
                            areas = areaBean.getData().getList();
                            areaArray = new String[areas.size()];
                            for (int i = 0; i < areas.size(); i++) {
                                areaArray[i] = areas.get(i).getRegion();
                            }
                        }

                        break;
                    case 11013:
                        LogUtils.d("公司信息2", obj);
                        hideCustomProgressDialog();
                        ListDatasBean listDatasBean = JSON.parseObject(obj.toString(), ListDatasBean.class);
                        if (listDatasBean.getData() != null) {
                            List<ListDatasBean.DataBean.ListBean> datas = new ArrayList<>();
                            datas = listDatasBean.getData().getList();
                            if (datas.size() < 20) {// 每次获取数据不足20条时 说明没有下一页 提示不能上拉加载
                                reNews.onNoMore();
                            }
                            if (page == 1) {//当下拉刷新是 数据清空
                                mLists.clear();
                            }
                            mLists.addAll(datas);
                            //设置适配器
                            mAdapter = new ListBankAdapter(MainActivity.this, mLists);
                            reNews.setAdapter(mAdapter);

                            reNews.complete();
                            mAdapter.setMyItemClick(new ListBankAdapter.MyItemClick() {
                                @Override
                                public void onItemClick(ListDatasBean.DataBean.ListBean model) {
                                    Intent i = new Intent(MainActivity.this, ShowMesageActivity.class);
                                    i.putExtra("ids", model.getId() + "");
                                    startActivity(i);

                                }
                            });
                            mAdapter.setMyItemButtonClick(new ListBankAdapter.MyItemButtonClick() {
                                @Override
                                public void onItemButtonClick(ListDatasBean.DataBean.ListBean model) {
                                    showDialogOK(model.getId());
                                }
                            });
                        }
                        break;

                    case 11014:
                        hideCustomProgressDialog();
                        LogUtils.d("公司状态", obj.toString());
                        ReaultBean reaultBean = JSON.parseObject(obj.toString(), ReaultBean.class);

                        if (reaultBean.getStatus() == 0) {
                            Toast.makeText(baseContext, "修改成功！", Toast.LENGTH_SHORT).show();
                            HashMap<String, String> param = new HashMap<>();
                            showProgressDialog("加载中..");
                            param.put("pageNo", page + "");
                            param.put("pageSize", "10");
                            param.put("region", DEVICE_ID);
                            param.put("name", mEditText.getText().toString());
                            sendHttpGet(ApiService.QUERY_LISTS_MESAGES, param, 11013);
                        } else {
                            Toast.makeText(baseContext, "修改失败！", Toast.LENGTH_SHORT).show();
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

    private void showDialogOK(final int iddd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //      builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("提示");
        builder.setMessage("确定完成该订单?");
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, String> param = new HashMap<>();
                showProgressDialog("修改中..");
                param.put("id", iddd + "");
                param.put("state", "2");
                sendHttpPost(ApiService.SAVE_STATUS, param, 11014);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        builder.show();
    }

    @Override
    public void initData() {
//        mEditText.setText(Utils.sHA1(baseContext));

        requestPermissions();
        HashMap<String, String> param = new HashMap<>();
        showProgressDialog("加载中..");
        sendHttpGet(ApiService.QUERY_COMPANY_MES, param, 11011);

        sendHttpGet(ApiService.QUERY_AREA_LISTS, param, 11012);
        param.put("pageNo", "1");
        param.put("pageSize", "10");
        param.put("region", DEVICE_ID);
        param.put("name", mEditText.getText().toString());

        sendHttpGet(ApiService.QUERY_LISTS_MESAGES, param, 11013);
//        Intent service = new Intent(MyApp.getInstance().getApplicationContext(), RecorderService.class);
//      //  startService(service);
//        service.putExtra("startType", 1);
//        if (Build.VERSION.SDK_INT >= 26) {
//            MyApp.getInstance().startForegroundService(service);
//        } else {
//            MyApp.getInstance().startService(service);
//        }

    }

    @Override
    public void initView() {

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else {
              DEVICE_ID = tm.getDeviceId();
//            Toast.makeText(baseContext, DEVICE_ID, Toast.LENGTH_SHORT).show();
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HashMap<String, String> param = new HashMap<>();
                showProgressDialog("加载中..");
                param.put("pageNo", page+"");
                param.put("pageSize", "10");
                param.put("region", DEVICE_ID);
                param.put("name", s.toString());
                sendHttpGet(ApiService.QUERY_LISTS_MESAGES, param, 11013);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /**
         * 模拟所有现象
         */
        reNews.setOnLoadListener(new CustomRefreshView.OnLoadListener() {
            @Override
            public void onRefresh() {
                page = 1;
                //下拉刷新
                HashMap<String, String> param = new HashMap<>();
                showProgressDialog("加载中..");
                param.put("pageNo", page+"");
                param.put("pageSize", "10");
                param.put("region", DEVICE_ID);
                LogUtils.e("DEVICE_ID",DEVICE_ID);
                param.put("name", mEditText.getText().toString());
                sendHttpGet(ApiService.QUERY_LISTS_MESAGES, param, 11013);
            }
            @Override
            public void onLoadMore() {
                page++;
                //上拉加载更多
                HashMap<String, String> param = new HashMap<>();
                showProgressDialog("加载中..");
                param.put("pageNo", page+"");
                param.put("pageSize", "10");
                param.put("region",DEVICE_ID);
                param.put("name", mEditText.getText().toString());
                sendHttpGet(ApiService.QUERY_LISTS_MESAGES, param, 11013);
            }
        });

        //设置自动下拉刷新，切记要在recyclerView.setOnLoadListener()之后调用
        //因为在没有设置监听接口的情况下，setRefreshing(true),调用不到OnLoadListener
        reNews.setRefreshing(true);
    }

    @OnClick({R.id.ll_area})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_area:
            //    String[] majors = {"郑州", "周口", "洛阳", "新乡"};
                ShowChoise(areaArray, texArea, "--请选择地区--");
                break;
        }
    }

    private void ShowChoise(final String[] items, final TextView textView, String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(baseContext, R.style.AlertDialog);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(title);
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(items[which]);
                HashMap<String, String> param = new HashMap<>();
                showProgressDialog("加载中..");
                param.put("pageNo", page+"");
                param.put("pageSize", "10");
                param.put("region", DEVICE_ID);
                param.put("name", mEditText.getText().toString());
                sendHttpGet(ApiService.QUERY_LISTS_MESAGES, param, 11013);

            }
        });
        builder.show();
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(MainActivity.this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(String permission) {
            }
        });
    }
}
