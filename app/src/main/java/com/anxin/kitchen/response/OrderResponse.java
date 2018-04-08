package com.anxin.kitchen.response;

import com.anxin.kitchen.bean.Order.Order;

/**
 * Created by xujianjun on 2018/4/8.
 */

public class OrderResponse extends BaseResponse {
    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }

    private Order data;
}
