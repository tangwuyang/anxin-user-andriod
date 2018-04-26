package com.anxin.kitchen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.umeng.analytics.MobclickAgent;


/**
 * 登陆界面
 */
public class UserNameActivity extends Activity implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private ImageView backBtn;//返回
    private TextView storeBtn;//保存
    private String userName;//用户名
    private EditText userNameEdit;//用户名称输入框


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_name_set_fragment);
        userName = getIntent().getStringExtra("userName");
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
        userNameEdit = (EditText) findViewById(R.id.userName_edit);
        if (userName != null && !userName.equals("暂无"))
            userNameEdit.setText(userName);
        userNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userNameEdit.getText().toString().length() != 0) {
                    storeBtn.setAlpha(1f);
                } else {
                    storeBtn.setAlpha(0.5f);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.store_btn://保存
                String name = userNameEdit.getText().toString();
                if (null != name && name.length() != 0) {
                    Intent intent = getIntent();
                    intent.putExtra("userName", name);
                    setResult(this.RESULT_OK, intent);
                }
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

