package com.izuul.springsecurity.service.activity;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-16 10:29
 **/
public interface ActivityService {

    /**
     * 申请
     *
     * @param t 申请实例
     */
    <T> void apply(T t);

    /**
     * 待处理流程
     *
     * @param applicantId 操作人
     * @return 申请实例
     */
    <T> List<T> toDoProcess(String applicantId);

    /**
     * 审批
     *
     * @param t 申请实例
     */
    <T> void approve(T t);

    /**
     * 获取历史流程
     *
     * @param operator 操作人
     * @param <T>      申请实例
     * @return
     */
    <T> List<T> getHistories(String operator);

    /**
     * 我发起的流程
     *
     * @param applicantId 申请人 ID
     * @param <T>         申请实例
     * @return
     */
    <T> PageInfo<T> getMyLeaves(String applicantId, int page, int size);
}
