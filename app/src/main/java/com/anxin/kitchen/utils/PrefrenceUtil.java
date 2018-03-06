package com.anxin.kitchen.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 唐午阳 on 2018/3/6.
 * 用来存储，获取放在sharepreference中的值
 */

public class PrefrenceUtil {
    private Context mContext = null;
    public static final String PREFRENCE_NAEM = "ANXIN_ANDROID";

    public PrefrenceUtil(Context mContext) {
        this.mContext = mContext;
    }

    private SharedPreferences getPreference(){
        SharedPreferences preferences = mContext.getSharedPreferences(PREFRENCE_NAEM,Context.MODE_PRIVATE);
        return preferences;
    }

    public void putKitchenId(int kitchenId){
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putInt(Constant.KITCHEN_ID,kitchenId);
    }

    public int getKitchenId(){
        SharedPreferences preferences =  getPreference();
        int kitchentId = preferences.getInt(Constant.KITCHEN_ID,0);
        return kitchentId;
    }
}
