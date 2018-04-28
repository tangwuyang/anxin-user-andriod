package com.anxin.kitchen.bean;


import com.bluetooth.tangwuyang.fantuanlibrary.entity.BaseEntity;

/**
 * @author: laohu on 2016/12/17
 * @site: http://ittiger.cn
 */

public class ContactEntity implements BaseEntity {

    private String name;
    private String avatar;
    private String mobile;
    private int groupId;
    private String userLogo;
    private int groupUserId;
    private boolean hasContained = false;
    public ContactEntity(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }


    public boolean isHasContained() {
        return hasContained;
    }

    public void setHasContained(boolean hasContained) {
        this.hasContained = hasContained;
    }

    public ContactEntity(String name, String mobile, int groupId, String userLogo) {
        this.name = name;
        this.mobile = mobile;
        this.groupId = groupId;
        this.userLogo = userLogo;
    }

    public ContactEntity(String name, String mobile, int groupId, String userLogo, int groupUserId) {
        this.name = name;
        this.mobile = mobile;
        this.groupId = groupId;
        this.userLogo = userLogo;
        this.groupUserId = groupUserId;
    }

    public int getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(int groupUserId) {
        this.groupUserId = groupUserId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    @Override
    public String getIndexField() {

        return name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getAvatar() {

        return avatar;
    }

    public void setAvatar(String avatar) {

        this.avatar = avatar;
    }

    public String getMobile() {

        return mobile;
    }

    public void setMobile(String mobile) {

        this.mobile = mobile;
    }
}
