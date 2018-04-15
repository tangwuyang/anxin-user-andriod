package com.anxin.kitchen.utils.pay;

/**
 * 支付宝支付响应
 * Created by xujianjun on 2018/4/9.
 */

public class AlipayResponse {
    private String memo;//描述信息
    private AlipayData result;//处理结果
    private String resultStatus;//结果码 9000	订单支付成功

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public AlipayData getResult() {
        return result;
    }

    public void setResult(AlipayData result) {
        this.result = result;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

}
