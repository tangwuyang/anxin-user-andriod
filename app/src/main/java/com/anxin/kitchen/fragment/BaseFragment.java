package com.anxin.kitchen.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.response.BaseResponse;
import com.anxin.kitchen.utils.JsonHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.Map;
import java.util.Set;

/**
 * 基础fragment
 * Created by xujianjun on 2018/4/5.
 */

public abstract class BaseFragment extends Fragment implements RequestNetListener {
    protected View view;
    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    //请求网络异步方法

    /**
     * post公共方法
     * urlPath 请求地址
     * dataMap 请求参数map
     * requestCode 请求标识
     */
    public void requestNet(String urlPath, Map<String, Object> dataMap, final String requestCode) {
        if (null != urlPath && urlPath.length() > 0) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            if (null != dataMap) {
                Set<String> paraNames = dataMap.keySet();
                for (String para :
                        paraNames) {
                    params.put(para, dataMap.get(para));
                }
            }
            client.post(urlPath, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                    String result = "";
                    String head = "";
                    if (bytes != null) {
                        result = new String(bytes);
                        BaseResponse response = JsonHandler.getHandler().getTarget(result, BaseResponse.class);
                        if (response.getCode() == 1) {
                            requestSuccess(result, requestCode);
                        } else {
                            requestFailure(response.getMessage(), requestCode);
                        }

                    }
                }

                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                    {
                        String result = "";
                        if (bytes != null) {
                            result = new String(bytes);
                            requestFailure(result, requestCode);
                        }
                    }
                }

            });


        }
    }


}
