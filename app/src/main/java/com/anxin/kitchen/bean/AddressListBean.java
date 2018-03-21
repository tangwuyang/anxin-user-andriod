package com.anxin.kitchen.bean;


import java.io.Serializable;

/**
 * 省市县地址ID信息
 * {"id":130700,"name":"张家口市","parentId":130000,"shortName":"张家口","levelType":2,"cityCode":"0313","zipCode":"075000","lng":"114.884091","lat":"40.811901","pinyin":"Zhangjiakou"},
 */

public class AddressListBean implements Serializable {

    private String adID;//ID
    private String adName;//名称
    private String adParenID;//父ID
    private String adShortName;//简称
    private String adLevelType;//层级
    private String adCityCode;//城市代码
    private String adZipCode;//邮编
    private String adLng;//经度
    private String adLat;//纬度
    private String adPinyin;//拼音

    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdParenID() {
        return adParenID;
    }

    public void setAdParenID(String adParenID) {
        this.adParenID = adParenID;
    }

    public String getAdShortName() {
        return adShortName;
    }

    public void setAdShortName(String adShortName) {
        this.adShortName = adShortName;
    }

    public String getAdLevelType() {
        return adLevelType;
    }

    public void setAdLevelType(String adLevelType) {
        this.adLevelType = adLevelType;
    }

    public String getAdCityCode() {
        return adCityCode;
    }

    public void setAdCityCode(String adCityCode) {
        this.adCityCode = adCityCode;
    }

    public String getAdZipCode() {
        return adZipCode;
    }

    public void setAdZipCode(String adZipCode) {
        this.adZipCode = adZipCode;
    }

    public String getAdLng() {
        return adLng;
    }

    public void setAdLng(String adLng) {
        this.adLng = adLng;
    }

    public String getAdLat() {
        return adLat;
    }

    public void setAdLat(String adLat) {
        this.adLat = adLat;
    }

    public String getAdPinyin() {
        return adPinyin;
    }

    public void setAdPinyin(String adPinyin) {
        this.adPinyin = adPinyin;
    }

    @Override
    public String toString() {
        return "AddressListBean{" +
                "adID='" + adID + '\'' +
                ", adName='" + adName + '\'' +
                ", adParenID='" + adParenID + '\'' +
                ", adShortName='" + adShortName + '\'' +
                ", adLevelType='" + adLevelType + '\'' +
                ", adCityCode='" + adCityCode + '\'' +
                ", adZipCode='" + adZipCode + '\'' +
                ", adLng='" + adLng + '\'' +
                ", adLat='" + adLat + '\'' +
                ", adPinyin='" + adPinyin + '\'' +
                '}';
    }
}
