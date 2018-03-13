package com.anxin.kitchen.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.StringUtils;

public class AddNewFriendActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mBackImg;
    private TextView mCompleteTv;
    private EditText mNameEt;
    private EditText mTelEt;
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
            case R.id.complete_tv:
                String name = mNameEt.getText().toString();
                String phone = mTelEt.getText().toString();
                boolean isValue = VerifyDataIsValue(name,phone);
                if (isValue){
                    addFriend(name,phone);
                }
                break;
        }
    }

    private void addFriend(String name, String phone) {

    }

    /**
     * 验证姓名跟电话是否符合要求
     *
     * @param name
     * @param phone*/
    private boolean VerifyDataIsValue(String name, String phone) {

        if ( StringUtils.isNullOrEmpty(name)){
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.length()!=11){
            Toast.makeText(this, "请输入长度为11的电话号码", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
