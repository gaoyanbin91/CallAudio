package com.ybin.callaudio.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridView extends GridView{

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * 设置不滑动 的GriView 避免滑动事件与 ScrollView的滑动事件冲突 
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);  
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}

