package com.anxin.kitchen.activity;

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

        private int payDeposit;
        private int totalPay;
        private int wantPay;
        public void setPayDeposit(int payDeposit) {
            this.payDeposit = payDeposit;
        }
        public int getPayDeposit() {
            return payDeposit;
        }

        public void setTotalPay(int totalPay) {
            this.totalPay = totalPay;
        }
        public int getTotalPay() {
            return totalPay;
        }

        public void setWantPay(int wantPay) {
            this.wantPay = wantPay;
        }
        public int getWantPay() {
            return wantPay;
        }

    }
}
