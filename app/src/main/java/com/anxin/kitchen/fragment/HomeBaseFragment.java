package com.anxin.kitchen.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;


import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.activity.MainActivity;
import com.anxin.kitchen.utils.Log;

public class HomeBaseFragment extends Fragment implements OnClickListener {

    private static final Log LOG = Log.getLog();
    public Context mContext;
    private MainActivity main;
    public MyApplication mApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (mApp == null) {
            mApp = (MyApplication) getActivity().getApplication();
        }
        mContext = getActivity();
        if (mContext == null) {
            mContext = MainActivity.context;
        }
        main = (MainActivity) MainActivity.context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void hideMainBottom() {
        if (main != null)
            main.hideMainBottom();
    }

    public void showMainBottom() {
        if (main != null)
            main.showMainBotton();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
