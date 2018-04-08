package com.anxin.kitchen.response;

import com.anxin.kitchen.bean.Order.Order;

import java.util.List;

/**
 * 订单列表响应
 * Created by xujianjun on 2018/4/6.
 */

public class OrderListResponse extends BaseResponse{

    private List<Order> data;

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
