package com.anxin.kitchen.bean;

/**
 * Created by 唐午阳 on 2018/4/17.
 */

public class PreMoneyBean {
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

        private double payDeposit;
        private double totalPay;
        private double wantPay;
        public void setPayDeposit(double payDeposit) {
            this.payDeposit = payDeposit;
        }
        public double getPayDeposit() {
            return payDeposit;
        }

        public void setTotalPay(double totalPay) {
            this.totalPay = totalPay;
        }
        public double getTotalPay() {
            return totalPay;
        }

        public void setWantPay(double wantPay) {
            this.wantPay = wantPay;
        }
        public double getWantPay() {
            return wantPay;
        }

    }
}
