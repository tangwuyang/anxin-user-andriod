package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;

public class AddNewLocationActivity extends BaseActivity implements View.OnClickListener{
    private TextView mCompleteTv;
    private ImageView mBackImg;

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
        mBackImg = findViewById(R.id.back_img);
        mCompleteTv = findViewById(R.id.complete_tv);
        mCompleteTv.setVisibility(View.VISIBLE);
        mCompleteTv.setOnClickListener(this);
        mBackImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.back_img:
                onBackPressed();
                break;
            case R.id.complete_tv:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
