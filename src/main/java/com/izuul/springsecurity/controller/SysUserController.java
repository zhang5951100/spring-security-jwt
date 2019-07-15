package com.izuul.springsecurity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.izuul.springsecurity.controller.vo.CodeEnum;
import com.izuul.springsecurity.controller.vo.Result;
import com.izuul.springsecurity.controller.vo.UserInfo;
import com.izuul.springsecurity.util.JwtTokenUtil;
import com.izuul.springsecurity.entity.SysUser;
import com.izuul.springsecurity.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Guihong.Zhang
 * @date 2019-07-14 09:47
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class SysUserController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    /**
     * 用户登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity token(@RequestBody SysUser sysUser) throws AuthenticationException {
        log.info("用户登录 :{}", sysUser.getUsername());
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        sysUser.getUsername(),
                        sysUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = "Bearer " + jwtTokenUtil.generateToken(authentication);

        return ResponseEntity.ok(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .data(token)
                .build());
    }

    /**
     * 获取用户信息
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseEntity info(HttpServletRequest req) {

        UserInfo userInfo = sysUserService.getUserInfo(req);
        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .message(CodeEnum.SUCCESS.getMsg())
                .data(userInfo)
                .build(), HttpStatus.OK);
    }

    /**
     * 获取用户列表
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity users() {
        log.info("获取用户列表");

        List<UserInfo> users = sysUserService.getUsers();
        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(users)
                .build(), HttpStatus.OK);
    }

    /**
     * 用户注销
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(CodeEnum.SUCCESS.getMsg())
                .build(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String hasRole() {
        return "hasRole ADMIN";
    }

    /**
     * 添加用户
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody UserInfo userInfo) {

        try {
            log.info("添加用户 userInfo: {}", objectMapper.writeValueAsString(userInfo));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        UserInfo result = sysUserService.addUser(userInfo);

        if (result == null) {
            return new ResponseEntity<>(Result.builder()
                    .code(CodeEnum.ACCOUNT_ALREADY_EXISTS.getCode())
                    .message(CodeEnum.ACCOUNT_ALREADY_EXISTS.getMsg())
                    .build(), HttpStatus.OK);
        }

        return new ResponseEntity<>(Result.builder()
                .code(CodeEnum.SUCCESS.getCode())
                .data(result)
                .build(), HttpStatus.OK);
    }
}
