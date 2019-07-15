package com.izuul.springsecurity.controller;

import com.izuul.springsecurity.controller.vo.CodeEnum;
import com.izuul.springsecurity.controller.vo.Result;
import com.izuul.springsecurity.controller.vo.RoleInfo;
import com.izuul.springsecurity.entity.SysRoute;
import com.izuul.springsecurity.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 14:21
 */
@RestController
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 创建 role
     */
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public ResponseEntity insertRole(@RequestBody RoleInfo roleInfo) {
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
        sysRoleService.deleteRole(id);

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .build(), HttpStatus.OK);
    }

    /**
     * 查询 roles
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public ResponseEntity role() {
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
        List<SysRoute> sysRoutes = sysRoleService.getRoutes();
        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(sysRoutes)
                .build(), HttpStatus.OK);
    }
}
