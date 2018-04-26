package com.anxin.kitchen.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.bean.AddressListBean;
import com.anxin.kitchen.bean.MessageBean;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.provider.Telephony.Mms.Part.FILENAME;

/***
 * Store application state information either permanently (in a properties
 * file), or in memory for the duration of the Application class lifecycle.
 */
public class Cache {
    /**
     * Preferences file name.
     **/
    private static final String PREFS_FILE = "CACHE";

    /**
     * Preferences ID for username(Phone).
     **/
    private static final String SAVED_USERNAME = "SAVED_USER";
    /**
     * Preferences ID password.
     **/
    private static final String SAVED_PASSWORD = "SAVED_PASS";
    /**
     * Preferences ID Account.
     **/
    private static final String SAVED_ACCOUNT = "SAVED_ACCOUNT";

    /**
     * Preferences ID AddressID_NameMap.
     **/
    private static final String AddressID_NameMap = "AddressID_NameMap";
    /**
     * Preferences ID AddressID_IDMap.
     **/
    private static final String AddressID_IDMap = "AddressID_IDMap";
    /**
     * Preferences ID AddressID_IDMap.
     **/
    private static final String AddressList = "AddressList";
    /**
     * Preferences ID NickName.
     **/
    private static final String SAVED_NICKNAME = "SAVED_NICKNAME";

    /**
     * Preferences ID for remember password.
     **/
    private static final String SAVED_ACCOUNTIMAGEURI = "SAVED_ACCOUNTIMAGEURI";

    /**
     * ToKen
     */
    private static final String SAVED_AMTOKEN = "SAVED_AMTOKEN";

    private final Context mContext;
    private static final String SAVED_MESSAGE = "SAVED_MESSAGE";
    private static final String SAVED_DEFAULTADDRESS = "SAVED_DEFAULTADDRESS";

    public Cache(final Context context) {
        mContext = context;
    }

    /***
     * Get the saved username of camera
     *
     * @return user value.
     */
    public final String getUserPhone() {
        return getValue(mContext, SAVED_USERNAME, null);
    }

    /***
     * Set the username of camera
     *
     * @param value
     *            user
     */
    public final void setUserPhone(final String value) {
        setValue(mContext, SAVED_USERNAME, value);
    }

    /***
     * @return user Account.
     */
    public final Account getAcount(Context context) {
        Account account = (Account) readObject(context, SAVED_ACCOUNT);
//        Log.e("--------------", "-------getAcount---------" + account);
        if (null == account)
            return null;
        return account;
    }

    /***
     * @param acount
     *            Acount
     */
    public final void setAcount(Context context, final Account acount) {
        saveObject(context, SAVED_ACCOUNT, acount);
    }

    public final AddressBean getDefaultAddress(Context context) {
        AddressBean addressBean = (AddressBean) readObject(context, SAVED_DEFAULTADDRESS);
//        Log.e("--------------", "-------getAcount---------" + account);
        if (null == addressBean)
            return null;
        return addressBean;
    }

    public final void setSavedDefaultaddress(Context context, final AddressBean addressBean) {
        saveObject(context, SAVED_DEFAULTADDRESS, addressBean);
    }

    /***
     * @return user Account.
     */
    public final MessageBean getMessageBean(Context context, String user) {
        MessageBean messageBean = (MessageBean) readObject(context, SAVED_MESSAGE + user);
//        Log.e("--------------", "-------getAcount---------" + account);
        if (null == messageBean)
            return null;
        return messageBean;
    }

    /***
     * @param messageBean
     *            Acount
     */
    public final void setMessage(Context context, final MessageBean messageBean, String user) {
        saveObject(context, SAVED_MESSAGE + user, messageBean);
    }

    public final Map<String, AddressListBean> getAddressNameMap(Context context) {
        Map<String, AddressListBean> addressNameMap = (Map<String, AddressListBean>) readObject(context, AddressID_NameMap);
//        Log.e("--------------", "-------getAcount---------" + account);
        if (null == addressNameMap)
            return null;
        return addressNameMap;
    }

    public final void setAddressNameMap(Context context, final Map<String, AddressListBean> addressNameMap) {
        saveObject(context, AddressID_NameMap, addressNameMap);
    }

    public final Map<String, AddressListBean> getAddressIDMap(Context context) {
        Map<String, AddressListBean> addressNameMap = (Map<String, AddressListBean>) readObject(context, AddressID_IDMap);
//        Log.e("--------------", "-------getAcount---------" + account);
        if (null == addressNameMap)
            return null;
        return addressNameMap;
    }

    public final void setAddressIDMap(Context context, final Map<String, AddressListBean> addressNameMap) {
        saveObject(context, AddressID_IDMap, addressNameMap);
    }

    public final List<AddressBean> getAddressList(Context context) {
        List<AddressBean> addressNameMap = (List<AddressBean>) readObject(context, AddressList);
//        Log.e("--------------", "-------getAcount---------" + account);
        if (null == addressNameMap)
            return new ArrayList<>();
        return addressNameMap;
    }

    public final void setAddressList(Context context, final List<AddressBean> addressBeanList) {
        saveObject(context, AddressList, addressBeanList);
    }

    /***
     * Get the saved username of camera
     *
     * @return nickname value.
     */
    public final String getNickName() {
        return getValue(mContext, SAVED_NICKNAME, null);
    }

    /***
     * Set the username of camera
     *
     * @param value
     *            nickname
     */
    public final void setNickName(final String value) {
        setValue(mContext, SAVED_NICKNAME, value);
    }

    /**
     * 保存ToKen
     *
     * @param token
     */
    public void setAMToken(String token) {
        setValue(mContext, SAVED_AMTOKEN, token);
    }

    /**
     * 从本地获取ToKen
     *
     * @return
     */
    public final String getAMToken() {
        return getValue(mContext, SAVED_AMTOKEN, null);
    }

    /***
     * Get the saved password of camera
     *
     * @return password value.
     */
    public final String getPassword() {
        return getValue(mContext, SAVED_PASSWORD, null);
    }

    /***
     * Set the password of camera
     *
     * @param value
     *            password
     */
    public final void setPassword(final String value) {
        setValue(mContext, SAVED_PASSWORD, value);
    }

    private static void removeValue(final Context context, final String key, final String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE, 0).edit();
        editor.remove(key);
        if (!editor.commit()) {
            throw new NullPointerException(
                    "MainApplication.setValue() Failed to set key[" + key + "] with value[" + value + "]");
        }
    }


    /***
     * Set a value in the preferences file.
     *
     * @param context
     *            Android context.
     * @param key
     *            Preferences file parameter key.
     * @param value
     *            Preference value.
     */
    private static void setValue(final Context context, final String key, final String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE, 0).edit();
        editor.putString(key, value);
        if (!editor.commit()) {
            throw new NullPointerException(
                    "MainApplication.setValue() Failed to set key[" + key + "] with value[" + value + "]");
        }
    }

    /***
     * Get a value from the preferences file.
     *
     * @param context
     *            Android context.
     * @param key
     *            Preferences file parameter key.
     * @param defaultValue
     *            Preference value.
     * @return Value as a String.
     */
    private static String getValue(final Context context, final String key, final String defaultValue) {
        return context.getSharedPreferences(PREFS_FILE, 0).getString(key, defaultValue);
    }

    /***
     * Set a value in the preferences file.
     *
     * @param context
     *            Android context.
     * @param key
     *            Preferences file parameter key.
     * @param value
     *            Preference value.
     */
    private static void setValue(final Context context, final String key, final int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE, 0).edit();
        editor.putInt(key, value);
        if (!editor.commit()) {
            throw new NullPointerException(
                    "MainApplication.setValue() Failed to set key[" + key + "] with value[" + value + "]");
        }
    }

    /***
     * Get a value from the preferences file.
     *
     * @param context
     *            Android context.
     * @param key
     *            Preferences file parameter key.
     * @param defaultValue
     *            Preference value.
     * @return Value as a String.
     */
    private static int getValue(final Context context, final String key, final int defaultValue) {
        return context.getSharedPreferences(PREFS_FILE, 0).getInt(key, defaultValue);
    }

    /***
     * Set a value in the preferences file.
     *
     * @param context
     *            Android context.
     * @param key
     *            Preferences file parameter key.
     * @param value
     *            Preference value.
     */
    private static void setValue(final Context context, final String key, final boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE, 0).edit();
        editor.putBoolean(key, value);
        if (!editor.commit()) {
            throw new NullPointerException(
                    "MainApplication.setValue() Failed to set key[" + key + "] with value[" + value + "]");
        }
    }

    /***
     * Get a value from the preferences file.
     *
     * @param context
     *            Android context.
     * @param key
     *            Preferences file parameter key.
     * @param defaultValue
     *            Preference value.
     * @return Value as a Boolean
     */
    private static boolean getValue(final Context context, final String key, final boolean defaultValue) {
        return context.getSharedPreferences(PREFS_FILE, 0).getBoolean(key, defaultValue);
    }

    public String getAccountImageURI(String userName) {
        return getValue(mContext, userName + SAVED_ACCOUNTIMAGEURI, null);
    }

    public void setAccountImageURI(String userName, String path) {
        setValue(mContext, userName + SAVED_ACCOUNTIMAGEURI, path);

    }

    /**
     * desc:保存对象
     *
     * @param context
     * @param key
     * @param obj     要保存的对象，只能保存实现了serializable的对象
     *                modified:
     */
    public static void saveObject(Context context, String key, Object obj) {
        try {
            // 保存对象
            SharedPreferences.Editor sharedata = context.getSharedPreferences(FILENAME, 0).edit();
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            sharedata.putString(key, bytesToHexString);
            sharedata.commit();
        } catch (IOException e) {
            MobclickAgent.reportError(MyApplication.getInstance(), e);
            Log.e("", "保存obj失败");
        }
    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return modified:
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * desc:获取保存的Object对象
     *
     * @param context
     * @param key
     * @return modified:
     */
    public Object readObject(Context context, String key) {
        try {
            SharedPreferences sharedata = context.getSharedPreferences(FILENAME, 0);
            if (sharedata.contains(key)) {
                String string = sharedata.getString(key, "");
                if (TextUtils.isEmpty(string)) {
                    return null;
                } else {
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = toBytes(string);
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    Object readObject = is.readObject();
                    return readObject;
                }
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        }
        //所有异常返回null
        return null;

    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }
}
