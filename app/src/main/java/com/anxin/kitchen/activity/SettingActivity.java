package com.anxin.kitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.fragment.loginfragment.AddUserDataFragment;
import com.anxin.kitchen.fragment.myfragment.AgreementFragment;
import com.anxin.kitchen.fragment.myfragment.MyHomeFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.user.wxapi.WXEntryActivity;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.utils.UmengHelper;
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
public class SettingActivity extends FragmentActivity implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private ImageView backBtn;//返回
    private RelativeLayout myRlt;//关于我们
    private RelativeLayout agreementRlt;//服务协议
    private Button logoutLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_fragment);
        initView();
    }

    /**
     * 初始化绑定控件
     */
    private void initView() {
        myRlt = (RelativeLayout) findViewById(R.id.my_rlt);
        agreementRlt = (RelativeLayout) findViewById(R.id.agreement_rlt);
        backBtn = (ImageView) findViewById(R.id.back_btn);
        logoutLoginBtn = findViewById(R.id.logout_user);
        logoutLoginBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        myRlt.setOnClickListener(this);
        agreementRlt.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.my_rlt://关于我们
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                MyHomeFragment myHomeFragment = new MyHomeFragment();
                ft.replace(R.id.content2_frame, myHomeFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.agreement_rlt://服务协议
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                AgreementFragment agreementFragment = new AgreementFragment();
                ft2.replace(R.id.content2_frame, agreementFragment);
                ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft2.addToBackStack(null);
                ft2.commit();
                break;
            case R.id.logout_user:
                if (MyApplication.getInstance().getCache().getAMToken() == null)
                    return;
                String userId = MyApplication.getInstance().getAccount().getUserID();
                if (userId != null)
                    UmengHelper.getInstance().deleteUserAlias(userId);
                MyApplication.getInstance().setAccount(null);
                MyApplication.getInstance().getCache().setAcount(this, null);
                MyApplication.getInstance().getCache().setAMToken(null);
                SystemUtility.AMToken = "";
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

