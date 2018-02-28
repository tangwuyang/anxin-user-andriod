package com.anxin.kitchen.utils;

import android.app.Application;
import android.content.Context;

import com.anxin.kitchen.user.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.text.SimpleDateFormat;

public class MainApplication extends Application {
    private static final Log LOG = Log.getLog();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    public static MainApplication mApp;
    private Cache mCache;

    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static MainApplication getInstance() {
        return mApp;
    }

    public final synchronized Cache getCache() {
        if (mCache == null) {
            mCache = new Cache(this);
        }
        return mCache;
    }
    @Override
    public final void onTerminate() {
        mCache = null;
        super.onTerminate();
    }
}
