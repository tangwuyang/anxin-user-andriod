package com.anxin.kitchen.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;

public class AddNewFriendActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mBackImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_friend);
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
        setTitle("新增");
        TextView sureBt = ((TextView)findViewById(R.id.complete_tv));
        sureBt.setVisibility(View.VISIBLE);
        sureBt.setText("确定");
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
