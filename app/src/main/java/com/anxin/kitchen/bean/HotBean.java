package com.anxin.kitchen.bean;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/4/16.
 * 热搜类
 */

public class HotBean {

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
        private String keywords;
        private int sortIndex;
        private int createTime;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }
        public String getKeywords() {
            return keywords;
        }

        public void setSortIndex(int sortIndex) {
            this.sortIndex = sortIndex;
        }
        public int getSortIndex() {
            return sortIndex;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }
        public int getCreateTime() {
            return createTime;
        }

    }
}
