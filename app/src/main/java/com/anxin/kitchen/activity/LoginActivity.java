package com.anxin.kitchen.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.anxin.kitchen.user.R;
import com.anxin.kitchen.user.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 登陆界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView WXloginBtn;//微信登陆按钮
    private IWXAPI mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        ImageView back_btn = (ImageView) findViewById(R.id.back_btn);//返回按钮
        back_btn.setOnClickListener(this);
        WXloginBtn = (ImageView) findViewById(R.id.wx_login);
        WXloginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.wx_login:
                loginToWeiXin();
                break;
        }
    }

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

        } else
            Toast.makeText(this, "请安装微信", Toast.LENGTH_SHORT).show();
    }
}

