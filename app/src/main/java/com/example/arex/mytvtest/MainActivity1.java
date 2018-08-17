package com.example.arex.mytvtest;

import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity1 extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        mWebView = (WebView) findViewById(R.id.webview);
//        mWebView.getSettings().setJavaScriptEnabled(true);
////        mWebView.setWebChromeClient(new WebChromeClient());
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //这里一定要是url  不能是网页
//                view.loadUrl(url);
//                return true;
//            }
//        });
//        mWebView.loadUrl("http://www.baidu.com/");
    }
}
