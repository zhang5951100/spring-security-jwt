package com.izuul.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 20:29
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Child {

    private String path;
    private String component;
    private String name;
    private Meta meta;

}
