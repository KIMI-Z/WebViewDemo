package com.lkl.ansuote.demo.webviewdemo.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

import com.lkl.ansuote.demo.webviewdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 点击网页中的全屏按钮，可以全屏（横屏）播放视频的webview
 * Created by huangdongqiang on 30/04/2017.
 */
public class VideoWebViewActivity extends Activity {
    public static final String BUNDLE_URL = "bundle_url";
    @BindView(R.id.webview_video) VideoWebView mVideoWebView;
    @BindView(R.id.framelayout_container) FrameLayout mContainerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_webview);
        ButterKnife.bind(this);
        initWebView();
        loadUrl();
    }

    private void initWebView() {
        if (null != mVideoWebView) {
            mVideoWebView.setIgnoreSslError(true);  //设置忽略证书错误
            mVideoWebView.setOnVideoWebViewListener(new VideoWebViewListenerImp());
        }
    }

    private void loadUrl() {
        if (null == mVideoWebView) {
            return;
        }

        Intent intent = getIntent();
        if (null != intent) {
            String url = intent.getStringExtra(BUNDLE_URL);
            if (!TextUtils.isEmpty(url)) {
                mVideoWebView.loadUrl(url);
            }
        }
    }

    /**
     * 自定义全屏接口实现类
     */
    class VideoWebViewListenerImp implements VideoWebView.OnVideoWebViewListener {

        @Override
        public void onJsEnterFullSceenMode() {
            setLandscape();
        }

        @Override
        public void onJsExitFullScreenMode() {
            setPortrait();
        }

        @Override
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            setLandscape();
            setFullScreen(view);
        }

        @Override
        public void onHideCustomView(WebChromeClient.CustomViewCallback callback) {
            setPortrait();
            if (null != callback) {
                callback.onCustomViewHidden();
            }
            setNormalScreen();
        }
    }

    /**
     * 设置横屏
     */
    private void setLandscape() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 设置竖屏
     */
    private void setPortrait() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 常规方式：设置全屏
     * @param view
     */
    private void setFullScreen(View view) {
        if (null == mVideoWebView || null == mContainerLayout || null == view) {
            return;
        }
        mVideoWebView.setVisibility(View.GONE);
        mContainerLayout.addView(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mContainerLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 常规方式：设置正常模式（非全屏）
     */
    private void setNormalScreen() {
        if (null == mVideoWebView || null == mContainerLayout) {
            return;
        }
        mContainerLayout.removeAllViews();
        mContainerLayout.setVisibility(View.GONE);
        mVideoWebView.setVisibility(View.VISIBLE);
    }

    /**
     * 打开此 Activity
     * @param context
     * @param url
     */
    public static void actionStart(Context context, String url) {
        if (null != context) {
            Intent intent = new Intent(context, VideoWebViewActivity.class);
            if (null != intent) {
                intent.putExtra(BUNDLE_URL, url);
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (null != mVideoWebView) {
            if (mVideoWebView.canGoBack()) {
                mVideoWebView.goBack();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mVideoWebView) {
            mVideoWebView.onDestory();
            mVideoWebView = null;
        }

        //mainifest声明为新进程，则在 onDestory 的时候杀死当前进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
