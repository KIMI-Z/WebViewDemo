package com.lkl.ansuote.demo.webviewdemo.base;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by huangdongqiang on 04/05/2017.
 */
public class BaseWebView extends WebView{
    private WebSettings mSettings;

    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        webSetting();
    }

    private void webSetting() {
        mSettings = getSettings();
        if (null != mSettings) {
            // 网页内容的宽度是否可大于WebView控件的宽度
            mSettings.setLoadWithOverviewMode(false);
            // 保存表单数据
            mSettings.setSaveFormData(true);
            // 是否应该支持使用其屏幕缩放控件和手势缩放
            mSettings.setSupportZoom(true);
            mSettings.setBuiltInZoomControls(true);
            mSettings.setDisplayZoomControls(false);
            // 启动应用缓存
            mSettings.setAppCacheEnabled(true);
            // 设置缓存模式
            mSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            // setDefaultZoom  api19被弃用
            // 设置此属性，可任意比例缩放。
            mSettings.setUseWideViewPort(true);
            // 缩放比例 1
            this.setInitialScale(1);
            // 告诉WebView启用JavaScript执行。默认的是false。
            mSettings.setJavaScriptEnabled(true);
            //  页面加载好以后，再放开图片
            //mSettings.setBlockNetworkImage(false);
            // 使用localStorage则必须打开
            mSettings.setDomStorageEnabled(true);
            // 排版适应屏幕
            mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            // WebView是否支持多个窗口。
            mSettings.setSupportMultipleWindows(true);

            // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            // 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)
            //ws.setTextZoom(100);
        }

        webSettingsImp(mSettings);
    }

    protected void webSettingsImp(WebSettings webSettings){

    }


    public void onDestory() {
        mSettings = null;
    }


}
