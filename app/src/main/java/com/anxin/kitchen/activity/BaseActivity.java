package com.anxin.kitchen.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;

public class BaseActivity extends Activity {
    private TextView titleTv;  //标题
    private boolean isDebug = true;  //是否是调试模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //修改标题
    protected void setTitle(String title){
        titleTv =  findViewById(R.id.title_tv);
        titleTv.setText(title);
    }

    //调试打印
    protected void myLog(String msg){
        if (isDebug){
            android.util.Log.i(getClass().getSimpleName(),msg);
        }
    }
}
