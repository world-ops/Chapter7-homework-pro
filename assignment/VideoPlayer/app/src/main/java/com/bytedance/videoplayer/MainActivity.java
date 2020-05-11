package com.bytedance.videoplayer;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LongDef;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    public static String URI = "URI";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
        String url = "https://s3.pstatp.com/toutiao/static/img/logo.271e845.png";
        Glide.with(this).load(url).into(imageView);
        setContentView(R.layout.activity_main);


       final Uri uri = getIntent().getData();
       Log.d("LINYUEBEI", "MainActivity onCreate: get uri : " + uri);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (uri != null)
                    jumpToVideoPlayer(uri.toString());
                else
                {
                    jumpToVideoPlayer("null");
                    Log.d("LINYUEBEI", "run: error");
                }
            }
        },1000);



    }
    private void jumpToVideoPlayer(String uri){

        //startActivity(new Intent(this, VideoActivity.class));
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        intent.putExtra(URI ,uri);
        Log.d("LINYUEBEI", "jumpToVideoPlayer: put extra success");
        startActivity(intent);
        Log.d("LINYUEBEI", "jumpToVideoPlayer: start success");
    }
}
