package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;

public class CreateGroupActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBackImg;
    private TextView mSureBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListeners();
    }

    private void initListeners() {
        mBackImg.setOnClickListener(this);
        mSureBt.setOnClickListener(this);
    }

    private void initView() {
        setTitle("创建饭团");
        mSureBt = ((TextView)findViewById(R.id.complete_tv));
        mSureBt.setVisibility(View.VISIBLE);
        mSureBt.setText("创建");
        mBackImg = findViewById(R.id.back_img);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.complete_tv:
                startNewActivity(null);
                break;
        }
    }

}
