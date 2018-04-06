package com.anxin.kitchen.utils;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.user.R;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.common.UmengMessageDeviceConfig;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 友盟推送类
 */
public class UmengHelper {

    public static final String TAG = "UmengHelper";
    public static final String ANXIN_Alias = "anxin";
    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
    public static UmengHelper mUmengHelper;
    public static String dToken = "";
    private PushAgent mPushAgent;

    public static UmengHelper getInstance() {
        if (mUmengHelper == null) {
            mUmengHelper = new UmengHelper();
        }
        return mUmengHelper;
    }

    public void init() {
        mPushAgent = PushAgent.getInstance(MyApplication.getInstance());
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        mPushAgent.setResourcePackageName("com.anxin.kitchen.user");
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                dToken = deviceToken;
                Log.d(TAG, "onSuccess: deviceToken----" + deviceToken);
                MyApplication.getInstance().sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.d(TAG, "onFailure: ---" + s + "     s1--" + s1);
                MyApplication.getInstance().sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });
        UMConfigure.init(MyApplication.getInstance(), "5341775518809", "${UMENG_CHANNEL_VALUE}", UMConfigure.DEVICE_TYPE_PHONE, "prJz5OuvqVBZwn0L0BkAcA==");
        mPushAgent.onAppStart();

        getAppInfo();
//        setUserTag();
//        setUserAlias();
        setMessageHandler();
        setNotificationClickHandler();

    }

    /**
     * 设置用户别名
     */
    public void setUserAlias(String userAlias) {
        //设置用户id和device_token的一对多的映射关系：
        mPushAgent.addAlias(userAlias, ANXIN_Alias, new UTrack.ICallBack() {

            @Override
            public void onMessage(boolean isSuccess, String message) {
                Log.e(TAG,"----------setUserAlias-------addAlias--------"+message);
            }

        });
        //设置用户id和device_token的一一映射关系，确保同一个alias只对应一台设备：
        mPushAgent.setAlias(userAlias, ANXIN_Alias,

                new UTrack.ICallBack() {

                    @Override
                    public void onMessage(boolean isSuccess, String message) {
                        Log.e(TAG,"----------setUserAlias-------setAlias--------"+message);
                    }

                });
    }

    /**
     * 设置用户标签
     */
    private void setUserTag() {
        mPushAgent.getTagManager().addTags(new TagManager.TCallBack() {

            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
            }

        }, "movie", "sport");
    }

    public void setMessageHandler() {
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(MyApplication.getInstance()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(MyApplication.getInstance()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
            }

            //自定义通知样式
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                Log.d(TAG, "getNotification 111 msg=" + msg + ", msg.builder_id=" + msg.builder_id);
                switch (msg.builder_id) {
                    //自定义通知样式编号
                    case 1:
//                        Notification.Builder builder = new Notification.Builder(context);
//                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
//                        builder.setContent(myNotificationView)
//                                .setSmallIcon(getSmallIconId(context, msg))
//                                .setTicker(msg.ticker)
//                                .setAutoCancel(true);
//
//                        return builder.getNotification();
                        return super.getNotification(context, msg);
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        PushAgent.getInstance(MyApplication.getInstance()).setMessageHandler(messageHandler);
    }


    /**
     * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
     * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     */
    public void setNotificationClickHandler() {
        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Map<String, String> params = new HashMap<>();
                params.put("msg_id", msg.msg_id);
                Log.d(TAG, "dealWithCustomAction msg=" + msg.toString() + ", msg.custom=" + msg.custom);

            }

            @Override
            public void launchApp(Context context, UMessage msg) {
                Log.d(TAG, "launchApp msg=" + msg.toString() + ", msg.custom=" + msg.custom);

                super.launchApp(context, msg);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
                Log.d(TAG, "openUrl msg=" + msg.toString() + ", msg.custom=" + msg.custom);
                super.openUrl(context, msg);
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
                Log.d(TAG, "openActivity msg=" + msg.toString() + ", msg.custom=" + msg.custom);

                super.openActivity(context, msg);
            }
        };
        PushAgent.getInstance(MyApplication.getInstance()).setNotificationClickHandler(notificationClickHandler);
    }

    private void getAppInfo() {
        String pkgName = MyApplication.getInstance().getPackageName();
        String info = String.format("DeviceToken:%s\n" + "SdkVersion:%s\nAppVersionCode:%s\nAppVersionName:%s",
                PushAgent.getInstance(MyApplication.getInstance()).getRegistrationId(), MsgConstant.SDK_VERSION,
                UmengMessageDeviceConfig.getAppVersionCode(MyApplication.getInstance()), UmengMessageDeviceConfig.getAppVersionName(MyApplication.getInstance()));
        Log.d(TAG, "应用包名:" + pkgName + "\n" + info);
    }
}