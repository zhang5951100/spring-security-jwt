package com.izuul.springsecurity.entity.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.izuul.springsecurity.util.converter.ChildrenConverter;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-16 08:04
 **/
@Data
@Entity(name = "VACATION")
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vacation implements Serializable {

    private static final long serialVersionUID = 2248469053125414262L;

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "ID", length = 40)
    private String id;

    /**
     * 流程名称
     */
    @Column(name = "PROCESS_NAME")
    private String processName;

    /**
     * 当前事件操作者
     */
    @Column(name = "OPERATOR")
    private String operator;

    /**
     * 当前事件操作者ID
     */
    @Column(name = "OPERATOR_ID")
    private String operatorId;

    /**
     * 当前事件操作者们
     */
    @Column(name = "OPERATORS", length = 1024)
    @Convert(converter = ChildrenConverter.class)
    private List<String> operators;

    /**
     * 是否提交请假申请
     */
    @Column(name = "SUBMIT")
    private Boolean submit;

    /**
     * 请假开始日期
     */
    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    /**
     * 请假结束日期
     */
    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    /**
     * 假期时长
     */
    @Column(name = "TOTAL_HOUR")
    private double totalHour;

    /**
     * 请假类型
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 请假描述
     */
    @Column(name = "EXPLANATION")
    private String explanation;

    /**
     * 第一审批人
     */
    @Column(name = "FIRST_APPROVER")
    private String firstApprover;

    /**
     * 第一审批人是否同意
     */
    @Column(name = "FIRST_AGREE")
    private Boolean firstAgree;

    /**
     * 第一审批描述
     */
    @Column(name = "FIRST_APPROVE_DESC")
    private String firstApproveDesc;

    /**
     * 第二审批人
     */
    @Column(name = "SECOND_APPROVER")
    private String secondApprover;

    /**
     * 第二审批人是否同意
     */
    @Column(name = "SECOND_AGREE")
    private Boolean secondAgree;

    /**
     * 第二审批描述
     */
    @Column(name = "SECOND_APPROVE_DESC")
    private String secondApproveDesc;

    /**
     * 事件 ID
     */
    @Column(name = "TASK_ID")
    private String taskId;

    /**
     * 事件 名字
     */
    @Column(name = "TASK_NAME")
    private String taskName;

    /**
     * 实例 ID
     */
    @Column(name = "INSTANCE_ID")
    private String instanceId;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 结束时间
     */
    @Column(name = "endTime")
    private Date endTime;

    /**
     * 代理人
     */
    @Column(name = "assignee")
    private String assignee;

    /**
     * 状态
     */
    @Column(name = "STATUS")
    private String status;
}

