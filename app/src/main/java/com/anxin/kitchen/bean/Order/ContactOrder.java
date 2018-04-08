package com.anxin.kitchen.bean.Order;

/**
 * 配送信息
 * Created by xujianjun on 2018/4/8.
 */

public class ContactOrder {
    private String contactPhone;//电话
    private String contactName;//姓名
    private String address;//地址

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
