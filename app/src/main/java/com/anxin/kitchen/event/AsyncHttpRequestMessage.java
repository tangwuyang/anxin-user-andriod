package com.anxin.kitchen.event;

public class AsyncHttpRequestMessage {

    private String requestCode;
    private String responseMsg;
    private String requestStatus;

    public AsyncHttpRequestMessage(String requestCode, String responseMsg, String requestStatus) {
        this.requestCode = requestCode;
        this.responseMsg = responseMsg;
        this.requestStatus = requestStatus;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
