package com.anxin.kitchen.bean;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/4/1.
 */

public class RecorverMenuBean {
    private int code;

    private String message;

    private List<Data> data ;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setData(List<Data> data){
        this.data = data;
    }
    public List<Data> getData(){
        return this.data;
    }


    public static class Data {
        private int id;

        private String dietName;

        private String img;

        private String desc;

        private int createTime;
        private int counts;

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setDietName(String dietName){
            this.dietName = dietName;
        }
        public String getDietName(){
            return this.dietName;
        }
        public void setImg(String img){
            this.img = img;
        }
        public String getImg(){
            return this.img;
        }
        public void setDesc(String desc){
            this.desc = desc;
        }
        public String getDesc(){
            return this.desc;
        }
        public void setCreateTime(int createTime){
            this.createTime = createTime;
        }
        public int getCreateTime(){
            return this.createTime;
        }

    }
}
