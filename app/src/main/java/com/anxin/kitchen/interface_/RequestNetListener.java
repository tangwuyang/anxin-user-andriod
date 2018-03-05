package com.anxin.kitchen.interface_;

/**
 * Created by 唐午阳 on 2018/3/5.
 */

public interface RequestNetListener {
    void requestSuccess(String responseBody,String requestCode);
    void requestFailure(String responseFailureBody,String requestCode);
}
