package com.bytedance.videoplayer;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.content.res.Configuration;
        import android.media.AudioTrack;
        import android.media.MediaPlayer;
        import android.net.IpSecManager;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.MediaController;
        import android.widget.RelativeLayout;
        import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    private myVideoView myvideoView;
    private SeekBar seekBar;
    private TextView tv_now;
    private TextView tv_all;
    private boolean screen_flag = true;//判断屏幕转

    /*自动更新进度条，获取当前播放时间并显示*/
    private Handler handler = new Handler();
    private Runnable run =  new Runnable() {
        int  currentPosition, duration;
        public void run() {
            // 获得当前播放时间和当前视频的长度
            currentPosition = myvideoView.getCurrentPosition();
            duration = myvideoView.getDuration();
            int time = ((currentPosition * 100) / duration);
            // 设置进度条的主要进度，表示当前的播放时间
            seekBar.setProgress(time);
            tv_now.setText(msecToTime(currentPosition));
            handler.postDelayed(run, 1000);
        }
    };


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        setTitle("VideoView");
        tv_now = findViewById(R.id.currentTime);
        tv_all = findViewById(R.id.totalTime);

        final Intent intent = getIntent();
        String uri=intent.getStringExtra("URI");
        Log.d("LINYUEBEI", "onCreate: get intent:n" + uri);


        final Button buttonPause = findViewById(R.id.buttonPause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myvideoView.pause();
            }
        });

        Button buttonPlay = findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myvideoView.start();
            }
        });


        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean isTouch = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (isTouch){
                    int process = seekBar.getProgress();
                    if (myvideoView != null) {
                        myvideoView.seekTo(process  * myvideoView.getDuration()/ 100);
                        Log.d("linyuebei", "onProgressChanged: "+process+"-----"+myvideoView.getDuration()+"____"+process * myvideoView.getDuration() / 100 );
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;
                myvideoView.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouch = false;
                myvideoView.start();
            }
        });
        myvideoView = (myVideoView)findViewById(R.id.videoView);

        //设置点击事件，OnClickListener不好用
        myvideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (myvideoView.isPlaying()){
                    myvideoView.pause();
                }else {
                    myvideoView.start();
                }
                return false;
            }
        });


        if(uri.equals("null"))
            uri = "android.resource://" + this.getPackageName() + "/" + R.raw.bytedance;
        myvideoView.setVideoPath(uri);

            //videoView.setVideoPath(getVideoPath(R.raw.bytedance));
        Log.d("LINYUEBEI", "onCreate: uri is:" + uri);

        myvideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //videoView.setMediaController(new MediaController(videoView.this));//系统自带的视频控制条

                int time_all = myvideoView.getDuration();
                tv_all.setText(msecToTime(time_all));
                Log.d("LINYUEBEI", "onPrepared: get all time :" + msecToTime(time_all));
                tv_now.setText("00:00:00");
                handler.postDelayed(run,1000);
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        // 判断当前屏幕的横竖屏状态
        int screenOritentation = getResources().getConfiguration().orientation;
        if (screenOritentation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,dip2px(this,235f));
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            myvideoView.setLayoutParams(params);
            Log.d("LINYUEBEI", "onConfigurationChanged: 竖屏");
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            myvideoView.setLayoutParams(params);
            Log.d("LINYUEBEI", "onConfigurationChanged: 横屏");
        }
    }



    /*
    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }
    */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);    }

    private String msecToTime(int time){
        String timeStr;
        int hour;
        if(time <= 0)
            return "00:00";
        else{
            int second = time / 1000;
            int minute = second / 60;
            if(second < 60){
                timeStr = "00:00:" + unitFormat(second);
            }
            else if (minute < 60){
                second = second % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            }
            else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }

        }
        return timeStr;
    }
    private String unitFormat(int i) {
        //时分秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


}
