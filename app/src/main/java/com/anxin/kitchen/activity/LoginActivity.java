package com.anxin.kitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.fragment.loginfragment.AddUserDataFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.user.wxapi.WXEntryActivity;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.utils.UmengHelper;
import com.anxin.kitchen.view.WaitingDialog;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 登陆界面
 */
public class LoginActivity extends FragmentActivity implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private ImageView WXloginBtn;//微信登陆按钮
    private IWXAPI mApi;//微信登陆API
    public static String openID = null;//第三方登陆ID
    public static String userNickName;//第三方登陆名称
    public static String userLogoPath;//第三方登陆用户头像URL
    private TextView sendPhoneCodeBtn;//发送验证码
    private EditText userPhoneEdit, phoneCodeEdit;//手机号码和验证码输入
    private int number = 60;
    private MyCountDownTimer mc;//验证码倒计时
    private Button loginBtn;//登陆按钮
    private String loginData = "1";//用户是否注册判断
    private String userPhone;//用户号码
    private String phoneCode;//验证码
    private MyApplication mApp;//
    private String platId = "0";//第三方登录标志：1微信，2QQ
    private LinearLayout thirdPartyLogin_lyt;//第三方登陆模块
    private TextView titleCenterName;
    private boolean isLoginMain = true;//是否在登录主界面
    public boolean tag = false;  //是否是由唐午阳操作的标志位
    /**
     * http请求标志
     */
    private static final String sendUserPhoneCode_http = "sendUserPhoneCode";
    private static final String sendUserLogin3_http = "sendUserLogin3";
    private static final String sendUserPhoneRegister_http = "sendUserPhoneRegister";
    private static final String sendUserPhoneLogin_http = "sendUserPhoneLogin";
    private static final String sendUserPhoneLocking_http = "sendUserPhoneLocking";
    private WaitingDialog mWaitingDiag;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EventBusFactory注册
        EventBusFactory.getInstance().register(this);
        setContentView(R.layout.activity_login);
        if (mApp == null) {
            mApp = (MyApplication) getApplication();
        }

        Intent intent = getIntent();
        tag = intent.getBooleanExtra("tag", false);
        initView();
    }

    /**
     * 初始化绑定控件
     */
    private void initView() {
         back_btn = (ImageView) findViewById(R.id.back_btn);//返回按钮
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { //表示按返回键 时的操作
                // 监听到返回按钮点击事件
                if (isLoginMain) {
                    if (tag) {
                        finishToLastActivity(188);
                    } else {
                        finish();
                    }
                } else {
                    return false;
//                    isLoginMain = true;
//                    thirdPartyLogin_lyt.setVisibility(View.VISIBLE);
//                    titleCenterName.setText("登录注册");
//                    loginBtn.setText("登录");
//                    userPhoneEdit.setText("");
//                    phoneCodeEdit.setText("");
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn://返回
                if (isLoginMain) {
                    if (tag) {
                        finishToLastActivity(188);
                    } else {
                        finish();
                    }
                } else {
                    break;
//                    isLoginMain = true;
//                    thirdPartyLogin_lyt.setVisibility(View.VISIBLE);
//                    titleCenterName.setText("登录注册");
//                    loginBtn.setText("登录");
//                    userPhoneEdit.setText("");
//                    phoneCodeEdit.setText("");
                }
                break;
            case R.id.wx_login://微信登陆
                platId = "1";
                loginToWeiXin();
                break;
            case R.id.sendPhoneCode://发送手机验证码
                if (number != 60)
                    return;
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
                userPhone = userPhoneEdit.getText().toString();
                if (userPhone == null || userPhone.length() <= 0) {
                    ToastUtil.showToast("请输入手机号码");
                    return;
                }
                phoneCode = phoneCodeEdit.getText().toString();
                if (phoneCode == null || phoneCode.length() <= 0) {
                    ToastUtil.showToast("请输入验证码");
                    return;
                }
                if (isLoginMain) {
                    if (loginData != null) {
                        sendPhoneLogin(userPhone, phoneCode, loginData);
                    }
                } else {
                    sendUserPhoneLocking(userPhone, phoneCode);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        if (isLoginMain) {
            thirdPartyLogin_lyt.setVisibility(View.VISIBLE);
            titleCenterName.setText("登录注册");
            loginBtn.setText("登录");
        } else {
            thirdPartyLogin_lyt.setVisibility(View.GONE);
            titleCenterName.setText("绑定手机号码");
            loginBtn.setText("绑定");
            back_btn.setVisibility(View.GONE);
        }
        if (platId.equals("1")) {
            if (openID != null && openID.length() != 0) {
                showDialog();
                sendLogin3(platId, openID);
            }

        }
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void showDialog() {
        mWaitingDiag = new WaitingDialog(this, 1000 * 20);
        mWaitingDiag.show();
        mWaitingDiag.startAnimation();
    }

    /**
     * 监听网络请求返回
     *
     * @param asyncHttpRequestMessage
     */
    public void onEventMainThread(AsyncHttpRequestMessage asyncHttpRequestMessage) {
        String requestCode = asyncHttpRequestMessage.getRequestCode();
        String responseMsg = asyncHttpRequestMessage.getResponseMsg();
        String requestStatus = asyncHttpRequestMessage.getRequestStatus();
        String codeToKen = StringUtils.parserMessage(responseMsg, "code");
//        LOG.d("----------requestCode------" + requestCode);
//        LOG.d("----------responseMsg------" + responseMsg);
//        LOG.d("----------requestStatus------" + requestStatus);
        switch (requestCode) {
            //验证码发送
            case sendUserPhoneCode_http:
                //网络请求返回成功
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    //解析验证码返回
                    String code = StringUtils.parserMessage(responseMsg, "code");
                    String data = StringUtils.parserMessage(responseMsg, "data");
                    if (code != null && code.equals("1")) {
                        if (data != null && !data.equals("null"))
                            ToastUtil.showToast("发送成功");
                        loginData = data;
                    } else if (code.equals("321")) {
                        ToastUtil.showToast("请求次数超限");
                    }
                }
                break;
            //验证码登陆
            case sendUserPhoneLogin_http:
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    LoginMessageAnalysis(responseMsg);

                }
                break;
            //验证码注册
            case sendUserPhoneRegister_http:
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    LoginMessageAnalysis(responseMsg);
                }
                break;
            //第三方登陆
            case sendUserLogin3_http:
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    //解析验证码返回
                    String code = StringUtils.parserMessage(responseMsg, "code");
                    if (code != null && code.equals("305")) {
                        isLoginMain = false;
                        thirdPartyLogin_lyt.setVisibility(View.GONE);
                        titleCenterName.setText("绑定手机号码");
                        loginBtn.setText("绑定");
                        if (null != mWaitingDiag) {
                            mWaitingDiag.stopAnimation();
                            mWaitingDiag.dismiss();
                        }
                    } else if (code != null && code.equals("1")) {
                        LoginMessageAnalysis(responseMsg);
                    }
                }
                break;
            case sendUserPhoneLocking_http:
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    LoginMessageAnalysis(responseMsg);
                }
                break;
        }
    }

    //解析登陆
    private void LoginMessageAnalysis(String Msg) {
        String code = StringUtils.parserMessage(Msg, "code");
        if (code != null && code.equals("1")) {
            //解析验证码返回
            Account account = SystemUtility.loginAnalysisJason(Msg, "");
//            LOG.e("--------sendPhoneLogin--Account--" + account.toString());
//            LOG.d("--------sendPhoneLogin--token--" + SystemUtility.AMToken);
            if (account != null) {
                String trueName = account.getUserTrueName();
                String userId = account.getUserID();
                sendBindPhoneReport();
                if (userId != null && userId.length() != 0)
                    UmengHelper.getInstance().setUserAlias(userId);
                if (null != trueName && !trueName.equals("暂无") && !trueName.equals("null")) {//登陆成功且用户信息不为空
//                    ToastUtil.showToast("登陆成功");
                    if (null != mWaitingDiag) {
                        mWaitingDiag.stopAnimation();
                        mWaitingDiag.dismiss();
                    }
                    if (tag) {
                        finishToLastActivity(201);
                    } else {
                        finish();
                    }
                } else {//登陆成功需要填写用户信息
                    platId = "0";
                    isLoginMain = true;
                    AddUserDataFragment addUserDataFragment = new AddUserDataFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, addUserDataFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    if (null != mWaitingDiag) {
                        mWaitingDiag.stopAnimation();
                        mWaitingDiag.dismiss();
                    }
                    if (mc != null)
                        mc.cancel();
                }
            }
        }
    }

    public void finishToLastActivity(int resultCode) {
        Intent intent = getIntent();
        intent.putExtra("loginTag", true);
        setResult(resultCode, intent);
        finish();
    }

    private void sendBindPhoneReport() {
        String model = android.os.Build.MODEL;
        String carrier = android.os.Build.MANUFACTURER;
        String deviceID = UmengHelper.dToken;
        if (deviceID == null)
            return;
//        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
//        LOG.e("-------手机唯一标识符------" + ANDROID_ID);
//        LOG.e("-------手机型号------" + model);
//        LOG.e("-------手机厂商------" + carrier);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("os", "android");
            jsonObject.put("model", carrier + " " + model);
            jsonObject.put("deviceId", deviceID);
        } catch (JSONException e) {
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        }
        String urlPath = SystemUtility.sendPhoneReportDevice();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("token", SystemUtility.AMToken);
        dataMap.put("formData", jsonObject.toString());
        SystemUtility.requestNetPost(urlPath, dataMap, "");
    }

    /**
     * 发送验证码
     *
     * @param userPhone
     * @param type
     */
    private void sendPhoneCode(final String userPhone, final String type) {
//        LOG.e("-----------sendPhoneCode-----------");
        String urlPath = SystemUtility.sendUserPhoneCode(userPhone, type);
        SystemUtility.requestNetGet(urlPath, sendUserPhoneCode_http);
    }

    /**
     * 第三方登录,注册
     *
     * @param platId     平台ID 1微信 2QQ
     * @param sourceCode 第三方id
     */
    private void sendLogin3(final String platId, final String sourceCode) {
        String urlPath = SystemUtility.sendUserLogin3(platId, sourceCode);
        SystemUtility.requestNetGet(urlPath, sendUserLogin3_http);
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
//            LOG.e("--------------sendUserPhoneLogin------------");
            urlPath = SystemUtility.sendUserPhoneLogin(userPhone, code);
            SystemUtility.requestNetGet(urlPath, sendUserPhoneLogin_http);
        } else if (loginData.equals("0")) {
//            LOG.e("--------------sendUserPhoneregister------------");
            urlPath = SystemUtility.sendUserPhoneregister(userPhone, code);
            SystemUtility.requestNetGet(urlPath, sendUserPhoneRegister_http);
        }
    }

    /**
     * 用户验证码登录,注册
     *
     * @param userPhone
     * @param code
     */
    private void sendUserPhoneLocking(final String userPhone, final String code) {
//        LOG.e("------------sendUserPhoneLocking------------");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", userPhone);
            jsonObject.put("userLogo", userLogoPath);
        } catch (JSONException e) {
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        }
        String urlPath = SystemUtility.sendUserPhoneLocking();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("code", code);
        dataMap.put("platId", platId);
        dataMap.put("sourceCode", openID);
        dataMap.put("formData", jsonObject.toString());
        SystemUtility.requestNetPost(urlPath, dataMap, sendUserPhoneLocking_http);
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
            sendPhoneCodeBtn.setTextColor(getResources().getColor(R.color.login_code_send));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            number--;
            sendPhoneCodeBtn.setText(number + "s后重新发送");
            sendPhoneCodeBtn.setTextColor(getResources().getColor(R.color.login_code_count_down));
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

