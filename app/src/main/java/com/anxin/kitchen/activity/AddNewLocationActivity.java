package com.anxin.kitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.anxin.kitchen.user.R;

public class AddNewLocationActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBackImg;//返回按钮
    private LinearLayout selectAddressRlt;//选择地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListeners();
    }

    private void setListeners() {
    }

    private void initView() {
        setTitle("新增地址");
        mBackImg = findViewById(R.id.back_img);//返回按钮
        mBackImg.setOnClickListener(this);
        selectAddressRlt = findViewById(R.id.select_address_rlt);//选择地址
        selectAddressRlt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.select_address_rlt:
                Intent locationActivity = new Intent(AddNewLocationActivity.this, LocationActivity.class);
                startActivityForResult(locationActivity,1);
                break;
            default:
                break;
        }
    }
}
