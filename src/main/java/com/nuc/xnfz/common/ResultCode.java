package com.nuc.xnfz.common;

/**
 * 错误码统一规范
 */
public class ResultCode {
    //成功
    public static final int SUCCESS = 200;
    public static final int USER_SAVE_SUCCESS = 20005;
    public static final int USER_SAVE_FAILED = 20006;
    //失败
    public static final int ERROR = 500;
    public static final int USER_EXIST=20001;
    //用户名或密码错误
    public static final int LOGINERROR = 20002;
    //权限不足
    public static final int ACCESSERROR = 20003;
    //远程调用失败
    public static final int REMOTEERROR = 20004;


    public static final String USER_EXIST_MSG = "用户已存在";
    public static final String SAVE_SUCCESS_MSG = "用户保存成功";
    public static final String SAVE_FAILED_MSG = "用户保存失败";
    public static final String LOGIN_ERROR_MSG = "用户或密码错误";


}