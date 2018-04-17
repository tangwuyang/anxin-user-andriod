package com.anxin.kitchen.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.anxin.kitchen.interface_.ListenerBack;
import com.anxin.kitchen.user.R;


/**
 * 地图菜单
 *
 * @author xujianjun
 */
@SuppressLint("InflateParams")
public class PopupUtil {

    private static PopupUtil tmp;

    public static synchronized PopupUtil getInstances() {
        if (tmp == null) {
            tmp = new PopupUtil();
        }
        return tmp;
    }

    private Activity mActivity;
    private View mView;
    private PopupWindow mPopupWindow = null;
    private ListenerBack mListener;

    public void show(Activity a, View v, ListenerBack listener) {
        this.mListener = listener;
        this.mActivity = a;
        if (mPopupWindow == null || mView == null) {
            mView = LayoutInflater.from(mActivity).inflate(R.layout.pop_pay, null);
            mView.findViewById(R.id.rl_tableware_deposit_account).setOnClickListener(clickListener);
            mView.findViewById(R.id.ll_pay_weixin).setOnClickListener(clickListener);
            mView.findViewById(R.id.ll_pay_zhifubao).setOnClickListener(clickListener);

            mPopupWindow = new PopupWindow(mView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(-00000));// 点击PopupWindow
            // 外的屏幕，PopupWindow依然会消失
            mPopupWindow.setFocusable(false);
            mPopupWindow.update();
            mPopupWindow.setAnimationStyle(R.style.PopupBottomAnimation);
        }
//        mPopupWindow.showAsDropDown(v);
        mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }


    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onListener(v.getId(), "", true);
            }
            if (mPopupWindow != null || mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }
    };
}
