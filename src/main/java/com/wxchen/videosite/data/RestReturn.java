package com.wxchen.videosite.data;

public class RestReturn<T> {
    private Boolean ok;
    private String msg;
    private T data;

    public RestReturn() {
    }

    public RestReturn(Boolean ok) {
        this.ok = ok;
    }

    public RestReturn(Boolean ok, String msg) {
        this.ok = ok;
        this.msg = msg;
    }

    public RestReturn(Boolean ok, String msg, T data) {
        this.ok = ok;
        this.msg = msg;
        this.data = data;
    }

    // getter/setter

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
