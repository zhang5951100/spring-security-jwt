package com.izuul.springsecurity.entity;

import lombok.Data;

import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 20:38
 */
@Data
public class Meta {

    private String title;
    private String icon;
    private List<String> roles;
}
