package com.ybin.callaudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ybin.callaudio.R;

import java.util.List;

/**
 * Created by gaoyanbin on 2017/12/3.
 * 描述:
 */

public class ImageAdapter extends BaseAdapter{
    List<String> list;
    LayoutInflater inflater;
    Context context;

    public ImageAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.main_item, parent, false);
            viewHolder.tvName1 = (ImageView) convertView.findViewById(R.id.iv_model_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(list.get(position)).into(viewHolder.tvName1);


        return convertView;
    }

    class ViewHolder {
        ImageView tvName1;
    }
}
