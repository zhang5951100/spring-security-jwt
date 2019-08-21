package com.izuul.springsecurity.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-21 13:02
 **/
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VacationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 当前事件操作者
     */
    private String operator;

    /**
     * 当前事件操作者ID
     */
    private String operatorId;

    /**
     * 当前事件操作者们
     */
    private List<String> operators;

    /**
     * 是否提交请假申请
     */
    private Boolean submit;

    /**
     * 请假开始日期
     */
    private LocalDateTime startDate;

    /**
     * 请假结束日期
     */
    private LocalDateTime endDate;

    /**
     * 假期时长
     */
    private double totalHour;

    /**
     * 假期类型
     */
    private String type;

    /**
     * 请假描述
     */
    private String explanation;

    /**
     * 第一审批人
     */
    private String firstApprover;

    /**
     * 第一审批人是否同意
     */
    private Boolean firstAgree;

    /**
     * 第一审批描述
     */
    private String firstApproveDesc;

    /**
     * 第二审批人
     */
    private String secondApprover;

    /**
     * 第二审批人是否同意
     */
    private Boolean secondAgree;

    /**
     * 第二审批描述
     */
    private String secondApproveDesc;

    /**
     * 事件 ID
     */
    private String taskId;

    /**
     * 事件 名字
     */
    private String taskName;

    /**
     * 实例 ID
     */
    private String instanceId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 代理人
     */
    private String assignee;

    /**
     * 状态
     */
    private String status;
}
