package com.anxin.kitchen.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.anxin.kitchen.user.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
    }

    @Override
    public void onClick(View v) {

    }
}
