package com.anxin.kitchen.bean.Order;

import java.io.Serializable;

/**
 * 订单对象
 * Created by xujianjun on 2018/4/6.
 */

public class Order implements Serializable {
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

    /**
     * 获取订单类型---用于支付
     * 1个人订单 2团订单_统一付款 3团订单_AA付款
     *
     * @return
     */
    public int getOrderType() {
        if (group == null) {
            return 1;
        } else {
            if (group.getUserId() == user.getUserId() && group.getPayType() == 1) {
                return 2;
            } else {
                return 3;
            }
        }
    }


}