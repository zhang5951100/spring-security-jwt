package com.izuul.springsecurity.service.activity;

import com.izuul.springsecurity.controller.vo.ActivityEnum;
import com.izuul.springsecurity.controller.vo.VacationVO;
import com.izuul.springsecurity.dao.repository.VacationRepository;
import com.izuul.springsecurity.entity.activity.Vacation;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-16 10:54
 **/
@Service
public class VacationServiceImpl implements ActivityService {

    private static String VARIABLE_NAME = "vacation";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private VacationRepository vacationRepository;

    @Override
    public <T> void apply(T t) {

        List<String> operators = new ArrayList<>();
        // 模拟多个审批人
        operators.add("admin");
        operators.add("user");

        VacationVO req = (VacationVO) t;
        req.setProcessName("请假")
                .setStatus(ActivityEnum.APPROVING.getName())
                .setOperators(operators)
                .setSubmit(true);

        Map<String, Object> vars = new HashMap<>();
        vars.put("vacation", req);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(VARIABLE_NAME, vars);

        //启动完成后对数据做记录
        Vacation vacation = this.saveVacation(req, processInstance);
        // 第一个任务节点
        Task task = processEngine.getTaskService().createTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId()).singleResult();

        VacationVO origin = (VacationVO) taskService.getVariable(task.getId(), VARIABLE_NAME);

        BeanUtils.copyProperties(vacation, origin);

        vars.put("vacation", origin);
        taskService.complete(task.getId(), vars);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> toDoProcess(String operator) {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(operator)
                .orderByTaskCreateTime().desc().list();


        //根据流程实例ID查询请假申请表单数据
//        List<String> processInstanceIds = taskList.stream()
//                .map(TaskInfo::getProcessInstanceId)
//                .collect(Collectors.toList());
//        List<Vacation> vacationList = vacationRepository.findAllByInstanceIdIn(processInstanceIds);
//
        List<VacationVO> VacationVOs = new ArrayList<>();

        taskList.forEach(t -> {
            VacationVO vacationVO = (VacationVO) taskService.getVariable(t.getId(), VARIABLE_NAME);
            vacationVO.setTaskId(t.getId())
                    .setTaskName(t.getName())
                    .setInstanceId(t.getProcessInstanceId())
                    .setAssignee(t.getAssignee())
                    .setCreateTime(t.getCreateTime());
            VacationVOs.add(vacationVO);
        });
        return (List<T>) VacationVOs;
    }

    @Override
    public <T> void approve(T t) {
        VacationVO vacationVO = (VacationVO) t;

        // TODO
        // 不用的时候删掉
        Task task = taskService.createTaskQuery().taskId(vacationVO.getTaskId()).singleResult();
        Map<String, Object> vars = new HashMap<>();

//        VacationVO origin = (VacationVO) taskService.getVariable(vacationVO.getTaskId(), VARIABLE_NAME);
//        BeanUtils.copyProperties(vacationVO, origin);

        vars.put("vacation", vacationVO);

        taskService.complete(vacationVO.getTaskId(), vars);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getHistories(String operator) {

        List<HistoricProcessInstance> list = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processDefinitionKey(VARIABLE_NAME)
                .variableValueEquals("vacation.operator", operator).list();

        List<VacationVO> vacationVOs = new ArrayList<>();
        for (HistoricProcessInstance pi : list) {
            vacationVOs.add((VacationVO) pi.getProcessVariables().get(VARIABLE_NAME));
        }
        return (List<T>) vacationVOs;
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
        List<VacationVO> vacationVOs = new ArrayList<>();

        taskInstanceList.forEach(t -> {
            VacationVO vacationVO = new VacationVO();
            // 获取运行时Service
            ProcessInstance pi = processEngine.getRuntimeService()
                    // 创建流程实例查询
                    .createProcessInstanceQuery()
                    // 用流程实例ID查询
                    .processInstanceId(t.getProcessInstanceId())
                    .singleResult();
            if (pi != null) {
                vacationVO.setStatus("审批中");
            } else {
                vacationVO.setStatus("已完成");
            }

            vacationVO.setTaskId(t.getId())
                    .setTaskName(t.getName())
                    .setInstanceId(t.getProcessInstanceId())
                    .setAssignee(t.getAssignee())
                    .setCreateTime(t.getCreateTime())
                    .setEndTime(t.getEndTime());
            vacationVOs.add(vacationVO);
        });
        return (List<T>) vacationVOs;
    }

    /**
     * 存储 Vacation
     *
     * @param vacationVO
     * @param processInstance
     */
    private Vacation saveVacation(VacationVO vacationVO, ProcessInstance processInstance) {
        Vacation vacation = new Vacation();
        BeanUtils.copyProperties(vacationVO, vacation);
        vacation.setInstanceId(processInstance.getProcessInstanceId());
        return vacationRepository.save(vacation);
    }
}
