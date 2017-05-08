package com.lkl.ansuote.demo.webviewdemo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lkl.ansuote.demo.webviewdemo.video.VideoListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_webview_video) void videoBtn() {
        Intent intent = new Intent(this, VideoListActivity.class);
        if (null != intent) {
            startActivity(intent);
        }
    }
}
