package com.anxin.kitchen.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.SystemUtility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.PushAgent;

import java.util.Map;
import java.util.Set;


public class BaseActivity extends FragmentActivity implements RequestNetListener{

    private TextView titleTv;  //标题
    private boolean isDebug = true;  //是否是调试模式
    public static final String PREFERENCE_NAME = "ANXIN_ANDROID";
    public Gson mGson;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushAgent.getInstance(this).onAppStart();
        mGson = new Gson();
    }

    //修改标题
    protected void setTitle(String title){
        titleTv =  findViewById(R.id.title_tv);
        titleTv.setText(title);

    }
    public String mToken;
    public boolean isLogin(){
        if (null == mToken){
            mToken = new Cache(this).getAMToken();
        }
        if (mToken==null){
            SystemUtility.startLoginUser(this);
        }
        return true;
    }


    //调试打印
    public void myLog(String msg){
        if (isDebug){
            android.util.Log.i(getClass().getSimpleName(),msg);
        }
    }

    public void startNewActivity(Class classType) {
        Intent intent = new Intent(this, classType);
        startActivity(intent);
    }

    //请求网络异步方法
    /**
     * post公共方法
     * urlPath 请求地址
     * dataMap 请求参数map
     * requestCode 请求标识
     * */
    public void requestNet(String urlPath, Map<String,Object> dataMap, final String requestCode){
        if (null != urlPath && urlPath.length()>0){
            myLog("------------------>"+urlPath);
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            if (null != dataMap){
                Set<String> paraNames = dataMap.keySet();
                for (String para:
                        paraNames) {
                    myLog("---------->"+para + "  " + dataMap.get(para));
                    params.put(para,dataMap.get(para));
                }
            }
            client.post(urlPath,params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                    String result = "";
                    String head = "";
                    if (bytes != null){
                        result = new String(bytes);
                        myLog(requestCode+"----------->请求成功" + result);
                        requestSuccess(result,requestCode);
                    }
                }

                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                    {
                        String result = "";
                        if (bytes != null) {
                            result = new String(bytes);
                            myLog("--------->请求失败" +result);
                        }

                    }
                }




            });


        }
    }


    //网咯请求成功
    @Override
    public void requestSuccess(String responseString,String requestCode) {
        //请求成功后的操作
    }
    //网咯请求失败
    @Override
    public void requestFailure(String responseFailure,String requestCode) {

    }

    public void setImg(ImageView view, String src){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.food1)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        // holder.BackgroundImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageLoader.displayImage(src,view,options);
    }
}
