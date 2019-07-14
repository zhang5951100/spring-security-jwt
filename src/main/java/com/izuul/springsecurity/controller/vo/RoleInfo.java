package com.izuul.springsecurity.controller.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 14:12
 */
@Data
@Accessors(chain = true)
public class RoleInfo {

    private String key;
    private String name;
    private String description;
    private List<RouteInfo> routes;
}
