package com.izuul.springsecurity.entity.activity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-16 11:13
 **/
@Data
@Accessors(chain = true)
public class Application {
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
