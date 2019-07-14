package com.izuul.springsecurity.controller.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 01:25
 */
@Data
@Builder
public class Result<T> {
    private int code;
    private String message;
    private T data;
}
