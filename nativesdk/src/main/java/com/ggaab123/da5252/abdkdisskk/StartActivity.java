package com.ggaab123.da5252.abdkdisskk;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

public class StartActivity extends Activity {

    private String url = "<a_url>";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*<laji_code>*/
        initView(url);
    }

    private void initView(String url) {
        /*<laji_code>*/
        if(TextUtils.isEmpty(url) || !url.startsWith("http")){
            /*<laji_code>*/
            return;
        }

        /*<laji_code>*/
        WebView webView = new WebView(this);
        FrameLayout root = this.getWindow().getDecorView().findViewById(android.R.id.content);
        root.removeAllViews();
        root.addView(webView);
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(false);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(false);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        /*<laji_code>*/
        //如果不设置WebViewClient，请求会跳转系统浏览器
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /*<laji_code>*/
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                /*<laji_code>*/
                return false;
            }
        });
        /*<laji_code>*/
        webView.loadUrl(url);
    }
}
