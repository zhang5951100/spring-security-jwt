package com.izuul.springsecurity.service.activity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.izuul.springsecurity.controller.vo.ActivityEnum;
import com.izuul.springsecurity.controller.vo.VacationVO;
import com.izuul.springsecurity.dao.mybatis.SysUserMapper;
import com.izuul.springsecurity.dao.mybatis.VacationMapper;
import com.izuul.springsecurity.dao.repository.SysUserRepository;
import com.izuul.springsecurity.dao.repository.VacationRepository;
import com.izuul.springsecurity.entity.SysRole;
import com.izuul.springsecurity.entity.SysUser;
import com.izuul.springsecurity.entity.activity.Vacation;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Resource
    private VacationMapper vacationMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public <T> void apply(T t) {
        // 获取一个角色的所有用户 id
        // 这些人作为审批人
        List<String> operators = sysUserMapper.findAllbyRole("ROLE_ADMIN");

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
    public <T> List<T> toDoProcess(String applicantId) {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(applicantId)
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
//        Task task = taskService.createTaskQuery().taskId(vacationVO.getTaskId()).singleResult();
        Map<String, Object> vars = new HashMap<>();

//        VacationVO origin = (VacationVO) taskService.getVariable(vacationVO.getTaskId(), VARIABLE_NAME);
//        BeanUtils.copyProperties(vacationVO, origin);

        vars.put("vacation", vacationVO);

        taskService.complete(vacationVO.getTaskId(), vars);

        if (vacationVO.getFirstAgree() != null && !vacationVO.getFirstAgree()) {
            Optional<Vacation> optional = vacationRepository.findById(vacationVO.getId());
            if (optional.isPresent()) {
                Vacation vacation = optional.get();
                vacation.setStatus(ActivityEnum.REJECT.getName());
                vacationRepository.save(vacation);
            }
        }

        if (vacationVO.getSecondAgree() != null && vacationVO.getSecondAgree()) {
            Optional<Vacation> optional = vacationRepository.findById(vacationVO.getId());
            if (optional.isPresent()) {
                Vacation vacation = optional.get();
                vacation.setStatus(ActivityEnum.PASS.getName());
                vacationRepository.save(vacation);
            }
        }
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
    public PageInfo getMyLeaves(String userId, int page, int size) {
        PageHelper.startPage(page, size);
        List<Vacation> vacations = vacationMapper.findAllByUserId(userId);
        return new PageInfo(vacations);
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
        vacation.setInstanceId(processInstance.getProcessInstanceId())
                .setCreateTime(LocalDateTime.now());
        return vacationRepository.save(vacation);
    }
}
