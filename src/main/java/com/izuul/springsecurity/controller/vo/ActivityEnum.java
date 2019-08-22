package com.izuul.springsecurity.controller.vo;

import lombok.Getter;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-22 08:51
 **/
@Getter
public enum ActivityEnum {
    /**
     * 审批中
     */
    APPROVING("审批中"),
    /**
     * 通过
     */
    PASS("通过"),
    /**
     * 拒绝
     */
    REJECT("拒绝"),
    ;

    private String name;

    ActivityEnum(String name) {
        this.name = name;
    }
}
