package com.anxin.kitchen.response;

import com.anxin.kitchen.bean.Order.PayWeixinInfo;

/**
 * Created by xujianjun on 2018/4/11.
 */

public class PayWeixinInfoResponse extends BaseResponse {
    private PayWeixinInfo data;

    public PayWeixinInfo getData() {
        return data;
    }

    public void setData(PayWeixinInfo data) {
        this.data = data;
    }


}
