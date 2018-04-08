package com.anxin.kitchen.bean.Order;

/**
 * 订单用户对象---组成员信息
 * Created by xujianjun on 2018/4/8.
 */

public class OrderUser {
    private long userId;
    private String trueName;
    private String userLogo;
    private int status;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
