package com.lkl.ansuote.demo.webviewdemo.video;

import android.text.TextUtils;

public class TagUtils {

    private static String getTagByUrl(String url) {
        if (url.contains("qq")) {
            if (url.contains("iframe")) {
                //(全屏视频。通过网页【分享】- 【通用代码】)
                // https://v.qq.com/iframe/player.html?vid=m0394wjagsq&tiny=0&auto=0
                return "tvp_fullscreen_button";
            } else {
                // 普通网页界面
                // https://v.qq.com/x/page/m0394wjagsq.html
                return "txp_btn_fullscreen";
            }
        } else if (url.contains("bilibili")) {
            return "icon-widescreen";       // http://www.bilibili.com/mobile/index.html
        }
        return "";
    }

    //  "javascript:document.getElementsByClassName('" + referParser(url) + "')[0].addEventListener('click',function(){local_obj.playing();return false;});"
    public static String getJs(String url) {
        String tag = getTagByUrl(url);
        if (TextUtils.isEmpty(tag)) {
            return "javascript:";
        } else {
            return "javascript:document.getElementsByClassName('" + tag + "')[0].addEventListener('click',function(){onClickFullScreenBtn.fullscreen();return false;});";
        }
    }
}
