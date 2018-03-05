package com.anxin.kitchen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.user.wxapi.WXEntryActivity;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;


/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private ImageView WXloginBtn;//微信登陆按钮
    private IWXAPI mApi;//微信登陆API
    public static String openID;//第三方登陆ID
    public static String userNickName;//第三方登陆名称
    public static String userLogoPath;//第三方登陆用户头像URL
    private TextView sendPhoneCodeBtn;//发送验证码
    private EditText userPhoneEdit, phoneCodeEdit;//手机号码和验证码输入
    private int number = 60;
    private MyCountDownTimer mc;//验证码倒计时
    private Button loginBtn;//登陆按钮
    private String loginData = null;//用户是否注册判断
    private String userPhone;//用户号码
    private String phoneCode;//验证码
    private MyApplication mApp;//
    private String platId = "0";//第三方登录标志：1微信，2QQ
    private LinearLayout thirdPartyLogin_lyt;//第三方登陆模块
    private TextView titleCenterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EventBusFactory注册
        EventBusFactory.getInstance().register(this);
        setContentView(R.layout.activity_login);
        if (mApp == null) {
            mApp = (MyApplication) getApplication();
        }
        initView();
    }

    /**
     * 初始化绑定控件
     */
    private void initView() {
        ImageView back_btn = (ImageView) findViewById(R.id.back_btn);//返回按钮
        back_btn.setOnClickListener(this);
        WXloginBtn = (ImageView) findViewById(R.id.wx_login);//微信登陆按钮
        WXloginBtn.setOnClickListener(this);
        sendPhoneCodeBtn = (TextView) findViewById(R.id.sendPhoneCode);//获取验证码按钮
        sendPhoneCodeBtn.setOnClickListener(this);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        userPhoneEdit = (EditText) findViewById(R.id.userPhone_edit);//手机号码输入框
        phoneCodeEdit = (EditText) findViewById(R.id.phoneCode_edit);//验证码输入框
        mc = new MyCountDownTimer(1000 * 60, 1000);//验证码倒计时
        thirdPartyLogin_lyt = (LinearLayout) findViewById(R.id.third_party_login_bottom);//第三方登录按钮模块
        titleCenterName = (TextView) findViewById(R.id.title_center_name);//标题名称
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.wx_login://微信登陆
                platId = "1";
                loginToWeiXin();
                break;
            case R.id.sendPhoneCode://发送手机验证码
                userPhone = userPhoneEdit.getText().toString();
                if (userPhone == null || userPhone.length() <= 0) {
                    ToastUtil.showToast("请输入手机号码");
                    return;
                }
                if (!SystemUtility.isMobileNO(userPhone)) {
                    ToastUtil.showToast("请输入正确的手机号码");
                    return;
                }
                sendPhoneCode(userPhone, "1");//发送验证码
                mc.start();//开启倒计时
                break;
            case R.id.loginBtn:
                phoneCode = phoneCodeEdit.getText().toString();
                if (phoneCode == null || phoneCode.length() <= 0) {
                    ToastUtil.showToast("请输入验证码");
                    return;
                }
                if (loginData != null) {
                    sendPhoneLogin(userPhone, phoneCode, loginData);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
//        LOG.e("----------onResume-----------");
        if (platId.equals("1")) {
//            LOG.e("----------wxID----------" + openID);
//            LOG.e("----------nickName----------" + userNickName);
//            LOG.e("----------userLogo----------" + userLogoPath);
            sendLogin3(platId, openID);
        }
        super.onResume();
    }

    /**
     * 监听网络请求返回
     * @param asyncHttpRequestMessage
     */
    public void onEventMainThread(AsyncHttpRequestMessage asyncHttpRequestMessage) {
        String requestCode = asyncHttpRequestMessage.getRequestCode();
        String responseMsg = asyncHttpRequestMessage.getResponseMsg();
        String requestStatus = asyncHttpRequestMessage.getRequestStatus();
        LOG.e("----------requestCode------" + requestCode);
        LOG.e("----------responseMsg------" + responseMsg);
        LOG.e("----------requestStatus------" + requestStatus);
    }

    /**
     * 发送验证码
     *
     * @param userPhone
     * @param type
     */
    private void sendPhoneCode(final String userPhone, final String type) {
        String urlPath = SystemUtility.sendUserPhoneCode(userPhone, type);
        SystemUtility.requestNet(urlPath, null, "sendUserPhoneCode");
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(urlPath, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String result = "";
//                if (responseBody != null)
//                    result = new String(responseBody);
//                /**
//                 * 解析验证码返回
//                 */
////                LOG.e("--------sendPhoneCode--onSuccess--" + result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    String code = jsonObject.getString("code");
//                    String data = jsonObject.getString("data");
//                    if (code != null && code.equals("1")) {
//                        if (data != null && !data.equals("null"))
//                            loginData = data;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//            }
//        });
    }

    /**
     * 第三方登录,注册
     *
     * @param platId     平台ID 1微信 2QQ
     * @param sourceCode 第三方id
     */
    private void sendLogin3(final String platId, final String sourceCode) {
        String urlPath = SystemUtility.sendUserLogin3(platId, sourceCode);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlPath, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = "";
                if (responseBody != null)
                    result = new String(responseBody);
                LOG.e("--------sendLogin3--onSuccess--" + result);
                /**
                 * 解析第三方登录
                 */
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String data = jsonObject.getString("data");
                    if (code != null && code.equals("305")) {
                        if (data != null && !data.equals("null")) {
                            thirdPartyLogin_lyt.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String result = "";
                if (responseBody != null)
                    result = new String(responseBody);
                LOG.e("--------sendLogin3--onFailure--" + result);
            }
        });
    }

    /**
     * 用户验证码登录,注册
     *
     * @param userPhone
     * @param code
     */
    private void sendPhoneLogin(final String userPhone, final String code, String loginData) {
        String urlPath = "";
        if (loginData.equals("1")) {
            urlPath = SystemUtility.sendUserPhoneLogin(userPhone, code);
        } else if (loginData.equals("0"))
            urlPath = SystemUtility.sendUserPhoneregister(userPhone, code);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlPath, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = "";
                if (responseBody != null)
                    result = new String(responseBody);
                LOG.d("--------sendPhoneLogin--onSuccess--" + result);
                Account account = SystemUtility.loginAnalysisJason(result);
                LOG.d("--------sendPhoneLogin--Account--" + account.toString());
                LOG.d("--------sendPhoneLogin--token--" + SystemUtility.AMToken);
                ToastUtil.showToast("登陆成功");
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                String result = "";
//                if (responseBody != null)
//                    result = new String(responseBody);
//                LOG.e("--------sendPhoneLogin--onFailure--" + result);
            }
        });
    }

    /**
     * 解析用户信息
     *
     * @param jason
     */

    /**
     * 微信登陆
     */
    private void loginToWeiXin() {
        mApi = WXAPIFactory.createWXAPI(this, WXEntryActivity.WEIXIN_APP_ID, true);
        mApi.registerApp(WXEntryActivity.WEIXIN_APP_ID);
        if (mApi != null && mApi.isWXAppInstalled()) {
            // 发送授权登录信息，来获取code
            SendAuth.Req req = new SendAuth.Req();
            // 应用的作用域，获取个人信息
            req.scope = "snsapi_userinfo";
            /**
             * 用于保持请求和回调的状态，授权请求后原样带回给第三方
             * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
             */
            req.state = "wechat_sdk_demo_test";
            mApi.sendReq(req);
        } else {
            platId = "0";
            ToastUtil.showToast("请安装微信");
        }
    }

    /**
     * 继承 CountDownTimer 防范
     * <p>
     * 重写 父类的方法 onTick() 、 onFinish()
     */

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            number = 60;
            sendPhoneCodeBtn.setText("获取验证码");
            sendPhoneCodeBtn.setTextColor(getColor(R.color.login_code_send));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            number--;
            sendPhoneCodeBtn.setText(number + "s后重新发送");
            sendPhoneCodeBtn.setTextColor(getColor(R.color.login_code_count_down));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mc != null)
            mc.cancel();
        //EventBusFactory销毁
        EventBusFactory.getInstance().unregister(this);
    }
}

