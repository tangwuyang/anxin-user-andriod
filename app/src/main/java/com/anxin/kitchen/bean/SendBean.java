package com.anxin.kitchen.bean;

/**
 * Created by 唐午阳 on 2018/4/13.
 * 获取配送消息
 *
 */

public class SendBean {
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

        private int kitchenId;
        private int moq;
        private double deliveryGroupPrice;
        private double deliveryDietPrice;
        private double sauceCosts;
        private double kitchenCosts;
        private double profitRate;
        private int dietOpenTime;
        private long dietEndTime;
        public void setKitchenId(int kitchenId) {
            this.kitchenId = kitchenId;
        }
        public int getKitchenId() {
            return kitchenId;
        }

        public void setMoq(int moq) {
            this.moq = moq;
        }
        public int getMoq() {
            return moq;
        }

        public void setDeliveryGroupPrice(double deliveryGroupPrice) {
            this.deliveryGroupPrice = deliveryGroupPrice;
        }
        public double getDeliveryGroupPrice() {
            return deliveryGroupPrice;
        }

        public void setDeliveryDietPrice(double deliveryDietPrice) {
            this.deliveryDietPrice = deliveryDietPrice;
        }
        public double getDeliveryDietPrice() {
            return deliveryDietPrice;
        }

        public void setSauceCosts(double sauceCosts) {
            this.sauceCosts = sauceCosts;
        }
        public double getSauceCosts() {
            return sauceCosts;
        }

        public void setKitchenCosts(double kitchenCosts) {
            this.kitchenCosts = kitchenCosts;
        }
        public double getKitchenCosts() {
            return kitchenCosts;
        }

        public void setProfitRate(double profitRate) {
            this.profitRate = profitRate;
        }
        public double getProfitRate() {
            return profitRate;
        }

        public void setDietOpenTime(int dietOpenTime) {
            this.dietOpenTime = dietOpenTime;
        }
        public int getDietOpenTime() {
            return dietOpenTime;
        }

        public void setDietEndTime(long dietEndTime) {
            this.dietEndTime = dietEndTime;
        }
        public long getDietEndTime() {
            return dietEndTime;
        }

    }
}
