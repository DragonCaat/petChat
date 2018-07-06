package com.pet.bean;

/**
 * Created by dragon on 2018/6/14.
 * 请求的基本实体
 */

public class ResultEntity {

    private int code;
    private String message;

    private Object data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
