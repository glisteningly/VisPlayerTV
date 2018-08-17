package com.example.arex.mytvtest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ClientWebViewFragment extends WebViewFragment {
    public static ClientWebViewFragment build(String url) {
        ClientWebViewFragment fragment = new ClientWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);

        return fragment;
    }

    public String getUrl() {

        return getArguments().getString("url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        WebView webView = (WebView) super.onCreateView(inflater, container, savedInstanceState);

        if (!rotated()) {

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setInitialScale(100);

            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });

            webView.loadUrl(getUrl());
        }

        return webView;
    }
}