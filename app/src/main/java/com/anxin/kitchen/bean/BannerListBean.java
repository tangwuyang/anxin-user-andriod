package com.anxin.kitchen.bean;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/3/21.
 */

public class BannerListBean {

    private int code;
    private String message;
    private List<Data> data;
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

    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }


    public class Data {

        private int id;
        private int kitchenId;
        private String bannerName;
        private String img;
        private int clickType;
        private String clickData;
        private int sortIndex;
        private long startTime;
        private long stopTime;
        private long createTime;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setKitchenId(int kitchenId) {
            this.kitchenId = kitchenId;
        }
        public int getKitchenId() {
            return kitchenId;
        }

        public void setBannerName(String bannerName) {
            this.bannerName = bannerName;
        }
        public String getBannerName() {
            return bannerName;
        }

        public void setImg(String img) {
            this.img = img;
        }
        public String getImg() {
            return img;
        }

        public void setClickType(int clickType) {
            this.clickType = clickType;
        }
        public int getClickType() {
            return clickType;
        }

        public void setClickData(String clickData) {
            this.clickData = clickData;
        }
        public String getClickData() {
            return clickData;
        }

        public void setSortIndex(int sortIndex) {
            this.sortIndex = sortIndex;
        }
        public int getSortIndex() {
            return sortIndex;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
        public long getStartTime() {
            return startTime;
        }

        public void setStopTime(long stopTime) {
            this.stopTime = stopTime;
        }
        public long getStopTime() {
            return stopTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
        public long getCreateTime() {
            return createTime;
        }

    }

}
