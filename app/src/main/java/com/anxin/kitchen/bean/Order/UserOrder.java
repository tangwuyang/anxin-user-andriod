package com.anxin.kitchen.bean.Order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xujianjun on 2018/4/6.
 */

public class UserOrder implements Serializable {
    private long id;//订单id
    private long userId;
    private int size;//份量大小,1:成人餐,2:儿童餐
    private int num;//份数
    private int status;//状态 0:未付款,1:已付款,2:已发货,3:完成,4:退款
    private int makeType;//订单类型 1康复 2预约
    private int payType;//1微信2支付宝
    private double money;//总价 包括餐具 配送费
    private double mealFee;//套餐总价格
    private double tablewareFee;//餐具使用费
    private double deliveryFee;//配送费
    private int eatingType;//午晚餐,1:午餐,2:晚餐
    private int tablewareType;//餐具类型 1:一次性,2:可回收
    private double tablewareDeposit;//餐具总押金
    private String tablewareName;//餐具名称
    private long createTime;//创建时间
    private long deliveryTime;//配送时间
    private long eatingTime;//用餐时间
    private long groupOrderId;//团订单ID
    private long kitchenId;//厨房ID
    private long tablewareId;//餐具id
    private String contactPhone;//联系人手机
    private String contactName;//联系人手机
    private String address;//地址
    private long deliveryId;//快递公司ID
    private long courierId;//快递人员ID
    private String courierName;//快递人员名称
    private String packagesName;//套餐名称（多）
    private long updateTime;//更新时间
    private List<PackageOrder> packageList;//套餐

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getMealFee() {
        return mealFee;
    }

    public void setMealFee(double mealFee) {
        this.mealFee = mealFee;
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

    public int getEatingType() {
        return eatingType;
    }

    public void setEatingType(int eatingType) {
        this.eatingType = eatingType;
    }

    public int getTablewareType() {
        return tablewareType;
    }

    public void setTablewareType(int tablewareType) {
        this.tablewareType = tablewareType;
    }

    public double getTablewareDeposit() {
        return tablewareDeposit;
    }

    public void setTablewareDeposit(double tablewareDeposit) {
        this.tablewareDeposit = tablewareDeposit;
    }

    public String getTablewareName() {
        return tablewareName;
    }

    public void setTablewareName(String tablewareName) {
        this.tablewareName = tablewareName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<PackageOrder> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<PackageOrder> packageList) {
        this.packageList = packageList;
    }


    public long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public long getEatingTime() {
        return eatingTime;
    }

    public void setEatingTime(long eatingTime) {
        this.eatingTime = eatingTime;
    }

    public long getGroupOrderId() {
        return groupOrderId;
    }

    public void setGroupOrderId(long groupOrderId) {
        this.groupOrderId = groupOrderId;
    }

    public long getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(long kitchenId) {
        this.kitchenId = kitchenId;
    }

    public long getTablewareId() {
        return tablewareId;
    }

    public void setTablewareId(long tablewareId) {
        this.tablewareId = tablewareId;
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }


}
