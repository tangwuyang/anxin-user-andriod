package com.anxin.kitchen;

import android.content.Context;
import android.support.multidex.MultiDexApplication;


import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Log;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by 唐午阳 on 2018/2/26.
 */

public class MyApplication extends MultiDexApplication {

    private static final Log LOG = Log.getLog();
    public static MyApplication mApp;
    private Cache mCache;
    private Account mAccount = null;//用户信息

    {
        Config.DEBUG = true;
        PlatformConfig.setWeixin("wx634128d6db8c15cd", "75bf85878994d867fd4bcec28ccca5aa");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");  //还未获取到

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Config.DEBUG = true;
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5a940df18f4a9d7d7b000197");
       /* UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"5a940df18f4a9d7d7b000197");
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin("wx634128d6db8c15cd", "75bf85878994d867fd4bcec28ccca5aa");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");  //还未获取到
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);*/
        UMShareAPI.get(this);
        mApp = this;
        mAccount = getCache().getAcount(this);

        initImageLoader(getApplicationContext());
    }

    private void initImageLoader(Context applicationContext) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(applicationContext)
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY-2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(configuration);
    }

    public static MyApplication getInstance() {
        return mApp;
    }

    public final synchronized Cache getCache() {
        if (mCache == null) {
            mCache = new Cache(this);
        }
        return mCache;
    }

    /**
     * 缓存用户信息
     *
     * @param account
     */
    public final synchronized void setAccount(Account account) {
        this.mAccount = account;
        mCache.setAcount(this, account);
    }

    public final synchronized Account getAccount() {
        return mAccount;
    }

    @Override
    public final void onTerminate() {
        mCache = null;
        super.onTerminate();
    }
}
