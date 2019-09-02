package com.izuul.springsecurity.dao.mybatis;

import com.izuul.springsecurity.entity.activity.Vacation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VacationMapper {
    int deleteByPrimaryKey(String id);

    int insert(Vacation record);

    int insertSelective(Vacation record);

    Vacation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Vacation record);

    int updateByPrimaryKey(Vacation record);

    List<Vacation> findAllByUserId(String userId);
}