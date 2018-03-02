package com.anxin.kitchen;

import android.app.Application;
import android.support.multidex.MultiDexApplication;


import com.umeng.analytics.MobclickAgent;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by 唐午阳 on 2018/2/26.
 */

public class MyApplication extends MultiDexApplication {
    {   Config.DEBUG = true;
        PlatformConfig.setWeixin("wx634128d6db8c15cd","75bf85878994d867fd4bcec28ccca5aa");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");  //还未获取到

    }
    @Override
    public void onCreate() {
        super.onCreate();
        Config.DEBUG = true;
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"5a940df18f4a9d7d7b000197");
       /* UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"5a940df18f4a9d7d7b000197");
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin("wx634128d6db8c15cd","75bf85878994d867fd4bcec28ccca5aa");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");  //还未获取到
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);*/
        UMShareAPI.get(this);
    }
}
