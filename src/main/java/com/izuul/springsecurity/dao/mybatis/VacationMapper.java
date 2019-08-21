package com.izuul.springsecurity.dao.mybatis;


import com.izuul.springsecurity.entity.activity.Vacation;

public interface VacationMapper {
    int deleteByPrimaryKey(String id);

    int insert(Vacation record);

    int insertSelective(Vacation record);

    Vacation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Vacation record);

    int updateByPrimaryKey(Vacation record);
}