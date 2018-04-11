package com.anxin.kitchen.bean.Order;

/**
 * Created by xujianjun on 2018/4/7.
 */

public class OrderDetail {
    private GroupOrder group;
    private UserOrder user;
    private ContactOrder contact;//配送信息
    private long lastPayTime;//最后支付时间

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

    public ContactOrder getContact() {
        return contact;
    }

    public void setContact(ContactOrder contact) {
        this.contact = contact;
    }

    public long getLastPayTime() {
        return lastPayTime;
    }

    public void setLastPayTime(long lastPayTime) {
        this.lastPayTime = lastPayTime;
    }


}