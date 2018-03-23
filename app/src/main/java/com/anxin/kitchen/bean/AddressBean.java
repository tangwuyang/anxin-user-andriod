package com.anxin.kitchen.bean;


import com.anxin.kitchen.MyApplication;

import java.io.Serializable;

/**
 * 添加地址封装类
 */

public class AddressBean implements Serializable {

    private String phoneNumber;//联系人电话
    private String isDefault;//是否默认
    private String contactName;//联系人名称
    private String provinceName;//省
    private String cityName;//市
    private String districtName;//区
    private String streetName;//街道
    private String address;//地址
    private String longitude;//经度
    private String latitude;//纬度

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * 获取省ID
     *
     * @return
     */
    public String getProvinceID() {
        return MyApplication.getInstance().getAddressNameToID(provinceName);
    }

    /**
     * 获取市ID
     *
     * @return
     */
    public String getCityID() {
        return MyApplication.getInstance().getAddressNameToID(cityName);
    }

    /**
     * 获取区ID
     *
     * @return
     */
    public String getDistrictID() {
        return MyApplication.getInstance().getAddressNameToID(districtName);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", streetName='" + streetName + '\'' +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
