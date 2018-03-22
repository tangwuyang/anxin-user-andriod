package com.anxin.kitchen.bean;

/**
 * Created by 唐午阳 on 2018/3/21.
 */

public class GroupBean {

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

        private int id;
        private int userId;
        private int groupNum;
        private String groupName;
        private String groupDesc;
        private String groupLogo;
        private long createTime;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
        }

        public void setGroupNum(int groupNum) {
            this.groupNum = groupNum;
        }
        public int getGroupNum() {
            return groupNum;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
        public String getGroupName() {
            return groupName;
        }

        public void setGroupDesc(String groupDesc) {
            this.groupDesc = groupDesc;
        }
        public String getGroupDesc() {
            return groupDesc;
        }

        public void setGroupLogo(String groupLogo) {
            this.groupLogo = groupLogo;
        }
        public String getGroupLogo() {
            return groupLogo;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
        public long getCreateTime() {
            return createTime;
        }

    }
}
