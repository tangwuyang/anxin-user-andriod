package com.anxin.kitchen;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by 唐午阳 on 2018/2/26.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"5a940df18f4a9d7d7b000197");
        UMConfigure.setLogEnabled(true);
        UMConfigure.setEncryptEnabled(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
}
