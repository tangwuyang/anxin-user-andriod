package com.anxin.kitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.anxin.kitchen.user.R;


/**
 * 登陆界面
 */
public class LoginActivity extends AppCompatActivity {

    private RelativeLayout welcome_rlt;//欢迎页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }, 2000);
    }

    private void initView() {
        welcome_rlt = (RelativeLayout) findViewById(R.id.welcome_bg);
    }

    private Handler handler = new Handler();


}

