package com.izuul.springsecurity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class SysUserController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity token(@RequestBody SysUser sysUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        sysUser.getUsername(),
                        sysUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok("Bearer " + token);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello jwt";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/admin")
    public String hasRole() {
        return "hasRole ADMIN";
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody SysUser sysUser) {

        try {
            log.info("创建 user: {}", objectMapper.writeValueAsString(sysUser));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        SysUser user = sysUserService.register(sysUser);

        if (user == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
