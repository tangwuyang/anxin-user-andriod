package com.anxin.kitchen.bean;


import com.anxin.kitchen.MyApplication;

import java.io.Serializable;

/**
 * 添加地址封装类
 */

public class AddressBean implements Serializable {

    private String addressID;//地址ID
    private String userID;//用户ID
    private String phoneNumber;//联系人电话
    private String isDefault;//是否默认
    private String contactName;//联系人名称
    private String provinceName;//省
    private String provinceID;//省ID
    private String cityName;//市
    private String cityID;//市ID
    private String districtName;//区
    private String districtID;//区ID
    private String streetName;//街道
    private String address;//地址
    private String longitude;//经度
    private String latitude;//纬度

    public String getProvinceName() {
        if (provinceName == null || provinceName.length() <= 0) {
            if (provinceID != null && provinceID.length() > 0)
                provinceName = MyApplication.getInstance().getAddressIDToName(provinceID);
        }
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
        if (provinceID == null || provinceID.length() <= 0) {
            if (provinceName != null && provinceName.length() > 0)
                provinceID = MyApplication.getInstance().getAddressNameToID(provinceName);
        }
        return provinceID;
    }

    /**
     * 获取市ID
     *
     * @return
     */
    public String getCityID() {
        if (cityID == null || cityID.length() <= 0) {
            if (cityName != null && cityName.length() > 0)
                cityID = MyApplication.getInstance().getAddressNameToID(cityName);
        }
        return cityID;
    }

    /**
     * 获取区ID
     *
     * @return
     */
    public String getDistrictID() {
        if (districtID == null || districtID.length() <= 0) {
            if (districtName != null && districtName.length() > 0)
                districtID = MyApplication.getInstance().getAddressNameToID(districtName);
        }
        return districtID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
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
        if (cityName == null || cityName.length() <= 0) {
            if (cityID != null && cityID.length() > 0)
                cityName = MyApplication.getInstance().getAddressIDToName(cityID);
        }
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        if (districtName == null || districtName.length() <= 0) {
            if (districtID != null && districtID.length() > 0)
                districtName = MyApplication.getInstance().getAddressIDToName(districtID);
        }
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

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "addressID='" + addressID + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isDefault='" + isDefault + '\'' +
                ", contactName='" + contactName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", streetName='" + streetName + '\'' +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
