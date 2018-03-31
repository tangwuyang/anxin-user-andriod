package com.anxin.kitchen.view;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;

/**
 * Created by 唐午阳 on 2018/3/1.
 */

public class WaitingDialog extends Dialog{
    private ImageView mWaitingImg;
    private ObjectAnimator ra;
    public WaitingDialog(Context context) {
        super(context,R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_layout);
        mWaitingImg = findViewById(R.id.waiting_img);
        ra = ObjectAnimator.ofFloat(mWaitingImg,"rotationY", 0f, 360f);
        ra.setDuration(10000);
    }

    public void startAnimation(){
        ra.start();
    }
    public void stopAnimation(){
        ra.cancel();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            WaitingDialog.this.dismiss();
        }
    };

}
