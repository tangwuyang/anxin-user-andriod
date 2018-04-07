package com.anxin.kitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;

public class MipushActivity extends UmengNotifyClickActivity {

    private static String TAG = MipushActivity.class.getName();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush_);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.e(TAG, body);
//        Message message = Message.obtain();
//        message.obj = body;
//        handler.sendMessage(message);
    }
}
