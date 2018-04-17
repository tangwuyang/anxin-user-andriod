package com.anxin.kitchen.response;

import com.anxin.kitchen.bean.Order.OrderPay;

/**
 * Created by xujianjun on 2018/4/15.
 */

public class OrderPayResponse extends BaseResponse {
    private OrderPay data;

    public OrderPay getData() {
        return data;
    }

    public void setData(OrderPay data) {
        this.data = data;
    }


}
