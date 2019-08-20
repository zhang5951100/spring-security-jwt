package com.izuul.springsecurity.entity.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-16 08:04
 **/
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Leave extends Application implements Serializable {

    private static final long serialVersionUID = 2248469053125414262L;

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
     * 假期天数
     */
    private double totalDay;

    /**
     * 请假描述
     */
    private String desc;

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
}

