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

    /**
     * 是否是统一支付
     *
     * @return
     */
    public boolean isAllPay() {
        if (getGroup() != null && getGroup().getUserId() == getUser().getUserId() && getGroup().getPayType() == 1) {
            //统一付款
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是AA支付
     *
     * @return
     */
    public boolean isAAPay() {
        if (getGroup() != null && getGroup().getUserId() == getUser().getUserId() && getGroup().getPayType() == 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取订单
     *
     * @return
     */
    public long getOrderId() {
        if (group != null) {
            return group.getId();
        } else {
            return user.getId();
        }
    }


}
