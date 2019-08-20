package com.izuul.springsecurity.controller.activity;

import com.izuul.springsecurity.controller.vo.CodeEnum;
import com.izuul.springsecurity.controller.vo.Result;
import com.izuul.springsecurity.entity.activity.Leave;
import com.izuul.springsecurity.service.activity.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Guihong.Zhang
 * @date: 2019-08-16 07:59
 **/
@RestController
@RequestMapping("/workflow")
@Slf4j
public class LeaveController {
    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    @Qualifier("leaveServiceImpl")
    private ActivityService leaveServiceImpl;

    /**
     * 填写请假单
     *
     * @param leave
     * @return
     */
    @RequestMapping(value = "/applies", method = RequestMethod.POST)
    public ResponseEntity apply(@RequestBody Leave leave) {

        leaveServiceImpl.apply(leave);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .build(), HttpStatus.OK);
    }

    /**
     * 查询用户代办流程
     *
     * @param operator
     * @return
     */
    @RequestMapping(value = "/processes/{operator}", method = RequestMethod.GET)
    public ResponseEntity toDoProcess(@PathVariable String operator) {
        List<Leave> leaves = leaveServiceImpl.toDoProcess(operator);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .data(leaves)
                .build(), HttpStatus.OK);
    }

    /**
     * 审批
     *
     * @param leave
     * @return
     */
    @RequestMapping(value = "/approvals", method = RequestMethod.POST)
    public ResponseEntity approve(@RequestBody Leave leave) {
        leaveServiceImpl.approve(leave);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .build(), HttpStatus.OK);
    }

    /**
     * 查看历史记录
     *
     * @param operator
     * @return
     */
    @RequestMapping(value = "/histories", method = RequestMethod.GET)
    public ResponseEntity getHistories(@RequestParam("operator") String operator) {
        List<Leave> leaves = leaveServiceImpl.getHistories(operator);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .data(leaves)
                .build(), HttpStatus.OK);
    }


    /**
     * 查看以发布流程
     *
     * @param operator
     * @return
     */
    @RequestMapping(value = "/leaves/{operator}", method = RequestMethod.GET)
    public ResponseEntity getMyLeaves(@PathVariable String operator) {

        List<Leave> leaves = leaveServiceImpl.getMyLeaves(operator);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .data(leaves)
                .build(), HttpStatus.OK);
    }

    public void getHistoryProcessInstance() {
        HistoricProcessInstance hpi = processEngine.getHistoryService() // 历史任务Service
                .createHistoricProcessInstanceQuery() // 创建历史流程实例查询
                .processInstanceId("2501") // 指定流程实例ID
                .singleResult();
        System.out.println("流程实例ID:" + hpi.getId());
        System.out.println("创建时间：" + hpi.getStartTime());
        System.out.println("结束时间：" + hpi.getEndTime());
    }


    public void historyActInstanceList(String processInstanceId) {
        List<HistoricActivityInstance> list = processEngine.getHistoryService() // 历史任务Service
                .createHistoricActivityInstanceQuery() // 创建历史活动实例查询
                .processInstanceId(processInstanceId) // 指定流程实例id
                .finished() // 查询已经完成的任务
                .list();
        for (HistoricActivityInstance hai : list) {
            System.out.println("任务ID:" + hai.getId());
            System.out.println("流程实例ID:" + hai.getProcessInstanceId());
            System.out.println("活动名称：" + hai.getActivityName());
            System.out.println("办理人：" + hai.getAssignee());
            System.out.println("开始时间：" + hai.getStartTime());
            System.out.println("结束时间：" + hai.getEndTime());
            System.out.println("===========================");
        }
    }
}
