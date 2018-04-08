package com.anxin.kitchen.bean.Order;

/**
 * 订单对象
 * Created by xujianjun on 2018/4/6.
 */

public class Order {
    private GroupOrder group;
    private UserOrder user;

    public GroupOrder getGroup() {
        return group;
    }

    public void setGroup(GroupOrder group) {
        this.group = group;
    }

    public UserOrder getUser() {
        return user;
    }

    public void setUser(UserOrder user) {
        this.user = user;
    }


}