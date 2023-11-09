package com.example.native_test_channel;

import android.app.Application;

/**
 * 自定义Application
 *
 * @author zhangbin
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
