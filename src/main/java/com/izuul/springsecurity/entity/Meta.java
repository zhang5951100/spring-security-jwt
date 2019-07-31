package com.izuul.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 20:38
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meta {

    private String title;
    private String icon;
    private List<String> roles;
}
