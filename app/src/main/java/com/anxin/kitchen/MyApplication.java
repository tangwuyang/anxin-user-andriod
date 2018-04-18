package com.anxin.kitchen;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.bean.AddressListBean;
import com.anxin.kitchen.bean.MessageBean;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.UmengHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 唐午阳 on 2018/2/26.
 */

public class MyApplication extends MultiDexApplication {

    private static final Log LOG = Log.getLog();
    public static MyApplication mApp;
    private Cache mCache;
    private Account mAccount = null;//用户信息
    private MessageBean mMessagebean = null;//用户消息
    private Map<String, AddressListBean> addressNameMap = new HashMap<>();//跟据名称获取地址信息
    private Map<String, AddressListBean> addressIDMap = new HashMap<>();//根据ID获取地址信息
    private List<AddressBean> addressBeanList = new ArrayList<>();//送餐地址信息

//    {
//        PlatformConfig.setWeixin("wx634128d6db8c15cd", "75bf85878994d867fd4bcec28ccca5aa");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");  //还未获取到
//
//    }

    @Override
    public void onCreate() {
        super.onCreate();
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5a940df18f4a9d7d7b000197");
       /* UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"5a940df18f4a9d7d7b000197");
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin("wx634128d6db8c15cd", "75bf85878994d867fd4bcec28ccca5aa");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");  //还未获取到
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);*/
//        UMShareAPI.get(this);
        mApp = this;
        SystemUtility.AMToken = getCache().getAMToken();
        mAccount = getCache().getAcount(this);
        addressNameMap = getCache().getAddressNameMap(this);
        addressIDMap = getCache().getAddressIDMap(this);
        addressBeanList = getCache().getAddressList(this);
        initImageLoader(getApplicationContext());
        UmengHelper.getInstance().init();
        String carrier = android.os.Build.MANUFACTURER;
        LOG.d("-----------carrier------" + carrier);
        if (carrier != null && carrier.equals("Xiaomi")) {//小米推送
            LOG.d("-------------Xiaomi-------");
            MiPushRegistar.register(getInstance(), "2882303761517755809", "5341775518809");
        } else if (carrier != null && carrier.equals("HUAWEI")) {//华为推送
            LOG.d("-------------HUAWEI-------");
            HuaWeiRegister.register(getInstance());
        } else if (carrier != null && carrier.equals("Meizu")) {//魅族推送
            LOG.d("-------------Meizu-------");
            MeizuRegister.register(getInstance(), "", "");
        }
    }

    private void initImageLoader(Context applicationContext) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(applicationContext)
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
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
//        LOG.e("-----------setAccount------------" + mAccount);
        mCache.setAcount(this, account);
    }

    public final synchronized Account getAccount() {
//        LOG.e("-----------getAccount------------" + mAccount);
        return mAccount;
    }

    /**
     * 缓存用户信息
     *
     * @param messageList
     */
    public final synchronized void setMessageList(MessageBean messageList) {
        this.mMessagebean = messageList;
        if (getCache().getUserPhone() != null)
            mCache.setMessage(this, mMessagebean, getCache().getUserPhone());
    }

    public final synchronized MessageBean getMessageList() {
        if (getCache().getUserPhone() != null)
            mMessagebean = getCache().getMessageBean(this, getCache().getUserPhone());
        return mMessagebean;
    }

    /**
     * 缓存地址名称获取ID
     *
     * @param addressNameMap
     */
    public final synchronized void setAddressNameMap(Map<String, AddressListBean> addressNameMap) {
        this.addressNameMap = addressNameMap;
        mCache.setAddressNameMap(this, addressNameMap);
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

    public void setAddressBeanList(List<AddressBean> addressBeanList) {
        this.addressBeanList = addressBeanList;
        mCache.setAddressList(this, addressBeanList);
    }

    public List<AddressBean> getAddressBeanList() {
        return addressBeanList;
    }

    /**
     * 缓存地址ID获取名称
     *
     * @param addressIDMap
     */
    public final synchronized void setAddressIDMap(Map<String, AddressListBean> addressIDMap) {
        this.addressIDMap = addressIDMap;
        mCache.setAddressIDMap(this, addressIDMap);
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
