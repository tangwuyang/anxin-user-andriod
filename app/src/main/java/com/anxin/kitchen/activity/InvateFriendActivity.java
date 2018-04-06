package com.anxin.kitchen.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.user.R;
//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.UMAuthListener;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;

import java.util.Map;

import static android.os.Build.VERSION.*;

public class InvateFriendActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mBackImg;
    private LinearLayout mShareWechatLl;
    private LinearLayout mShareFriendGroupLl;
    private LinearLayout mShareQQLl;
//    private UMShareListener shareListener = new UMShareListener() {
//        /**
//         * @descrption 分享开始的回调
//         * @param platform 平台类型
//         */
//        @Override
//        public void onStart(SHARE_MEDIA platform) {
//        }
//        /**
//         * @descrption 分享成功的回调
//         * @param platform 平台类型
//         */
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(InvateFriendActivity.this,"成功了",Toast.LENGTH_LONG).show();
//        }
//
//        /**
//         * @descrption 分享失败的回调
//         * @param platform 平台类型
//         * @param t 错误原因
//         */
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(InvateFriendActivity.this,"失                                            败"+t.getMessage(),Toast.LENGTH_LONG).show();
//        }
//
//        /**
//         * @descrption 分享取消的回调
//         * @param platform 平台类型
//         */
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(InvateFriendActivity.this,"取消                                     了",Toast.LENGTH_LONG).show();
//
//        }
//    };

//    UMAuthListener authListener = new UMAuthListener() {
//        @Override
//        public void onStart(SHARE_MEDIA platform) {
//            //授权开始的回调，可以用来处理等待框，或相关的文字提示
//        }
//
//        @Override
//        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform, int action) {
//
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invate_friend_activity);
        requestPermission();
        initView();
    }


    private void requestPermission(){
        String[] mPermissionList =
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
        ActivityCompat.requestPermissions(this,mPermissionList,123);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initListeners();
    }

    private void initListeners() {
        mBackImg.setOnClickListener(this);
        mShareQQLl.setOnClickListener(this);
        mShareFriendGroupLl.setOnClickListener(this);
        mShareWechatLl.setOnClickListener(this);
    }

    private void initView() {
        setTitle("邀请团友");
        mBackImg = findViewById(R.id.back_img);
        mShareWechatLl = findViewById(R.id.share_wechat_ll);
        mShareFriendGroupLl = findViewById(R.id.share_group_ll);
        mShareQQLl = findViewById(R.id.share_qq_ll);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.share_wechat_ll:
//                shareWechatFriend();
                break;

            case R.id.share_qq_ll:
//                new ShareAction(InvateFriendActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN)
//                        .withText("hello").setCallback(new UMShareListener() {
//                    @Override
//                    public void onStart(SHARE_MEDIA share_media) {
//
//                    }
//
//                    @Override
//                    public void onResult(SHARE_MEDIA share_media) {
//
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA share_media) {
//
//                    }
//                }).open();
                break;
        }
    }


    //分享到微信好友
//    private void shareWechatFriend() {
//        UMImage image = new UMImage(InvateFriendActivity.this, R.drawable.search_bg);//资源文件
//       /* new ShareAction(InvateFriendActivity.this)
//                .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
//                .withText("hello")//分享内容
//                .withMedia(image)
//                .setCallback(shareListener)//回调监听器
//                .share();*/
//
//        new ShareAction(InvateFriendActivity.this)
//                .setPlatform(SHARE_MEDIA.WEIXIN)
//                .withText("hello").withMedia(image)
//                .setCallback(shareListener)
//                .share();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//    }
}

