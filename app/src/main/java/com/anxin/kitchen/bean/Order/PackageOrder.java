package com.anxin.kitchen.bean.Order;

/**
 * Created by xujianjun on 2018/4/6.
 */

public class PackageOrder {
    private String packageName;//套餐名称
    private String foods;//配菜 ,号隔开
    private int num;//数量
    private double subtotal;//金额

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFoods() {
        return foods;
    }

    public void setFoods(String foods) {
        this.foods = foods;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

}
