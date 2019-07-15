package com.izuul.springsecurity.controller.vo;

import com.izuul.springsecurity.entity.SysRole;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 09:47
 */
@Data
@Accessors(chain = true)
public class UserInfo {

    private String key;

    /**
     * 名称
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 介绍
     */
    private String introduction;

    /**
     * 角色
     */
    private List<String> roles;

    private List<SysRole> roleList;
}
