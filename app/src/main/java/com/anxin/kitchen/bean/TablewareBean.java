package com.anxin.kitchen.bean;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/4/7.
 */

public class TablewareBean {
    private int curPage;
    private int maxPage;
    private int total;
    private List<Data> data;
    private boolean empty;
    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }
    public int getCurPage() {
        return curPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
    public int getMaxPage() {
        return maxPage;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
    public boolean getEmpty() {
        return empty;
    }


    public class Data {

        private int id;
        private String name;
        private int sortIndex;
        private int useType;
        private int orderType;
        private String brand;
        private String model;
        private double cost;
        private double usePrice;
        private double deposit;
        private String img;
        private long createTime;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setSortIndex(int sortIndex) {
            this.sortIndex = sortIndex;
        }
        public int getSortIndex() {
            return sortIndex;
        }

        public void setUseType(int useType) {
            this.useType = useType;
        }
        public int getUseType() {
            return useType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }
        public int getOrderType() {
            return orderType;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }
        public String getBrand() {
            return brand;
        }

        public void setModel(String model) {
            this.model = model;
        }
        public String getModel() {
            return model;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }
        public double getCost() {
            return cost;
        }

        public void setUsePrice(double usePrice) {
            this.usePrice = usePrice;
        }
        public double getUsePrice() {
            return usePrice;
        }

        public void setDeposit(double deposit) {
            this.deposit = deposit;
        }
        public double getDeposit() {
            return deposit;
        }

        public void setImg(String img) {
            this.img = img;
        }
        public String getImg() {
            return img;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
        public long getCreateTime() {
            return createTime;
        }

    }
}
