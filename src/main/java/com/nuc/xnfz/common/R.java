package com.nuc.xnfz.common;

import lombok.Data;

@Data
public class R {
    private int code = 200;
    private String message;
    private Long total;//总记录数
    private Object data;

    public R(String message){
        this.message=message;
    }

    public static R ok() {
        R r = new R("success");
        // 使用统一编码 ResultCode
        r.setCode(ResultCode.SUCCESS);
        return r;
    }
    public static R ok(Object data) {
        R r = new R("success");
        // 使用统一编码 ResultCode
        r.setCode(ResultCode.SUCCESS);
        r.setData(data);
        return r;
    }
    public static R ok(Object data, Long total) {
        R r = new R("success");
        // 使用统一编码 ResultCode
        r.setCode(ResultCode.SUCCESS);
        r.setData(data);
        r.setTotal(total);
        return r;
    }
    public static R ok(Object data, String message) {
        R r = new R("success");
        // 使用统一编码 ResultCode
        r.setCode(ResultCode.SUCCESS);
        r.setData(data);
        r.setMessage(message);
        return r;
    }
    public static R fail() {
        R r = new R("failed");
        r.setCode(ResultCode.ERROR);
        return r;
    }
    public R message(String message) {
        this.setMessage(message);
        return this;
    }
    public R data(Object data) {
        this.setData(data);
        return this;
    }
}
