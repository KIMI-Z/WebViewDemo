package com.lkl.ansuote.demo.webviewdemo.video;

import android.app.Activity;
import android.os.Bundle;

import com.lkl.ansuote.demo.webviewdemo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 列表
 * Created by huangdongqiang on 30/04/2017.
 */
public class VideoListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_tencent_normal) void tencentNormalClick() {
        //普通网页视频
        VideoWebViewActivity.actionStart(this, "https://v.qq.com/x/page/m0394wjagsq.html");
    }

    @OnClick(R.id.btn_tencent_fullscreen) void tencentFullScreenClick() {
        //全屏网页视频
        VideoWebViewActivity.actionStart(this, "https://v.qq.com/iframe/player.html?vid=m0394wjagsq&tiny=0&auto=0");
    }

    @OnClick(R.id.btn_bilibili) void bilibiliClick() {
        VideoWebViewActivity.actionStart(this, "http://www.bilibili.com/video/av1229691/?from=search&seid=3051662471162165433");
    }

    @OnClick(R.id.btn_tudou) void toudouClick() {
        VideoWebViewActivity.actionStart(this, "http://video.tudou.com/v/XMjcwNjc2NDQ5Ng==.html?from=s1.8-1-1.2&spm=a2h0k.8191414.0.0");
    }
}


