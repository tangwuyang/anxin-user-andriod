package com.anxin.kitchen.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 唐午阳 on 2018/2/10.
 */

public class MessageBean implements Serializable {
    private String publishTime = "0";//最近一次获取时间
    private List<Message> OrderMessageList = new ArrayList<>();//订单通知列表
    private List<Message> UpdateMessageList = new ArrayList<>();//订单通知列表
    private List<Message> ActivityMessageList = new ArrayList<>();//订单通知列表

    public MessageBean() {

    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public List<Message> getOrderMessageList() {
        return OrderMessageList;
    }

    public void setOrderMessageList(List<Message> orderMessageList) {
        OrderMessageList = orderMessageList;
    }

    public List<Message> getUpdateMessageList() {
        return UpdateMessageList;
    }

    public void setUpdateMessageList(List<Message> updateMessageList) {
        UpdateMessageList = updateMessageList;
    }

    public List<Message> getActivityMessageList() {
        return ActivityMessageList;
    }

    public void setActivityMessageList(List<Message> activityMessageList) {
        ActivityMessageList = activityMessageList;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "publishTime='" + publishTime + '\'' +
                ", OrderMessageList=" + OrderMessageList.toString() +
                ", UpdateMessageList=" + UpdateMessageList.toString() +
                ", ActivityMessageList=" + ActivityMessageList.toString() +
                '}';
    }
}
