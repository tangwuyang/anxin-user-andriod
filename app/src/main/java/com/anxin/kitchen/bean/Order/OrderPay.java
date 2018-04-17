package com.anxin.kitchen.bean.Order;

/**
 * 订单支付金额
 * Created by xujianjun on 2018/4/15.
 */

public class OrderPay {
    private double payMoney;//订单除了押金的金额
    private double payDeposit;//订单的押金
    private double totalPay;//订单总金额
    private double wantPay;//订单需要支付的金额,微信支付或者支付宝需要支付的金额

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public double getPayDeposit() {
        return payDeposit;
    }

    public void setPayDeposit(double payDeposit) {
        this.payDeposit = payDeposit;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    public double getWantPay() {
        return wantPay;
    }

    public void setWantPay(double wantPay) {
        this.wantPay = wantPay;
    }

}
