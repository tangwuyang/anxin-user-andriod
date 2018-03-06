package com.anxin.kitchen.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.activity.CreateGroupActivity;
import com.anxin.kitchen.activity.SetCountActivity;
import com.anxin.kitchen.interface_.OnGivedPermissionListener;
import com.anxin.kitchen.user.R;

/**
 * Created by 唐午阳 on 2018/3/1.
 */

public class ChoseGroupDialog extends Dialog{
    private Context mContext;
    private OnGivedPermissionListener customDialogListener;
    public ChoseGroupDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_group_dialog_layout);
        ImageView cancelImg = findViewById(R.id.cancel_img);
        TextView setcount = (TextView) findViewById(R.id.cancel_tv);
        TextView createNewGroup = (TextView) findViewById(R.id.open_right_tv);
        setcount.setOnClickListener(clickListener2);
        createNewGroup.setOnClickListener(clickListener3);
        cancelImg.setOnClickListener(clickListener);
    }
   private View.OnClickListener clickListener2 = new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           Intent intent = new Intent(getContext(), SetCountActivity.class);
           getContext().startActivity(intent);
           ChoseGroupDialog.this.dismiss();
       }
   };
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ChoseGroupDialog.this.dismiss();
        }
    };


    private View.OnClickListener clickListener3 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, CreateGroupActivity.class);
            mContext.startActivity(intent);
            ChoseGroupDialog.this.dismiss();
        }
    };
}
