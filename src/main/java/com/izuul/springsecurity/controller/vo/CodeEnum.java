package com.izuul.springsecurity.controller.vo;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 01:28
 */
public enum CodeEnum {
    /**
     * 成功
     */
    SUCCESS(20000, "成功"),
    /**
     * 账号不存在
     */
    ACCOUNT_ERROR(60207, "账号不存在"),
    /**
     * 密码错误
     */
    PASSWORD_ERROR(60204, "密码错误"),
    /**
     * 用户名已存在
     */
    ACCOUNT_ALREADY_EXISTS(60205, "用户名已存在"),
    /**
     * 该用户不允许删除
     */
    NOT_ALLOWED_DELETE(60206, "该用户不允许删除"),
    /**
     * 登录失败
     */
    LOGIN_FAILED(50008, "登录失败，无法获取用户详细信息"),
    /**
     * 令牌失败
     */
    TOKEN_EXPIRED(50014, "登录失效，请重新登录"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(50000, "系统异常, 请稍后再试"),
    ;

    private int code;
    private String msg;

    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
