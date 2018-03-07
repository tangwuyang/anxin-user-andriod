package com.anxin.kitchen.utils;


import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SystemUtility {

    public static String AMUAC_IP = "http://tapi.anxinyc.com";//服务器请求，IP地址
    public static String AMToken;//网络请求ToKen
    public static String RequestSuccess = "onSuccess";//http方法成功返回标志
    public static String RequestFailure = "onFailure";//http方法失败返回标志

    //发送手机验证码
    public static String sendUserPhoneCode(String phone, String type) {
        return AMUAC_IP + "/v1.0/user/send_code?phone=" + phone + "&type=" + type;
    }

    //注册用户
    public static String sendUserPhoneregister(String phone, String code) {
        return AMUAC_IP + "/v1.0/user/auto_register_login?formData={phone:" + phone + "}&code=" + code;
    }

    //注册用户
    public static String sendUserPhoneLogin(String phone, String code) {
        return AMUAC_IP + "/v1.0/user/login_code?phone=" + phone + "&code=" + code;
    }


    /**
     * 获取定位最近的厨房信息
     * */
    public static String getNearKitchenId() {
        return AMUAC_IP + "/v1.0/kitchen/near";
    }

    /**
     * 获取广告列表
     * */
    public static String getBannerListUrl() {
        return AMUAC_IP + "/v1.0/system/banner_list";
    }


    /**
     * 首页套餐url
     * */
    public static String getMenuMealUrl(){
        return AMUAC_IP + "/v1.0/kitchen/menu";
    }
    //第三方登陆，注册
    public static String sendUserLogin3(String platId, String sourceCode) {
        return AMUAC_IP + "/v1.0/user/login3?platId=" + platId + "&sourceCode=" + sourceCode + "&formData={}";
    }
//请求网络异步方法

    /**
     * post公共方法
     * urlPath 请求地址
     * dataMap 请求参数map
     * requestCode 请求标识
     */
    public static void requestNet(String urlPath, Map<String, Object> dataMap, final String requestCode) {
        if (null != urlPath && urlPath.length() > 0) {

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            if (null != dataMap) {
                Set<String> paraNames = dataMap.keySet();
                for (String para :
                        paraNames) {
                    params.put(para, dataMap.get(para));
                }
            }
            client.post(urlPath, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String result = "";
                    if (bytes != null) {
                        result = new String(bytes);
                        EventBusFactory.getInstance().post(new AsyncHttpRequestMessage(requestCode, result, RequestSuccess));
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    {
                        String result = "";
                        if (bytes != null) {
                            result = new String(bytes);
                            EventBusFactory.getInstance().post(new AsyncHttpRequestMessage(requestCode, result, RequestFailure));
                        }

                    }
                }
            });


        }
    }

    /**
     * 解析用户信息
     *
     * @param jason
     */
    public static Account loginAnalysisJason(String jason) {
        Account userAccount = new Account();
        try {
            JSONObject jsonObject = new JSONObject(jason);
            String data = jsonObject.getString("data");
            //解析用户信息
            String user = new JSONObject(data).getString("user");
            JSONObject userJason = new JSONObject(user);
            Iterator<?> it1 = userJason.keys();
            String resultKey = "";
            String resultValue = null;
            while (it1.hasNext()) {
                resultKey = (String) it1.next().toString();
                resultValue = userJason.getString(resultKey);
                if (resultKey == null) {
                    resultKey = "";
                }
                if (resultValue == null) {
                    resultValue = "";
                }
                if (resultKey.equals("id")) {
                    userAccount.setUserID(resultValue);
                } else if (resultKey.equals("size")) {
                    userAccount.setUserSize(resultValue);
                } else if (resultKey.equals("status")) {
                    userAccount.setUserStatus(resultValue);
                } else if (resultKey.equals("sex")) {
                    userAccount.setUserSex(resultValue);
                } else if (resultKey.equals("phone")) {
                    userAccount.setUserPhone(resultValue);
                    MyApplication.getInstance().getCache().setUsername(resultValue);
                } else if (resultKey.equals("password")) {
                    userAccount.setUserPassword(resultValue);
                } else if (resultKey.equals("initials")) {
                    userAccount.setUserInitials(resultValue);
                } else if (resultKey.equals("nickName")) {
                    userAccount.setUserNickName(resultValue);
                } else if (resultKey.equals("trueName")) {
                    userAccount.setUserTrueName(resultValue);
                } else if (resultKey.equals("weixin")) {
                    userAccount.setUserWeiXin(resultValue);
                } else if (resultKey.equals("num")) {
                    userAccount.setUserNum(resultValue);
                } else if (resultKey.equals("money")) {
                    userAccount.setUserMoney(resultValue);
                } else if (resultKey.equals("deposit")) {
                    userAccount.setUserDeposit(resultValue);
                } else if (resultKey.equals("alipay")) {
                    userAccount.setUserAlipay(resultValue);
                } else if (resultKey.equals("birthdayTime")) {
                    userAccount.setUserBirthdayTime(resultValue);
                } else if (resultKey.equals("userLogo")) {
                    userAccount.setUserLogoPathURL(resultValue);
                } else if (resultKey.equals("addressNum")) {
                    userAccount.setUserAddressNum(Integer.valueOf(resultValue));
                } else if (resultKey.equals("groupUserNum")) {
                    userAccount.setUserGroupUserNum(Integer.valueOf(resultValue));
                } else if (resultKey.equals("updateTime")) {
                    userAccount.setUserUpdateTime(new Long(resultValue));
                } else if (resultKey.equals("lastLoginTime")) {
                    userAccount.setUserLastLoginTime(new Long(resultValue));
                } else if (resultKey.equals("createTime")) {
                    userAccount.setUserCreateTime(new Long(resultValue));
                }
            }
            //解析服务器请求密钥，toKen
            String toKen = new JSONObject(data).getString("token");
            if (toKen != null && toKen.length() != 0) {
                AMToken = toKen;
                userAccount.setUserAMToKen(toKen);
                MyApplication.getInstance().getCache().setAMToken(toKen);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyApplication.getInstance().setAccount(userAccount);
        return userAccount;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
        String telRegex = "[1][3456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    //设置item高度
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + 15;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static MySSLSocketFactory getMySSLSocketFactory() {
        MySSLSocketFactory sf = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        } catch (UnsupportedEncodingException e) {
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return sf;
    }

}