package com.anxin.kitchen.response;

/**
 * 基础响应类
 * Created by xujianjun on 2018/4/6.
 */

public class BaseResponse {
    private long code;
    private String message;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
