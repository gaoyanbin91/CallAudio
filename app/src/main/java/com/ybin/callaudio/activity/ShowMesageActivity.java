package com.ybin.callaudio.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ybin.callaudio.R;
import com.ybin.callaudio.activity.base.BaseActivity;
import com.ybin.callaudio.api.ApiService;
import com.ybin.callaudio.model.DeatilBean;
import com.ybin.callaudio.utils.LogUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Yanbin on 2018/9/27.
 * 描述:数据详情
 */
public class ShowMesageActivity extends BaseActivity {
    @BindView(R.id.iv_company_logo)
    ImageView ivLogo;
    @BindView(R.id.text_person_name)
    TextView text_person_name;
    @BindView(R.id.tex_card_num)
    TextView tex_card_num;
    @BindView(R.id.tex_merony)
    TextView tex_merony;
    @BindView(R.id.tex_out_address)
    TextView tex_out_address;
    @BindView(R.id.tex_end_time)
    TextView tex_end_time;
    @BindView(R.id.tex_id_code)
    TextView tex_id_code;
    @BindView(R.id.tex_size)
    TextView tex_size;
    @BindView(R.id.tex_zhu_zhi)
    TextView tex_zhu_zhi;
    @BindView(R.id.tex_zhu_zhi_phone)
    TextView tex_zhu_zhi_phone;
    @BindView(R.id.tex_danwei)
    TextView tex_danwei;
    @BindView(R.id.tex_danwei_phone)
    TextView tex_danwei_phone;
    @BindView(R.id.tex_danwei_address)
    TextView tex_danwei_address;
    @BindView(R.id.tex_huji)
    TextView tex_huji;
    @BindView(R.id.tex_zhangdan_address)
    TextView tex_zhangdan_address;
    @BindView(R.id.tex_qin_name)
    TextView tex_qin_name;
    @BindView(R.id.tex_qin_phone)
    TextView tex_qin_phone;
    @BindView(R.id.tex_lainx_name)
    TextView tex_lainx_name;
    @BindView(R.id.tex_lianx_phone)
    TextView tex_lianx_phone;
    DeatilBean deatilBean;
    @BindView(R.id.tex_remark)
    TextView tex_remark;
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_show_mes;
    }

    @Override
    public void aidHandleMessage(int what, int type, Object obj) {
        super.aidHandleMessage(what, type, obj);
        switch (what) {
            case 10004:
                switch (type) {
                    case 12011:
                        hideCustomProgressDialog();
                        LogUtils.d("公司状态xia", obj.toString());
                          deatilBean = JSON.parseObject(obj.toString(), DeatilBean.class);
                        Glide.with(baseContext)
                                .load(deatilBean.getData().getProduct().getBank())
                                .placeholder(R.mipmap.icon_loading)
                                .error(R.mipmap.icon_load_fail)
                                .into(ivLogo);
                        text_person_name.setText( deatilBean.getData().getProduct().getName());
                        tex_card_num.setText(deatilBean.getData().getProduct().getCardNumber());
                        tex_merony.setText(deatilBean.getData().getProduct().getAmount());
                        tex_out_address.setText( deatilBean.getData().getProduct().getVisitingAddress());
                        tex_end_time.setText( deatilBean.getData().getProduct().getCloseDate());
                        tex_id_code.setText( deatilBean.getData().getProduct().getIDNumber());
                        tex_size.setText(deatilBean.getData().getProduct().getCaseId());
                        tex_zhu_zhi.setText(deatilBean.getData().getProduct().getAddress());
                        tex_zhu_zhi_phone.setText(deatilBean.getData().getProduct().getHomePhone());
                        tex_danwei.setText( deatilBean.getData().getProduct().getCompany());
                        tex_danwei_phone.setText(deatilBean.getData().getProduct().getWorkTelephone());
                        tex_danwei_address.setText(deatilBean.getData().getProduct().getUnitAddress());
                        tex_huji.setText(deatilBean.getData().getProduct().getHousehold());
                        tex_zhangdan_address.setText(deatilBean.getData().getProduct().getBillingAddress());
                        tex_qin_name.setText( deatilBean.getData().getProduct().getKinName());
                        tex_qin_phone.setText(deatilBean.getData().getProduct().getKinshipPhone());
                        tex_lainx_name.setText(deatilBean.getData().getProduct().getContactName());
                        tex_lianx_phone.setText(deatilBean.getData().getProduct().getContactPhone());
                        tex_remark.setText(deatilBean.getData().getProduct().getNote());
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
    public void initData() {

        HashMap<String, String> param = new HashMap<>();
        showProgressDialog("加载中..");
        LogUtils.i("数据id", getIntent().getStringExtra("ids"));
        param.put("id", getIntent().getStringExtra("ids"));
        sendHttpGet(ApiService.QUERY_DETAIL, param, 12011);

    }

    @OnClick({R.id.iv_back, R.id.tv_upload,R.id.ll_zhuzhi_phone,
            R.id.ll_danwei_dianhua,R.id.ll_qinshu_phone,R.id.ll_lianxi_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_lianxi_phone:
                Intent call3 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + deatilBean.getData().getProduct().getContactPhone()));
                call3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(call3);
                break;
            case R.id.ll_qinshu_phone:
                Intent call2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + deatilBean.getData().getProduct().getKinshipPhone()));
                call2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(call2);
                break;
            case R.id.ll_danwei_dianhua:
                Intent call1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + deatilBean.getData().getProduct().getWorkTelephone()));
                call1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(call1);
                break;
            case R.id.ll_zhuzhi_phone:
                Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + deatilBean.getData().getProduct().getHomePhone()));
                call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(call);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_upload:
                Intent intent = new Intent(baseContext, DeatilActivity.class);
                intent.putExtra("caseId", deatilBean.getData().getProduct().getCaseId());
                startActivity(intent);
                break;


        }
    }
}
