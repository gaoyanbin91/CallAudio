package com.ybin.callaudio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ybin.callaudio.R;
import com.ybin.callaudio.model.ListDatasBean;

import java.util.List;


/**
 * Created by gaoyanbin on 2018/7/4.
 * 描述:新闻适配器
 */
public class ListBankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = 1;
    private Context mContext;
    public List<ListDatasBean.DataBean.ListBean> mList;

    private MyItemClick myItemClick;
    private MyItemButtonClick myItemButtonClick;
    public MyItemButtonClick getMyItemButtonClick() {
        return myItemButtonClick;
    }

    public void setMyItemButtonClick(MyItemButtonClick myItemButtonClick) {
        this.myItemButtonClick = myItemButtonClick;
    }
    public ListBankAdapter(Context mContext, List<ListDatasBean.DataBean.ListBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news_list, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != TYPE_NORMAL) {
            return;
        }
        final ListDatasBean.DataBean.ListBean csvm = mList.get(position);
        final MyViewHolder viewHolder = (MyViewHolder) holder;

        Glide.with(mContext)
                .load(csvm.getBank())
                .placeholder(R.mipmap.icon_loading)
                .error(R.mipmap.icon_load_fail)
                .into(viewHolder.iv_bank);
        viewHolder.tv_name.setText(csvm.getName());
        viewHolder.tv_bank_num.setText("卡号："+csvm.getCardNumber() + "");
        viewHolder.tv_monery.setText(csvm.getAmount());

        viewHolder.tv_address.setText("地址："+csvm.getVisitingAddress());

        viewHolder.btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemButtonClick!=null){
                    myItemButtonClick.onItemButtonClick(csvm);
                }
            }
        });
        viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemClick != null) {
                    myItemClick.onItemClick(csvm);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setMyItemClick(MyItemClick myItemClick) {
        this.myItemClick = myItemClick;
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout rootLayout;
        private ImageView iv_bank;
        private TextView tv_name;
        private TextView tv_bank_num;

        private TextView tv_monery;
        private TextView tv_address;
        private TextView btn_status;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            iv_bank = itemView.findViewById(R.id.iv_bank);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_bank_num = itemView.findViewById(R.id.tv_bank_num);
            tv_monery = itemView.findViewById(R.id.tv_monery);
            btn_status = itemView.findViewById(R.id.btn_status);
            tv_address = itemView.findViewById(R.id.tv_address);
        }
    }

    public interface MyItemClick {
        void onItemClick(ListDatasBean.DataBean.ListBean model);
    }
    public interface MyItemButtonClick {
        void onItemButtonClick(ListDatasBean.DataBean.ListBean model);
    }
    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
