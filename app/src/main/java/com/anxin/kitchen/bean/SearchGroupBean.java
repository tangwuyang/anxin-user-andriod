package com.anxin.kitchen.bean;

import com.bluetooth.tangwuyang.fantuanlibrary.entity.BaseEntity;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/3/7.
 */

public class SearchGroupBean {
    private int curPage;
    private int maxPage;
    private int total;
    private List<Data> data;
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

    public class Data implements BaseEntity {

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

        @Override
        public String getIndexField() {
            return null;
        }
    }
}
