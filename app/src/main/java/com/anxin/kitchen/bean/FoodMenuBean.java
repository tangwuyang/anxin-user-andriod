package com.anxin.kitchen.bean;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/3/19.
 *
 * 菜系类
 */

public class FoodMenuBean {
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
        private String cuisineName;
        private String img;
        private String desc;
        private long createTime;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setCuisineName(String cuisineName) {
            this.cuisineName = cuisineName;
        }
        public String getCuisineName() {
            return cuisineName;
        }

        public void setImg(String img) {
            this.img = img;
        }
        public String getImg() {
            return img;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
        public String getDesc() {
            return desc;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
        public long getCreateTime() {
            return createTime;
        }

    }
}
