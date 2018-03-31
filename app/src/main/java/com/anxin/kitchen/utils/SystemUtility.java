package com.anxin.kitchen.utils;


import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.bean.AddressListBean;
import com.anxin.kitchen.event.AddressListEvent;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.event.OnSaveBitmapEvent;
import com.anxin.kitchen.event.OnUserAcountEvent;
import com.anxin.kitchen.event.ViewUpdateHeadIconEvent;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.anxin.kitchen.MyApplication.mApp;

public class SystemUtility {
    private static final Log LOG = Log.getLog();
    public static final String PHOTO_FILE_NAME = "anxinicon.png";//用户头像名称
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
    public static String sendUserPhoneLocking() {
        return AMUAC_IP + "/v1.0/user/auto_register_login";
    }

    //注册用户
    public static String sendUserPhoneLogin(String phone, String code) {
        return AMUAC_IP + "/v1.0/user/login_code?phone=" + phone + "&code=" + code;
    }

    //添加地址
    public static String sendAddAddress() {
        return AMUAC_IP + "/v1.0/user/add_address";
    }

    //获取送餐地址
    public static String sendGetAddress() {
        return AMUAC_IP + "/v1.0/user/address_list?token=" + AMToken;
    }

    /**
     * /v1.0/system/message_list
     * 获取系统消息
     * */
    public static String getMessageListUrl() {
        return AMUAC_IP + "/v1.0/system/message_list";
    }
    /**
     * 获取省市县定位ID列表
     */
    public static String sendGetAddresshttp() {
        return "http://tapi.anxinyc.com/v1.0/system/address_list";
    }

    /**
     * 获取定位最近的厨房信息
     */
    public static String getNearKitchenId() {
        return AMUAC_IP + "/v1.0/kitchen/near";
    }

    /**
     * /v1.0/kitchen/menu
     * 菜系  推荐菜品 首页
     */
    public static String getFoodMenuUrl() {
        return AMUAC_IP + "/v1.0/food/cuisine_list";
    }

    /**
     *  /v1.0/food/package_list
     *  获取康复食疗
     * */
    public static String getRecoverList() {
        return AMUAC_IP + "/v1.0/food/package_list";
    }

    /**
     * 获取广告列表
     */
    public static String getBannerListUrl() {
        return AMUAC_IP + "/v1.0/system/banner_list";
    }


    /**
     * 首页套餐url
     */
    public static String getMenuMealUrl() {
        return AMUAC_IP + "/v1.0/kitchen/menu";
    }

    /**
     * 康复食疗菜系/v1.0/food/cuisine_list
     */
    public static String getRecoverMenuUrl() {
        return AMUAC_IP + "/v1.0/system/diet_list";
    }

    /**
     * 创建新的定参团url
     */
    public static String CreateGroup() {
        return AMUAC_IP + "/v1.0/group/create";
    }

    /**
     * 查询所有团或者指定名字的团url
     */
    public static String searchGroupUrl() {
        return AMUAC_IP + "/v1.0/group/list";
    }


    /**
     * 查询团友信息url
     */
    public static String getFriendsUrl() {
        return AMUAC_IP + "/v1.0/group/user_list";
    }


    /**
     * 删除团友url
     */
    public static String deleteFriendsUrl() {
        return AMUAC_IP + "/v1.0/group/del_user";
    }


    /**
     * 添加好友到饭团
     */
    public static String addFriendToGroupUrl() {
        return AMUAC_IP + "/v1.0/group/add_user";
    }

    /**
     * 添加多个好友到饭团
     */
    public static String addFriendsToGroupUrl() {
        return AMUAC_IP + "/v1.0/group/add_users";
    }

    /**
     * 查询饭团好友
     */
    public static String getFriendsOfGroupUrl() {
        return AMUAC_IP + "/v1.0/group/user_list";
    }

    /**
     * 删除团url
     */
    public static String deleteGroupUrl() {
        return AMUAC_IP + "/v1.0/group/del";
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
    public static void requestNetPost(String urlPath, Map<String, Object> dataMap, final String requestCode) {
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
//            Log.e("", "------requestNetPost----------" + params.toString());
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
     * Get公共方法
     * urlPath 请求地址
     * dataMap 请求参数map
     * requestCode 请求标识
     */
    public static void requestNetGet(String urlPath, final String requestCode) {
        if (null != urlPath && urlPath.length() > 0) {

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(urlPath, new AsyncHttpResponseHandler() {
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
        final Account userAccount = new Account();
        try {
            JSONObject jsonObject = new JSONObject(jason);
            String data = jsonObject.getString("data");
            //解析服务器请求密钥，toKen
            String toKen = new JSONObject(data).getString("token");
            if (toKen != null && toKen.length() != 0) {
                AMToken = toKen;
                userAccount.setUserAMToKen(toKen);
                MyApplication.getInstance().getCache().setAMToken(toKen);
            }
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
                    MyApplication.getInstance().getCache().setUserPhone(resultValue);
                } else if (resultKey.equals("password")) {
                    userAccount.setUserPassword(resultValue);
                } else if (resultKey.equals("initials")) {
                    userAccount.setUserInitials(resultValue);
                } else if (resultKey.equals("nickName")) {
                    userAccount.setUserNickName(resultValue);
                    MyApplication.getInstance().getCache().setNickName(resultValue);
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
                    if (resultValue != null && resultValue.length() != 0) {
                        EventBusFactory.getInstance().post(new OnSaveBitmapEvent(resultValue, userAccount.getUserPhone()));
                    }
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyApplication.getInstance().setAccount(userAccount);
        EventBusFactory.postEvent(new OnUserAcountEvent());
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

    /**
     * 获取全部地址信息
     */
    public static void sendGetAddressHttp() {
        String urlPath = sendGetAddress();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlPath, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = "";
                if (bytes != null) {
                    result = new String(bytes);
//                    LOG.e("----------sendGetAddressList------------" + result);
                    String code = StringUtils.parserMessage(result, "code");
                    String data = StringUtils.parserMessage(result, "data");
                    if (code != null && code.equals("1")) {
                        AddressListJason(data);
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                {
                    String result = "";
                    if (bytes != null) {
                        result = new String(bytes);
                    }

                }
            }
        });
    }

    //获取所有城市ID
    public static void sendGetAddressList() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(sendGetAddresshttp(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = "";
                if (bytes != null) {
                    result = new String(bytes);
//                    LOG.e("----------sendGetAddressList------------" + result);
                    AddressIDJason(result);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                {
                    String result = "";
                    if (bytes != null) {
                        result = new String(bytes);
                    }

                }
            }
        });
    }

    /**
     * 解析地址ID信息
     *
     * @param jason
     */
    public static void AddressIDJason(String jason) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jason);
            String data = jsonObject.getString("data");
            JSONArray jsonArrayResult2 = new JSONArray(data);
            int AccountCount2 = jsonArrayResult2.length();
            Map<String, AddressListBean> addressNameMap = new HashMap<>();
            Map<String, AddressListBean> addressIDMap = new HashMap<>();
            for (int j = 0; j < AccountCount2; j++) {
                String alarmMsg2 = jsonArrayResult2.getString(j);
                JSONObject jsonAlarm2 = new JSONObject(alarmMsg2);
                Iterator<?> it2 = jsonAlarm2.keys();
                String resultKey2 = "";
                String resultValue2 = null;
                AddressListBean addressListBean = new AddressListBean();
                while (it2.hasNext()) {
                    resultKey2 = (String) it2.next().toString();
                    resultValue2 = jsonAlarm2.getString(resultKey2).trim();
                    if (resultKey2 == null) {
                        resultKey2 = "";
                    }
                    if (resultValue2 == null) {
                        resultValue2 = "";
                    }
                    resultValue2 = resultValue2.trim();
                    if (resultKey2.equals("id")) {
                        addressListBean.setAdID(resultValue2);
                    } else if (resultKey2.equals("name")) {
                        addressListBean.setAdName(resultValue2);
                    } else if (resultKey2.equals("parentId")) {
                        addressListBean.setAdParenID(resultValue2);
                    } else if (resultKey2.equals("shortName")) {
                        addressListBean.setAdShortName(resultValue2);
                    } else if (resultKey2.equals("levelType")) {
                        addressListBean.setAdLevelType(resultValue2);
                    } else if (resultKey2.equals("cityCode")) {
                        addressListBean.setAdCityCode(resultValue2);
                    } else if (resultKey2.equals("zipCode")) {
                        addressListBean.setAdZipCode(resultValue2);
                    } else if (resultKey2.equals("lng")) {
                        addressListBean.setAdLng(resultValue2);
                    } else if (resultKey2.equals("lat")) {
                        addressListBean.setAdLat(resultValue2);
                    } else if (resultKey2.equals("pinyin")) {
                        addressListBean.setAdPinyin(resultValue2);
                    }
                }
                addressIDMap.put(addressListBean.getAdID(), addressListBean);
                addressNameMap.put(addressListBean.getAdName(), addressListBean);
            }
            mApp.setAddressIDMap(addressIDMap);
            mApp.setAddressNameMap(addressNameMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析地址信息
     *
     * @param jason
     */
    public static void AddressListJason(String jason) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jason);
            String data = jsonObject.getString("data");
            JSONArray jsonArrayResult2 = new JSONArray(data);
            int AccountCount2 = jsonArrayResult2.length();
            List<AddressBean> addressListBean = new ArrayList<>();
            for (int j = 0; j < AccountCount2; j++) {
                String alarmMsg2 = jsonArrayResult2.getString(j);
                JSONObject jsonAlarm2 = new JSONObject(alarmMsg2);
                Iterator<?> it2 = jsonAlarm2.keys();
                String resultKey2 = "";
                String resultValue2 = null;
                AddressBean addressBean = new AddressBean();
                while (it2.hasNext()) {
                    resultKey2 = (String) it2.next().toString();
                    resultValue2 = jsonAlarm2.getString(resultKey2).trim();
                    if (resultKey2 == null) {
                        resultKey2 = "";
                    }
                    if (resultValue2 == null) {
                        resultValue2 = "";
                    }
                    resultValue2 = resultValue2.trim();
                    if (resultKey2.equals("id")) {
                        addressBean.setAddressID(resultValue2);
                    } else if (resultKey2.equals("phone")) {
                        addressBean.setPhoneNumber(resultValue2);
                    } else if (resultKey2.equals("province")) {
                        addressBean.setProvinceID(resultValue2);
                    } else if (resultKey2.equals("city")) {
                        addressBean.setCityID(resultValue2);
                    } else if (resultKey2.equals("district")) {
                        addressBean.setDistrictID(resultValue2);
                    } else if (resultKey2.equals("longitude")) {
                        addressBean.setLongitude(resultValue2);
                    } else if (resultKey2.equals("latitude")) {
                        addressBean.setLatitude(resultValue2);
                    } else if (resultKey2.equals("address")) {
                        addressBean.setAddress(resultValue2);
                    } else if (resultKey2.equals("street")) {
                        addressBean.setStreetName(resultValue2);
                    } else if (resultKey2.equals("contactName")) {
                        addressBean.setContactName(resultValue2);
                    } else if (resultKey2.equals("isDefault")) {
                        addressBean.setIsDefault(resultValue2);
                    } else if (resultKey2.equals("userId")) {
                        addressBean.setUserID(resultValue2);
                    }
                }
                addressListBean.add(addressBean);
            }
            MyApplication.getInstance().setAddressBeanList(addressListBean);
            EventBusFactory.getInstance().post(new AddressListEvent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}