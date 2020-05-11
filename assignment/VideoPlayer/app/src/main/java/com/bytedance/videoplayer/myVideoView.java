package com.bytedance.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

public class myVideoView extends VideoView {
    public myVideoView(Context context){
        super(context);
    }

    public myVideoView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public myVideoView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    public void setOnTouchListener(View v){
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //此处设置的默认值可随意,因为getDefaultSize中的size是有值的
        int width = getDefaultSize(0,widthMeasureSpec);
        int height = getDefaultSize(0,heightMeasureSpec);
        setMeasuredDimension(width,height);
        System.out.println("======onMeasure===width==="+width);
        System.out.println("======onMeasure===height==="+height);
    }
}
