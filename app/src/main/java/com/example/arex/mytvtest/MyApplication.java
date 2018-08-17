package com.example.arex.mytvtest;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashManager crashHandler = new CrashManager(this);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }
}
