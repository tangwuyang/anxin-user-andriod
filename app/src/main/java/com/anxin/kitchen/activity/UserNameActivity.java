package com.anxin.kitchen.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
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
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 登陆界面
 */
public class UserNameActivity extends BaseActivity implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private ImageView backBtn;//返回
    private TextView storeBtn;//保存


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_name_set_fragment);
        initView();
    }

    /**
     * 初始化绑定控件
     */
    private void initView() {
        backBtn = (ImageView) findViewById(R.id.back_btn);//返回按钮
        storeBtn = (TextView) findViewById(R.id.store_btn);//保存按钮
        storeBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.store_btn://保存
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

