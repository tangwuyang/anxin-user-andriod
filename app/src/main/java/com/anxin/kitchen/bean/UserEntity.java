package com.anxin.kitchen.bean;


import com.bluetooth.tangwuyang.fantuanlibrary.entity.BaseEntity;

/**
 * Created by ylhu on 16-12-21.
 */
public class UserEntity implements BaseEntity {

    private String name;
    private String avatar;
    private String mobile;

    public UserEntity(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
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
