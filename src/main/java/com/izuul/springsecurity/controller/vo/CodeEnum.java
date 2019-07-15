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
     * 账号/密码错误
     */
    ACCOUNT_PASSWOR_ERROR(60204, "账号/密码错误"),
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
