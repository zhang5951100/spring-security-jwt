package com.izuul.springsecurity.controller.activity;

import com.github.pagehelper.PageInfo;
import com.izuul.springsecurity.controller.vo.CodeEnum;
import com.izuul.springsecurity.controller.vo.Result;
import com.izuul.springsecurity.controller.vo.VacationVO;
import com.izuul.springsecurity.entity.activity.Vacation;
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
@RequestMapping("/vacations")
@Slf4j
public class VacationController {
    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    @Qualifier("vacationServiceImpl")
    private ActivityService vacationServiceImpl;

    /**
     * 填写请假单
     *
     * @param vacationVO
     * @return
     */
    @RequestMapping(value = "/applies", method = RequestMethod.POST)
    public ResponseEntity apply(@RequestBody VacationVO vacationVO) {

        vacationServiceImpl.apply(vacationVO);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .build(), HttpStatus.OK);
    }

    /**
     * 查询用户待办流程
     *
     * @param applicantId 操作人ID
     * @return ResponseEntity
     */
    @RequestMapping(value = "/processes/{applicantId}", method = RequestMethod.GET)
    public ResponseEntity toDoProcess(@PathVariable String applicantId) {

        List<VacationVO> vacationVOs = vacationServiceImpl.toDoProcess(applicantId);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .data(vacationVOs)
                .build(), HttpStatus.OK);
    }

    /**
     * 审批
     *
     * @param vacationVO
     * @return
     */
    @RequestMapping(value = "/approvals", method = RequestMethod.POST)
    public ResponseEntity approve(@RequestBody VacationVO vacationVO) {

        vacationServiceImpl.approve(vacationVO);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .build(), HttpStatus.OK);
    }

    /**
     * 查看历史记录
     *
     * @param operator 操作人
     * @return
     */
    @RequestMapping(value = "/histories", method = RequestMethod.GET)
    public ResponseEntity getHistories(@RequestParam("operator") String operator) {

        List<VacationVO> vacations = vacationServiceImpl.getHistories(operator);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .data(vacations)
                .build(), HttpStatus.OK);
    }


    /**
     * 查看以发布流程
     *
     * @param applicantId 操作人
     * @return
     */
    @RequestMapping(value = "/vacations/{applicantId}/{page}/{size}", method = RequestMethod.GET)
    public ResponseEntity getMyLeaves(@PathVariable String applicantId,
                                      @PathVariable int page, @PathVariable int size) {

        PageInfo pageInfo = vacationServiceImpl.getMyLeaves(applicantId, page, size);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .data(pageInfo)
                .build(), HttpStatus.OK);
    }

    public void getHistoryProcessInstance() {
        // 历史任务Service
        HistoricProcessInstance hpi = processEngine.getHistoryService()
                // 创建历史流程实例查询
                .createHistoricProcessInstanceQuery()
                // 指定流程实例ID
                .processInstanceId("2501")
                .singleResult();
        System.out.println("流程实例ID:" + hpi.getId());
        System.out.println("创建时间：" + hpi.getStartTime());
        System.out.println("结束时间：" + hpi.getEndTime());
    }


    public void historyActInstanceList(String processInstanceId) {
        // 历史任务Service
        List<HistoricActivityInstance> list = processEngine.getHistoryService()
                // 创建历史活动实例查询
                .createHistoricActivityInstanceQuery()
                // 指定流程实例id
                .processInstanceId(processInstanceId)
                // 查询已经完成的任务
                .finished()
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
