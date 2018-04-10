package com.anxin.kitchen.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 唐午阳 on 2018/2/10.
 */

public class Message implements Serializable {

    private String msID;//ID
    private String msType;//类型 "1": "系统通知","2": "活动通知","50": "其他"
    private String userID;//用户ID,0:系统，1:用户
    private String msStatus;//发送状态
    private String msTitle;//标题
    private String msContent;//内容
    private String msLink;//链接
    private String msPublishTime;//发布时间
    private String msCreateTime;//创建时间

    public String getMsID() {
        return msID;
    }

    public void setMsID(String msID) {
        this.msID = msID;
    }

    public String getMsType() {
        return msType;
    }

    public void setMsType(String msType) {
        this.msType = msType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMsStatus() {
        return msStatus;
    }

    public void setMsStatus(String msStatus) {
        this.msStatus = msStatus;
    }

    public String getMsTitle() {
        return msTitle;
    }

    public void setMsTitle(String msTitle) {
        this.msTitle = msTitle;
    }

    public String getMsContent() {
        return msContent;
    }

    public void setMsContent(String msContent) {
        this.msContent = msContent;
    }

    public String getMsLink() {
        return msLink;
    }

    public void setMsLink(String msLink) {
        this.msLink = msLink;
    }

    public String getMsPublishTime() {
        return msPublishTime;
    }

    public void setMsPublishTime(String msPublishTime) {
        this.msPublishTime = msPublishTime;
    }

    public String getMsCreateTime() {
        return msCreateTime;
    }

    public void setMsCreateTime(String msCreateTime) {
        this.msCreateTime = msCreateTime;
    }
}
