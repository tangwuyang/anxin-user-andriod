package com.anxin.kitchen.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
}
