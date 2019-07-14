package com.izuul.springsecurity.controller.vo;

import com.izuul.springsecurity.entity.Child;
import com.izuul.springsecurity.entity.Meta;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 14:14
 */
@Data
@Accessors(chain = true)
public class RouteInfo {

    private String id;

    private String path;

    private String component;

    private String redirect;

    private boolean alwaysShow;

    private boolean hidden;

    private Meta meta;

    private List<Child> children;
}
