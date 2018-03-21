package com.anxin.kitchen;

import android.support.multidex.MultiDexApplication;


import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.bean.AddressListBean;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Log;
import com.umeng.analytics.MobclickAgent;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 唐午阳 on 2018/2/26.
 */

public class MyApplication extends MultiDexApplication {

    private static final Log LOG = Log.getLog();
    public static MyApplication mApp;
    private Cache mCache;
    private Account mAccount = null;//用户信息
    private Map<String, AddressListBean> addressNameMap = new HashMap<>();//跟据名称获取地址信息
    private Map<String, AddressListBean> addressIDMap = new HashMap<>();//根据ID获取地址信息

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

    /**
     * 缓存地址名称获取ID
     *
     * @param addressNameMap
     */
    public final synchronized void setAddressNameMap(Map<String, AddressListBean> addressNameMap) {
        this.addressNameMap = addressNameMap;
//        mCache.setAcount(this, account);
    }

    public final synchronized Map<String, AddressListBean> getAddressNameMap() {
        return addressNameMap;
    }

    public String getAddressNameToID(String addressName) {
        String addressID = null;
        if (addressNameMap != null && addressNameMap.size() != 0) {
            AddressListBean addressListBean = addressNameMap.get(addressName);
            addressID = addressListBean.getAdID();
            if (addressID != null && addressID.length() != 0)
                return addressID;
        }
        return addressID;
    }

    /**
     * 缓存地址ID获取名称
     *
     * @param addressIDMap
     */
    public final synchronized void setAddressIDMap(Map<String, AddressListBean> addressIDMap) {
        this.addressIDMap = addressIDMap;
//        mCache.setAcount(this, account);
    }

    public final synchronized Map<String, AddressListBean> getAddressIDMap() {
        return addressIDMap;
    }

    public String getAddressIDToName(String addressID) {
        String addressName = null;
        if (addressIDMap != null && addressIDMap.size() != 0) {
            AddressListBean addressListBean = addressIDMap.get(addressID);
            addressName = addressListBean.getAdName();
            if (addressName != null && addressName.length() != 0)
                return addressName;
        }
        return addressName;
    }

    @Override
    public final void onTerminate() {
        mCache = null;
        super.onTerminate();
    }
}
