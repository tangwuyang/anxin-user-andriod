package com.anxin.kitchen.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.activity.CreateGroupActivity;
import com.anxin.kitchen.activity.PreserveActivity;
import com.anxin.kitchen.activity.SetCountActivity;
import com.anxin.kitchen.bean.SearchGroupBean;
import com.anxin.kitchen.interface_.OnGivedPermissionListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/3/1.
 */

public class DeleteMealDialog extends Dialog{
    private Context mContext;
    private OnGivedPermissionListener customDialogListener;
    private View.OnClickListener cancel;
    private PrefrenceUtil prefrenceUtil;
    private String mGroupList;
    private SearchGroupBean bean;
    private long day;
    private String type;
    private View.OnClickListener mSureListener;
    public DeleteMealDialog(Context context, View.OnClickListener sureListener) {
        super(context);
        mContext = context;

        this.mSureListener = sureListener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_meal_dialog_layout);
        TextView cancelTv = (TextView) findViewById(R.id.cancel_tv);
        TextView sureBt = (TextView) findViewById(R.id.sure_tv);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteMealDialog.this.dismiss();
            }
        });
        sureBt.setOnClickListener(mSureListener);
    }


}
