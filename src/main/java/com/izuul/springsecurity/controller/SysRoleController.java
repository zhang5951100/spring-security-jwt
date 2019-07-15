package com.izuul.springsecurity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.izuul.springsecurity.controller.vo.CodeEnum;
import com.izuul.springsecurity.controller.vo.Result;
import com.izuul.springsecurity.controller.vo.RoleInfo;
import com.izuul.springsecurity.entity.SysRoute;
import com.izuul.springsecurity.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 14:21
 */
@Slf4j
@RestController
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 创建 role
     */
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public ResponseEntity insertRole(@RequestBody RoleInfo roleInfo) {
        try {
            log.info("创建角色 role: {}", objectMapper.writeValueAsString(roleInfo));
        } catch (JsonProcessingException e) {
            log.error("JSON 转化异常: {}", e.getMessage());
        }

        RoleInfo result = sysRoleService.insertRole(roleInfo);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(result)
                .build(), HttpStatus.OK);
    }

    /**
     * 删除 role
     */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteRole(@PathVariable String id) {
        log.info("删除角色: {}", id);

        sysRoleService.deleteRole(id);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .build(), HttpStatus.OK);
    }

    /**
     * 修改 role
     */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateRole(@PathVariable String id, @RequestBody RoleInfo roleInfo) {
        try {
            log.info("修改角色 id:{} role: {}", id, objectMapper.writeValueAsString(roleInfo));
        } catch (JsonProcessingException e) {
            log.error("JSON 转化异常: {}", e.getMessage());
        }

        RoleInfo result = sysRoleService.updateRole(roleInfo);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(result)
                .build(), HttpStatus.OK);
    }

    /**
     * 查询 roles
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public ResponseEntity role() {
        log.info("查询角色列表");

        List<RoleInfo> roles = sysRoleService.getRoles();
        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(roles)
                .build(), HttpStatus.OK);
    }

    /**
     * 获取路由
     */
    @RequestMapping(value = "/routes", method = RequestMethod.GET)
    public ResponseEntity route() {
        log.info("获取路由列表");

        List<SysRoute> sysRoutes = sysRoleService.getRoutes();
        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(sysRoutes)
                .build(), HttpStatus.OK);
    }
}
