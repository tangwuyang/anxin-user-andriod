package com.anxin.kitchen.bean;

/**
 * Created by 唐午阳 on 2018/2/10.
 */

public class MessageBean {
    private String imgUrl;
    private int messageType;
    private String messageTitle;
    private String messageContent;
    private String messageTime;

    public MessageBean() {

    }

    public MessageBean(String imgUrl, int messageType, String messageTitle, String messageContent) {
        this.imgUrl = imgUrl;
        this.messageType = messageType;
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        messageType = messageType;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
