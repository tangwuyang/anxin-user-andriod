package com.anxin.kitchen.view;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anxin.kitchen.interface_.OnGivedPermissionListener;
import com.anxin.kitchen.user.R;

/**
 * Created by 唐午阳 on 2018/3/1.
 */

public class RequestLocationPermissionDialog extends Dialog{
    private OnGivedPermissionListener customDialogListener;
    public RequestLocationPermissionDialog(Context context,OnGivedPermissionListener customDialogListener) {
        super(context);
        this.customDialogListener = customDialogListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_location_permission_layout);
        TextView cancelBt = (TextView) findViewById(R.id.cancel_tv);
        TextView openPermissionTv = (TextView) findViewById(R.id.open_right_tv);
        cancelBt.setOnClickListener(clickListener);
        openPermissionTv.setOnClickListener(clickListener2);
    }
   private View.OnClickListener clickListener2 = new View.OnClickListener() {
       @Override
       public void onClick(View view) {
            if (customDialogListener!=null){
                customDialogListener.onGivedPermssion();
            }
            dismiss();
       }
   };
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            RequestLocationPermissionDialog.this.dismiss();
        }
    };

}
