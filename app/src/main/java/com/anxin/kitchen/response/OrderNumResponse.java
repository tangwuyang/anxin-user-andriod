package com.anxin.kitchen.response;

import java.util.HashMap;

/**
 * Created by xujianjun on 2018/4/8.
 */

public class OrderNumResponse extends  BaseResponse{
    public HashMap<String, Integer> getData() {
        return data;
    }

    public void setData(HashMap<String, Integer> data) {
        this.data = data;
    }

    private HashMap<String,Integer> data;
}
