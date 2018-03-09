package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class CreateGroupActivity extends BaseActivity implements View.OnClickListener,RequestNetListener{
    private static final String TAG = "CreateGroupActivity";
    private ImageView mBackImg;
    private TextView mSureBt;
    private String mGroupName;
    private EditText mGroupNameEt;
    private static final String CREATE_GROUP = "CREATE_GROUP";
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
        mGroupNameEt = findViewById(R.id.new_group_name_et);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.complete_tv:
                CreateGroup();
                break;
        }
    }

    //创建饭团
    private void CreateGroup() {
        mGroupName = mGroupNameEt.getText().toString();
        if (!(null!=mGroupName&&mGroupName.length()>0)){
            Toast.makeText(this, "请输入饭团名", Toast.LENGTH_SHORT).show();
            return;
        }
        String token = new Cache(this).getAMToken();
        if (token==null){
            startNewActivity(LoginActivity.class);
        }else {
          /*  Map<String,Object> dataMap= new HashMap<>();
            dataMap.put("groupName",mGroupName);
            dataMap.put("token",token);*/

            String url = SystemUtility.CreateGroup()+"?groupName="+mGroupName+"&token="+token;
            Log.i(TAG, "CreateGroup: --------->"+url);
            requestNet(url,null,CREATE_GROUP);
        }
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);

    }

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        if (requestCode==CREATE_GROUP){
            String status = StringUtils.parserMessage(responseString,"message");
            if (status.equals(Constant.LOGIN_FIRST)){
                startNewActivity(LoginActivity.class);
            }
        }
    }
}
