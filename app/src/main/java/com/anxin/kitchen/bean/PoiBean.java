package com.anxin.kitchen.bean;

import com.amap.api.services.core.LatLonPoint;

/**
 * Created by 唐午阳 on 2018/3/22.
 */

public class PoiBean {
    private String titleName;
    private String cityName;
    private String ad;
    private String snippet;
    private LatLonPoint point;

    public PoiBean() {
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public LatLonPoint getPoint() {
        return point;
    }

    public void setPoint(LatLonPoint point) {
        this.point = point;
    }
}
