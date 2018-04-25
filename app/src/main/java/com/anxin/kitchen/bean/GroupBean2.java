package com.anxin.kitchen.bean;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/4/25.
 */

public class GroupBean2 {


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
        private int groupId;
        private String phone;
        private String initials;
        private String nickName;
        private String trueName;
        private String personality;
        private int sex;
        private String userLogo;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }
        public int getGroupId() {
            return groupId;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
        public String getPhone() {
            return phone;
        }

        public void setInitials(String initials) {
            this.initials = initials;
        }
        public String getInitials() {
            return initials;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
        public String getNickName() {
            return nickName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }
        public String getTrueName() {
            return trueName;
        }

        public void setPersonality(String personality) {
            this.personality = personality;
        }
        public String getPersonality() {
            return personality;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
        public int getSex() {
            return sex;
        }

        public void setUserLogo(String userLogo) {
            this.userLogo = userLogo;
        }
        public String getUserLogo() {
            return userLogo;
        }

    }
}
