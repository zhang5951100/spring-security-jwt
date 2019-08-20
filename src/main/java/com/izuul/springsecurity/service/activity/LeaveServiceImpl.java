package com.izuul.springsecurity.service.activity;

import com.izuul.springsecurity.entity.activity.Leave;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-16 10:54
 **/
@Service
public class LeaveServiceImpl implements ActivityService {

    private static String VARIABLE_NAME = "leave";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngine processEngine;

    @Override
    public <T> void apply(T t) {
        Leave leave = (Leave) t;
        Map<String, Object> vars = new HashMap<>();
        vars.put("leave", leave);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(leave.getProcessName(), vars);

        // 第一个任务节点
        Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();

        Leave origin = (Leave) taskService.getVariable(task.getId(), VARIABLE_NAME);

        origin.setOperator(leave.getOperator())
                .setFirstApprover(leave.getOperator())
                .setDesc(leave.getDesc())
                .setStartDate(leave.getStartDate())
                .setEndDate(leave.getEndDate())
                .setTotalDay(leave.getTotalDay())
                .setSubmit(true);

        vars.put("leave", origin);
        taskService.complete(task.getId(), vars);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> toDoProcess(String operator) {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(operator).list();

        List<Leave> leaves = new ArrayList<>();

        taskList.forEach(t -> {
            Leave leave = (Leave) taskService.getVariable(t.getId(), VARIABLE_NAME);
            leave.setTaskId(t.getId())
                    .setTaskName(t.getName())
                    .setInstanceId(t.getProcessInstanceId())
                    .setAssignee(t.getAssignee())
                    .setCreateTime(t.getCreateTime());
            leaves.add(leave);
        });
        return (List<T>) leaves;
    }

    @Override
    public <T> void approve(T t) {
        Leave leave = (Leave) t;

        // TODO
        // 不用的时候删掉
        Task task = taskService.createTaskQuery().taskId(leave.getTaskId()).singleResult();
        Map<String, Object> vars = new HashMap<>();

        Leave origin = (Leave) taskService.getVariable(leave.getTaskId(), VARIABLE_NAME);
        // 设置下一级审批人
        BeanUtils.copyProperties(leave, origin);
        List<String> operators = new ArrayList<>();
        // 模拟多个审批人
        operators.add("admin");
        operators.add("user");

        origin.setOperators(operators);
        vars.put("leave", origin);

        taskService.complete(leave.getTaskId(), vars);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getHistories(String operator) {
        HistoryService historyService = processEngine.getHistoryService();

        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(VARIABLE_NAME).variableValueEquals("leave.operator", operator).list();
        List<Leave> leaves = new ArrayList<>();
        for (HistoricProcessInstance pi : list) {
            leaves.add((Leave) pi.getProcessVariables().get(VARIABLE_NAME));
        }
        return (List<T>) leaves;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getMyLeaves(String operator) {
        // 历史任务Service
        List<HistoricTaskInstance> taskInstanceList = processEngine.getHistoryService()
                // 创建历史任务实例查询
                .createHistoricTaskInstanceQuery()
                // 指定办理人
                .taskAssignee(operator)
                .taskName("资料提交")
                // 查询已经完成的任务
                .list();
        List<Leave> leaves = new ArrayList<>();

        taskInstanceList.forEach(t -> {
            Leave leave = new Leave();
            // 获取运行时Service
            ProcessInstance pi = processEngine.getRuntimeService()
                    // 创建流程实例查询
                    .createProcessInstanceQuery()
                    // 用流程实例ID查询
                    .processInstanceId(t.getProcessInstanceId())
                    .singleResult();
            if (pi != null) {
                leave.setStatus("进行中");
            } else {
                leave.setStatus("已完成");
            }

            leave.setTaskId(t.getId())
                    .setTaskName(t.getName())
                    .setInstanceId(t.getProcessInstanceId())
                    .setAssignee(t.getAssignee())
                    .setCreateTime(t.getCreateTime())
                    .setEndTime(t.getEndTime());
            leaves.add(leave);
        });
        return (List<T>) leaves;
    }
}
