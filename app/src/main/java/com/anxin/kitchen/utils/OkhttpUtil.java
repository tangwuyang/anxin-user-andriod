package com.anxin.kitchen.utils;

import com.anxin.kitchen.interface_.ListenerBack;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xujianjun on 2018/4/6.
 */

public class OkhttpUtil {

    private static OkhttpUtil mInstance;
    private static OkHttpClient mClient;

    public static OkhttpUtil getmInstance() {
        if (mInstance == null) {
            mInstance = new OkhttpUtil();
        }

        if (mClient == null) {
            mClient = new OkHttpClient.Builder()
                    //设置读取数据的时间
                    .readTimeout(5, TimeUnit.SECONDS)
                    //对象的创建
                    .build();
        }
        return mInstance;
    }

    /**
     * 获取google 位置规划数据
     */
    public void getGoogleDistanceMatrix(String url, final int type, final ListenerBack listenerBack) {


    }

    /**
     * get请求方式
     * 请求百度网页的源码数据
     */
    public void requestGet(String url, final int type, final ListenerBack listenerBack) {
        //创建网络处理的对象

        //创建一个网络请求的对象，如果没有写请求方式，默认的是get
        //在请求对象里面传入链接的URL地址
        Request request = new Request.Builder()
                .url(url).build();

        //call就是我们可以执行的请求类
        Call call = mClient.newCall(request);
        //异步方法，来执行任务的处理，一般都是使用异步方法执行的
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                listenerBack.onListener(type, e.toString(), false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功
                //子线程
                //main thread1
//                Log.e(“TAG”, Thread.currentThread().getName() + "结果  " + response.body().string());
                listenerBack.onListener(type, response.body().string(), true);
            }
        });
        //  call.cancel();取消任务

        //同步方法,一般不用
       /* try {
            Response execute = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }



    /**
     * post请求方式，请求网络数据
     * 请求ShowAPI里面的json数据
     */
    public void requestPost(String url, final int type, String data, final ListenerBack listenerBack) {
        //创建网络处理的对象

        //post请求来获得数据
        //创建一个RequestBody，存放重要数据的键值对
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, data);
//        RequestBody body = new FormBody.Builder()
//                .add("showapi_appid", "13074")
//                .add("showapi_sign", "ea5b4bf2e140498bb772d1bf2a51a7a0").build();
        //创建一个请求对象，传入URL地址和相关数据的键值对的对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody).build();

        //创建一个能处理请求数据的操作类
        Call call = mClient.newCall(request);

        //使用异步任务的模式请求数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                WybLog.syso("---okHttp Post:失败 " + e.toString());
                listenerBack.onListener(type, e.toString(), false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                WybLog.syso("---okHttp Post:成功 " + response.body().string());
                listenerBack.onListener(type, response.body().string(), true);
            }
        });
    }

    public static void getData(String url, String data) {
        OkHttpClient client = new OkHttpClient();
        //构建FormBody，传入要提交的参数
        RequestBody requestBody = RequestBody.create(MediaType.parse("Application/json;charset=utf-8"),
                data);

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

//                WybLog.syso("---okHttp：失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
//                WybLog.syso("---okHttp：" + response.toString());
            }
        });

    }

}
