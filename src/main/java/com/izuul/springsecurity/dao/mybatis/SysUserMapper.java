package com.izuul.springsecurity.dao.mybatis;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {

    List<String> findAllbyRole(String role);
}