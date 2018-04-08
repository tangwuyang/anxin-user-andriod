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
        editor.commit();
    }

    public int getKitchenId(){
        SharedPreferences preferences =  getPreference();
        int kitchentId = preferences.getInt(Constant.KITCHEN_ID,0);
        return kitchentId;
    }

    public void putFrinends(String friendsString){
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(Constant.FRIENDSSTRING,friendsString);
        editor.commit();
    }

    public String getFriends(){
        SharedPreferences preferences =  getPreference();
        String frindsSt = preferences.getString(Constant.FRIENDSSTRING,"null");
        return frindsSt;
    }

    public void putGroups(String friendsString){
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(Constant.GROUPSSTRING,friendsString);
        editor.commit();
    }

    public String getGroups(){
        SharedPreferences preferences =  getPreference();
        String groupSt = preferences.getString(Constant.GROUPSSTRING,"null");
        return groupSt;
    }


    //康复食疗缓存
    public void setRecoverList(String recoverBeanListSt){
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(Constant.RECAVOR,recoverBeanListSt);
        editor.commit();
    }

    public String getRecoverList(){
        SharedPreferences preferences =  getPreference();
        String groupSt = preferences.getString(Constant.RECAVOR,"null");
        return groupSt;
    }

    public void setRecoverMenuList(String recoverBeanListSt){
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(Constant.RECAVOR_MENU,recoverBeanListSt);
        editor.commit();
    }

    public String getRecoverMenu(){
        SharedPreferences preferences =  getPreference();
        String groupSt = preferences.getString(Constant.RECAVOR_MENU,"null");
        return groupSt;
    }
}
