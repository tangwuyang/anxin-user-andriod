package com.anxin.kitchen.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.event.OnSaveBitmapEvent;
import com.anxin.kitchen.event.ViewUpdateHeadIconEvent;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 服务类
 */
public class MyService extends Service {

    private static final Log LOG = Log.getLog();

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    public class MyBinder extends Binder {
        public MyService getServiceInstance() {
            return MyService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        LOG.e("-------------MyService--onCreate--");
        EventBusFactory.getInstance().register(this);
    }

    //解析登陆
    public void onEventMainThread(OnSaveBitmapEvent Msg) {
//        LOG.e("-------------OnSaveBitmapEvent----");
        String poth = Msg.getImavePath();
        String namePoth = Msg.getSaveName();
        onSaveBitmap(poth, MyApplication.getInstance(), namePoth);
    }

    //下载图片
    private void onSaveBitmap(String imagePath, final Context context, final String userPhone) {
        OkHttpClient okHttpClient = new OkHttpClient();
//        LOG.e("-------------imagePath----" + imagePath);
        Request request = new Request.Builder().url(imagePath).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                LOG.e("-------------onFailure----");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                LOG.e("-------------?onResponse----");
                byte[] bytes = response.body().bytes();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                String fileName = context.getExternalCacheDir().getPath() + "anxin/" + userPhone + "logo.png";
                onSaveBitmap(bitmap, context, fileName);
                MyApplication.mApp.getCache().setAccountImageURI(userPhone, fileName);
                EventBusFactory.postEvent(new ViewUpdateHeadIconEvent());
            }
        });
    }

    /*
    *Android保存图片到系统
    */
    public static void onSaveBitmap(Bitmap mBitmap, final Context context, String fileName) {
        // 第一步：首先保存图片
        //将Bitmap保存图片到指定的路径/sdcard/Boohee/下，文件名以当前系统时间命名,但是这种方法保存的图片没有加入到系统图库中
        File file = new File(fileName);
        if (!file.exists()) {
            //先得到文件的上级目录，并创建上级目录，在创建文件
            file.getParentFile().mkdir();
            try {
                //创建文件
                file.createNewFile();
            } catch (IOException e) {
                MobclickAgent.reportError(MyApplication.getInstance(), e);
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }
}
