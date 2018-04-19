package com.anxin.kitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AddNewFriendActivity extends BaseActivity implements View.OnClickListener, RequestNetListener {
    private static final String ADD_FRIEND_TO_GROUP = "ADD_FRIEND_TO_GROUP";
    private Gson mGson;
    private String mGroupName;
    private int mGroupId;
    private String mToken;
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
        mCompleteTv.setOnClickListener(this);
    }

    private void initView() {
        setTitle("新增");
        mCompleteTv = ((TextView) findViewById(R.id.complete_tv));
        mNameEt = findViewById(R.id.name_et);
        mTelEt = findViewById(R.id.tel_et);
        mCompleteTv.setVisibility(View.VISIBLE);
        mCompleteTv.setText("确定");
        Intent intent = getIntent();
        mGson = new Gson();
        if (null != intent) {
            mGroupId = intent.getIntExtra("groupId", 0);
            mGroupName = intent.getStringExtra("groupName");
        }
        mToken = new Cache(this).getAMToken();
        mBackImg = findViewById(R.id.back_img);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.complete_tv:
                String name = mNameEt.getText().toString();
                String phone = mTelEt.getText().toString();
                boolean isValue = VerifyDataIsValue(name, phone);
                if (isValue) {
                    addFriend(name, phone);
                    onBackPressed();
                }
                break;
        }
    }

    private void addFriend(String name, String phone) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("token", mToken);
        Map<String, Object> formData = new HashMap<>();
        formData.put("groupId", mGroupId);
        formData.put("phone", phone);
        formData.put("trueName", name);
        String formDataSt = mGson.toJson(formData);
        myLog("--------form---" + formDataSt);
        dataMap.put("formData", formDataSt);
        requestNet(SystemUtility.addFriendToGroupUrl(), dataMap, ADD_FRIEND_TO_GROUP);
    }

    /**
     * 验证姓名跟电话是否符合要求
     *
     * @param name
     * @param phone
     */
    private boolean VerifyDataIsValue(String name, String phone) {

        if (StringUtils.isNullOrEmpty(name)) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.length() != 11) {
            Toast.makeText(this, "请输入长度为11的电话号码", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        if (null != responseString && requestCode.equals(ADD_FRIEND_TO_GROUP)) {
            String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
            if (status.equals(Constant.REQUEST_SUCCESS)) {
                //关闭按钮 返回前一个界面刷新界面
            }
        }
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);
    }
}
