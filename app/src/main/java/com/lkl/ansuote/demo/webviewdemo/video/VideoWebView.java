package com.lkl.ansuote.demo.webviewdemo.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lkl.ansuote.demo.webviewdemo.base.BaseWebView;

import java.net.URISyntaxException;

/**
 * Created by huangdongqiang on 03/05/2017.
 */
public class VideoWebView extends BaseWebView{
    private OnVideoWebViewListener mOnVideoWebViewListener;
    private WebChromeClient.CustomViewCallback mCallback;
    private boolean mFullScreenMode;  //是否进入了横屏全屏模式

    public VideoWebView(Context context) {
        super(context);
    }

    public VideoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init() {
        super.init();

        this.setWebViewClient(new CustomWebClient());
        this.setWebChromeClient(new CustomWebChromClient());
        this.addJavascriptInterface(new VideoJsObject(), "onClickFullScreenBtn");
    }

    @Override
    protected void webSettingsImp(WebSettings webSettings) {
        super.webSettingsImp(webSettings);
    }

    /**
     * 响应改变浏览器中装饰元素的事件（JavaScript 警告，网页图标，状态条加载，网页标题的刷新）
     */
    private class CustomWebChromClient extends WebChromeClient {

        /**
         * 常规方式下，点击全屏按钮的时候调
         * @param view
         * @param callback
         */
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            Log.i("lkl", "onShowCutomView");
            mCallback=callback;
            if (null != mOnVideoWebViewListener) {
                mOnVideoWebViewListener.onShowCustomView(view, callback);
            }
            mFullScreenMode = true;
        }

        /**
         * 常规方式下，点击退出全屏的时候调
         */
        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            Log.i("lkl", "onHideCustomView");
            if (null != mOnVideoWebViewListener) {
                mOnVideoWebViewListener.onHideCustomView(mCallback);
            }
            mFullScreenMode = false;
        }
    }

    private class CustomWebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //页面加载完成的时候，注入js
            String js= TagUtils.getJs(url);
            view.loadUrl(js);
            Log.i("lkl", "onPageFinished -- url = " + url + "\njs = " + js);
        }

        /**
         * (此接口 Android N 以后 deprecation)
         * true: 不处理这个url，我自己来； false：webView加载这个url，我什么都不做
         */
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return handleUri(view, uri);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            final Uri uri = request.getUrl();
            return handleUri(view, uri);
        }

        private boolean handleUri(WebView view, final Uri uri) {
            final String  url = uri.toString();
            //final String host = uri.getHost();  //m.youku.com
            //final String scheme = uri.getScheme();  // http

            if (url.startsWith("intent://")) {
                try {
                    Context context = view.getContext();
                    Intent intent = new Intent().parseUri(url, Intent.URI_INTENT_SCHEME);

                    if (intent != null) {
                        view.stopLoading();

                        PackageManager packageManager = context.getPackageManager();
                        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (info != null) {
                            context.startActivity(intent);
                        } else {
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            view.loadUrl(fallbackUrl);

                            // or call external broswer
                            //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
                            //context.startActivity(browserIntent);
                        }

                        return true;
                    }
                } catch (URISyntaxException e) {
                }
            }

            return false;
        }
    }

    private class VideoJsObject {

        @JavascriptInterface
        public void fullscreen() {
            if (null != mOnVideoWebViewListener) {
                if (mFullScreenMode) {
                    mOnVideoWebViewListener.onJsExitFullScreenMode();
                } else {
                    mOnVideoWebViewListener.onJsEnterFullSceenMode();
                }
                mFullScreenMode = !mFullScreenMode; //重置全屏状态
            }
        }
    }


    public void setOnVideoWebViewListener(OnVideoWebViewListener onVideoWebViewListener) {
        mOnVideoWebViewListener = onVideoWebViewListener;
    }

    public interface OnVideoWebViewListener {

        /**
         * 注入方式：进入横屏全屏模式
         */
        void onJsEnterFullSceenMode();

        /**
         * 注入方式：退出横屏全屏模式
         */
        void onJsExitFullScreenMode();

        /**
         * 常规方式：点击网页上的全屏按钮时，执行此方法
         * @param view
         * @param callback
         */
        void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback);

        /**
         * 常规方式：再次点击网页上的全屏按钮，执行此方法
         * @param callback
         */
        void onHideCustomView(WebChromeClient.CustomViewCallback callback);
    }

    /**
     * 获取当前是否全屏的状态
     * @return
     */
    public boolean getFullScreenMode() {
        return mFullScreenMode;
    }

    /**
     * 设置是否为全屏模式
     * @param fullScreenMode
     */
    public void setFullScreenMode(boolean fullScreenMode) {
        mFullScreenMode = fullScreenMode;
    }

    /**
     * 外部手动销毁
     */
    public void onDestory() {
        super.onDestory();
        mOnVideoWebViewListener = null;
        mCallback = null;
        stopLoading();
        clearCache(true);
        clearFormData();
        clearMatches();
        clearHistory();
        clearDisappearingChildren();
        clearAnimation();
        removeAllViews();
        destroy();
    }
}
