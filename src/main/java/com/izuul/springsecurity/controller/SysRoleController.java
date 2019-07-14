package com.izuul.springsecurity.controller;

import com.izuul.springsecurity.controller.vo.CodeEnum;
import com.izuul.springsecurity.controller.vo.Result;
import com.izuul.springsecurity.controller.vo.RoleInfo;
import com.izuul.springsecurity.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 14:21
 */
@RestController
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public ResponseEntity role() {
        List<RoleInfo> roles = sysRoleService.getRoles();
        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(roles)
                .build(), HttpStatus.OK);
    }
}
