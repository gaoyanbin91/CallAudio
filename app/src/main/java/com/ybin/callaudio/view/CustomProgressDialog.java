package com.ybin.callaudio.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybin.callaudio.R;


/**
 * Created by gaoyanbin on 2018/1/24.
 * 描述:加载进度框
 */

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context , String text) {
        super(context, R.style.custom_dialog);
        this.setContentView(R.layout.progress_dialog);
        setCancelable(true);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        TextView par_title = findViewById(R.id.par_title);
        if (text!=null) {
            par_title.setText(text);
        }
    //    AnimationDrawable animationDrawable = (AnimationDrawable)imageView.getDrawable();
      //  animationDrawable.start();
    }
}
