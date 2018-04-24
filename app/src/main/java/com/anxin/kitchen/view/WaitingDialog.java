package com.anxin.kitchen.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.activity.LoginActivity;
import com.anxin.kitchen.user.R;

/**
 * Created by 唐午阳 on 2018/3/1.
 */

public class WaitingDialog extends Dialog {
    private ImageView mWaitingImg;
    private ObjectAnimator ra;
    private MyCountDownTimer mc;
    private long millisInFuture;

    public WaitingDialog(Context context, long millisInFuture) {
        super(context, R.style.MyDialog);
        this.millisInFuture = millisInFuture;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_layout);
        mWaitingImg = findViewById(R.id.waiting_img);
        mc = new MyCountDownTimer(millisInFuture, 1000 * 20);
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            ra = ObjectAnimator.ofFloat(mWaitingImg, "rotationY", 0f, 180f);
            ra.setDuration(1000);
        }

        @Override
        public void onFinish() {
            ra.cancel();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    private Handler handler = new Handler();

    public void startAnimation() {
        ra.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animator.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ra.start();
        mc.start();
    }

    public void stopAnimation() {
        mc.cancel();
        ra.cancel();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            WaitingDialog.this.dismiss();
        }
    };

}
