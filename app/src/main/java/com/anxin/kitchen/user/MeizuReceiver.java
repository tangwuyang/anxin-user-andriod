package com.anxin.kitchen.user;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.event.OnSaveBitmapEvent;
import com.anxin.kitchen.event.ViewUpdateHeadIconEvent;
import com.umeng.message.meizu.UmengMeizuPushReceiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 友盟魅族通道监听
 */
public class MeizuReceiver extends UmengMeizuPushReceiver {

}
