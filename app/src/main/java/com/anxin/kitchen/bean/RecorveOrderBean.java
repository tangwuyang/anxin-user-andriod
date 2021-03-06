package com.anxin.kitchen.bean;

/**
 * Created by 唐午阳 on 2018/4/19.
 */

public class RecorveOrderBean {
    private int code;
    private String message;
    private Data data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }


    public class Data {

        private long id;
        private int groupOrderId;
        private int kitchenId;
        private int userId;
        private int size;
        private int status;
        private int makeType;
        private int num;
        private int payType;
        private double money;
        private double mealFee;
        private int tablewareId;
        private int tablewareType;
        private double tablewareFee;
        private double deliveryFee;
        private int deposit;
        private String contactPhone;
        private String contactName;
        private String address;
        private int eatingType;
        private long eatingTime;
        private String deliveryTime;
        private String deliveryId;
        private String courierId;
        private String courierName;
        private String packagesName;
        private long createTime;
        private String updateTime;
        public void setId(long id) {
            this.id = id;
        }
        public long getId() {
            return id;
        }

        public void setGroupOrderId(int groupOrderId) {
            this.groupOrderId = groupOrderId;
        }
        public int getGroupOrderId() {
            return groupOrderId;
        }

        public void setKitchenId(int kitchenId) {
            this.kitchenId = kitchenId;
        }
        public int getKitchenId() {
            return kitchenId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
        }

        public void setSize(int size) {
            this.size = size;
        }
        public int getSize() {
            return size;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }

        public void setMakeType(int makeType) {
            this.makeType = makeType;
        }
        public int getMakeType() {
            return makeType;
        }

        public void setNum(int num) {
            this.num = num;
        }
        public int getNum() {
            return num;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }
        public int getPayType() {
            return payType;
        }

        public void setMoney(double money) {
            this.money = money;
        }
        public double getMoney() {
            return money;
        }

        public void setMealFee(double mealFee) {
            this.mealFee = mealFee;
        }
        public double getMealFee() {
            return mealFee;
        }

        public void setTablewareId(int tablewareId) {
            this.tablewareId = tablewareId;
        }
        public int getTablewareId() {
            return tablewareId;
        }

        public void setTablewareType(int tablewareType) {
            this.tablewareType = tablewareType;
        }
        public int getTablewareType() {
            return tablewareType;
        }

        public void setTablewareFee(double tablewareFee) {
            this.tablewareFee = tablewareFee;
        }
        public double getTablewareFee() {
            return tablewareFee;
        }

        public void setDeliveryFee(double deliveryFee) {
            this.deliveryFee = deliveryFee;
        }
        public double getDeliveryFee() {
            return deliveryFee;
        }

        public void setDeposit(int deposit) {
            this.deposit = deposit;
        }
        public int getDeposit() {
            return deposit;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }
        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }
        public String getContactName() {
            return contactName;
        }

        public void setAddress(String address) {
            this.address = address;
        }
        public String getAddress() {
            return address;
        }

        public void setEatingType(int eatingType) {
            this.eatingType = eatingType;
        }
        public int getEatingType() {
            return eatingType;
        }

        public void setEatingTime(long eatingTime) {
            this.eatingTime = eatingTime;
        }
        public long getEatingTime() {
            return eatingTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }
        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryId(String deliveryId) {
            this.deliveryId = deliveryId;
        }
        public String getDeliveryId() {
            return deliveryId;
        }

        public void setCourierId(String courierId) {
            this.courierId = courierId;
        }
        public String getCourierId() {
            return courierId;
        }

        public void setCourierName(String courierName) {
            this.courierName = courierName;
        }
        public String getCourierName() {
            return courierName;
        }

        public void setPackagesName(String packagesName) {
            this.packagesName = packagesName;
        }
        public String getPackagesName() {
            return packagesName;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
        public long getCreateTime() {
            return createTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
        public String getUpdateTime() {
            return updateTime;
        }

    }
}
