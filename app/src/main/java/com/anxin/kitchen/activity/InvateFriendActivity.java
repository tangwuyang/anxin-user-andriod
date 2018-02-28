package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;

public class InvateFriendActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mBackImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invate_friend_activity);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListeners();
    }

    private void initListeners() {
        mBackImg.setOnClickListener(this);
    }

    private void initView() {
        setTitle("邀请团友");
        mBackImg = findViewById(R.id.back_img);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_img:
                onBackPressed();
                break;
        }
    }
}

