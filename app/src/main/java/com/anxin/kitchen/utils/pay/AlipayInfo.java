package com.anxin.kitchen.utils.pay;

/**
 * 支付宝支付返回信息
 * Created by xujianjun on 2018/4/9.
 */

public class AlipayInfo {
    private String code;//结果码 9000成功
    private String msg;//处理结果的描述，信息来自于code返回结果的描述
    private String app_id;//支付宝分配给开发者的应用Id
    private String out_trade_no;//商户网站唯一订单号
    private String trade_no;//该交易在支付宝系统中的交易流水号
    private String total_amount;//该笔订单的资金总额，单位为RMB-Yuan
    private String seller_id;//收款支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字
    private String charset;//编码格式
    private String timestamp;//时间

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
