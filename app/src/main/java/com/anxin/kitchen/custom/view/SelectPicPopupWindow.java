package com.anxin.kitchen.custom.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.anxin.kitchen.user.R;


public class SelectPicPopupWindow extends PopupWindow {


    private TextView btn_take_photo, btn_pick_photo, btn_cancel;
    private View mMenuView;

    public SelectPicPopupWindow(Context context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.fragment_headicon_dialog, null);
        btn_take_photo = (TextView) mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (TextView) mMenuView.findViewById(R.id.btn_pick_photo);
        btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                SelectPicPopupWindow.this.dismiss();
            }
        });
        btn_pick_photo.setOnClickListener(itemsOnClick);
        btn_take_photo.setOnClickListener(itemsOnClick);
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.dialog_background));
        this.setBackgroundDrawable(dw);
    }

}
