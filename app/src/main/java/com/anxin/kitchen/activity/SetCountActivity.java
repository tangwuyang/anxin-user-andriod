package com.anxin.kitchen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;
import com.umeng.analytics.MobclickAgent;

public class SetCountActivity extends BaseActivity implements View.OnClickListener {
    public static final int SET_COUNT = 202;  //设置数量
    private TextView mTitleTv;
    private TextView mSureTv;
    private ImageView mBackImg;
    private EditText mCountsEt;
    private String type;
    private String recevieData;
    private long day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_count);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        if (null!=intent){
            recevieData = intent.getStringExtra("data");
            day = Long.valueOf(recevieData.substring(0,recevieData.indexOf("-")));
            type = recevieData.substring(recevieData.indexOf("-")+1);
            myLog("------------->"+day + "  " + type);
        }

        mBackImg = findViewById(R.id.back_img);
        mTitleTv = findViewById(R.id.title_tv);
        mSureTv = findViewById(R.id.complete_tv);
        mCountsEt = findViewById(R.id.account_tv);
        mTitleTv.setText("添加份数");
        mSureTv.setText("确定");
        mSureTv.setVisibility(View.VISIBLE);
        mCountsEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String numSt = mCountsEt.getText().toString();
                if (numSt==null || numSt.length()<1){
                    setSureTvUnclicekable();
                    return;
                }
                int nums = Integer.valueOf(numSt);
                if (nums>0){
                    setSureTvClicekable();
                }else {
                    setSureTvUnclicekable();
                }
            }
        });
        setSureTvUnclicekable();
        mBackImg.setOnClickListener(this);
        mSureTv.setOnClickListener(this);
    }

    private void setSureTvUnclicekable() {
        mSureTv.setEnabled(false);
        mSureTv.setTextColor(getResources().getColor(R.color.color_text_unselected));
    }

    private void setSureTvClicekable() {
        mSureTv.setEnabled(true);
        mSureTv.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_img:
                this.finish();
                break;
            case R.id.complete_tv:
                Intent intent = new Intent();
                intent.putExtra("day",day);
                intent.putExtra("type",type);
                //myLog("--------------->"+mGson.toJson(food));
                intent.putExtra("nums",Integer.valueOf(mCountsEt.getText().toString()));
                setResult(SET_COUNT,intent);
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
}
