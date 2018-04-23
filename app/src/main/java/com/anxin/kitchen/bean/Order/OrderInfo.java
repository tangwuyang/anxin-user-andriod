package com.anxin.kitchen.bean.Order;

import java.io.Serializable;
import java.util.List;

/**
 * 订单列表使用
 * Created by xujianjun on 2018/4/22.
 */

public class OrderInfo implements Serializable {
    private int curPage;
    private int maxPage;
    private int total;
    private List<Order> data;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }

}
