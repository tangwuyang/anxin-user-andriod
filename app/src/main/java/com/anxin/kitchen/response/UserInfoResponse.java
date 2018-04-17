package com.anxin.kitchen.response;

import com.anxin.kitchen.bean.Order.User;

/**
 * 用户信息响应
 * Created by xujianjun on 2018/4/10.
 */

public class UserInfoResponse extends BaseResponse {
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }


}
