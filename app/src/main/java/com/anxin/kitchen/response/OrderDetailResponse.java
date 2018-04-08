package com.anxin.kitchen.response;

import com.anxin.kitchen.bean.Order.OrderDetail;

/**
 * Created by xujianjun on 2018/4/8.
 */

public class OrderDetailResponse extends BaseResponse {
    private OrderDetail data;

    public OrderDetail getData() {
        return data;
    }

    public void setData(OrderDetail data) {
        this.data = data;
    }


}
