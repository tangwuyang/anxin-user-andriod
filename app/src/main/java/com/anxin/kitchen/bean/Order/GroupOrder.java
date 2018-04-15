package com.anxin.kitchen.bean.Order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xujianjun on 2018/4/6.
 */

public class GroupOrder implements Serializable{
    private long userId;//发起人ID
    private long id;//团购订单ID
    private int status;//状态 0:未付款,1:已付款,2:已发货,3:完成,4:退款
    private int makeType;//订单类型,1:普通,2:食疗
    private int payType;//付费方式,1:代付，2:AA付费
    private long eatingTime;//用餐时间
    private long createTime;
    private int size;//份量大小,1:成人餐,2:儿童餐
    private long kitchenId;//厨房ID
    private long packageId;//套餐ID
    private double money;//总费用
    private int num;//总份数量
    private double mealFee;//餐费
    private long tablewareId;//餐具ID
    private int tablewareType;//使用类型,1:一次性,2:可回收
    private double tablewareFee;//餐具费用
    private double deliveryFee;// 配送费用(总)
    private String contactPhone;//联系人电话
    private String contactName;//联系人姓名
    private String address;//就餐地址
    private int eatingType;//午晚餐,1:午餐,2:晚餐
    private long deliveryId;//快递公司ID
    private long courierId;//快递人员ID
    private String courierName;//快递人员名称
    private String packagesName;//套餐名称（多）
    private long deliveryTime;//配送时间
    private List<OrderUser> orderUsers;//组员信息

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMakeType() {
        return makeType;
    }

    public void setMakeType(int makeType) {
        this.makeType = makeType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public long getEatingTime() {
        return eatingTime;
    }

    public void setEatingTime(Long eatingTime) {
        this.eatingTime = eatingTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setEatingTime(long eatingTime) {
        this.eatingTime = eatingTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(long kitchenId) {
        this.kitchenId = kitchenId;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getMealFee() {
        return mealFee;
    }

    public void setMealFee(double mealFee) {
        this.mealFee = mealFee;
    }

    public long getTablewareId() {
        return tablewareId;
    }

    public void setTablewareId(long tablewareId) {
        this.tablewareId = tablewareId;
    }

    public int getTablewareType() {
        return tablewareType;
    }

    public void setTablewareType(int tablewareType) {
        this.tablewareType = tablewareType;
    }

    public double getTablewareFee() {
        return tablewareFee;
    }

    public void setTablewareFee(double tablewareFee) {
        this.tablewareFee = tablewareFee;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

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

    public int getEatingType() {
        return eatingType;
    }

    public void setEatingType(int eatingType) {
        this.eatingType = eatingType;
    }

    public long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public long getCourierId() {
        return courierId;
    }

    public void setCourierId(long courierId) {
        this.courierId = courierId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getPackagesName() {
        return packagesName;
    }

    public void setPackagesName(String packagesName) {
        this.packagesName = packagesName;
    }

    public long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<OrderUser> getOrderUsers() {
        return orderUsers;
    }

    public void setOrderUsers(List<OrderUser> orderUsers) {
        this.orderUsers = orderUsers;
    }


}
