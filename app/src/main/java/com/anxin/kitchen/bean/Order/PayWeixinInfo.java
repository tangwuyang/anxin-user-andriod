package com.anxin.kitchen.bean.Order;

/**
 * 微信支付签名信息
 * Created by xujianjun on 2018/4/11.
 */

public class PayWeixinInfo {
    private String noncestr;//随机数
    private String signType;//MD5
    private String package_rname;
    private String timestamp;//时间戳
    private String appid;
    private String sign;//签名
    private String code_url;
    private String partnerid;//商户号
    private String prepayid;//统一下单里面返回的标识符


    public String getNonceStr() {
        return noncestr;
    }

    public void setNonceStr(String nonceStr) {
        this.noncestr = nonceStr;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPackage_rname() {
        return package_rname;
    }

    public void setPackage_rname(String package_rname) {
        this.package_rname = package_rname;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timestamp = timeStamp;
    }

    public String getAppId() {
        return appid;
    }

    public void setAppId(String appId) {
        this.appid = appId;
    }

    public String getPaySign() {
        return sign;
    }

    public void setPaySign(String paySign) {
        this.sign = paySign;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getPartnerId() {
        return partnerid;
    }

    public void setPartnerId(String partnerId) {
        this.partnerid = partnerId;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

}
